/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.GoodsReceivedNoteDetailBatch;
import com.orbix.api.domain.Item;
import com.orbix.api.domain.ItemMedicineCoefficient;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyMedicineBatch;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.PharmacyToStoreRODetail;
import com.orbix.api.domain.StoreItem;
import com.orbix.api.domain.StoreItemBatch;
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.StoreToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyRNDetail;
import com.orbix.api.domain.StoreToPharmacyTO;
import com.orbix.api.domain.StoreToPharmacyTODetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.StoreToPharmacyRNDetailModel;
import com.orbix.api.models.StoreToPharmacyRNModel;
import com.orbix.api.models.StoreToPharmacyTODetailModel;
import com.orbix.api.models.StoreToPharmacyTOModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ItemMedicineCoefficientRepository;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyMedicineBatchRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.PharmacyToStoreRODetailRepository;
import com.orbix.api.repositories.PharmacyToStoreRORepository;
import com.orbix.api.repositories.StoreToPharmacyBatchRepository;
import com.orbix.api.repositories.StoreToPharmacyRNDetailRepository;
import com.orbix.api.repositories.StoreToPharmacyRNRepository;
import com.orbix.api.repositories.StoreToPharmacyTODetailRepository;
import com.orbix.api.repositories.StoreToPharmacyTORepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoreToPharmacyRNServiceImpl implements StoreToPharmacyRNService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyToStoreRORepository pharmacyToStoreRORepository;
	private final PharmacyToStoreRODetailRepository pharmacyToStoreRODetailRepository;
	private final MedicineRepository medicineRepository;
	private final StoreToPharmacyTORepository storeToPharmacyTORepository;
	private final StoreToPharmacyTODetailRepository storeToPharmacyTODetailRepository;
	private final StoreToPharmacyRNRepository storeToPharmacyRNRepository;
	private final StoreToPharmacyRNDetailRepository storeToPharmacyRNDetailRepository;
	private final ItemRepository itemRepository;
	private final ItemMedicineCoefficientRepository itemMedicineCoefficientRepository;
	private final StoreToPharmacyBatchRepository storeToPharmacyBatchRepository;
	private final PharmacyMedicineBatchRepository pharmacyMedicineBatchRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	
	@Override
	public StoreToPharmacyRNModel createReceivingNote(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		Optional<StoreToPharmacyTO> t = storeToPharmacyTORepository.findByPharmacyToStoreRO(pharmacyToStoreRO);
		if(t.isEmpty()) {
			throw new NotFoundException("Corresponding transfer order not found");
		}
		
		if(!t.get().getStatus().equals("GOODS-ISSUED")) {
			throw new InvalidOperationException("Could not create/process GRN. Goods not issued");
		}
		
		Optional<StoreToPharmacyRN> nt = storeToPharmacyRNRepository.findByStoreToPharmacyTO(t.get());
		StoreToPharmacyRN note;
		StoreToPharmacyRN receiveNote;
		if(nt.isEmpty()) {
			note = new StoreToPharmacyRN();
			note.setNo(this.requestReceivingNoteNo().getNo());
			note.setReceivingDate(LocalDate.now());
			note.setStoreToPharmacyTO(t.get());
			note.setPharmacy(t.get().getPharmacy());
			note.setStore(pharmacyToStoreRO.getStore());
			note.setStatus("PENDING");
			
			note.setCreatedBy(userService.getUser(request).getId());
			note.setCreatedOn(dayService.getDay().getId());
			note.setCreatedAt(dayService.getTimeStamp());
			
			note = storeToPharmacyRNRepository.save(note);
			
			for(StoreToPharmacyTODetail d : t.get().getStoreToPharmacyTODetails()) {
				StoreToPharmacyRNDetail receivingNoteDetail = new StoreToPharmacyRNDetail();
				receivingNoteDetail.setStoreToPharmacyRN(note);
				receivingNoteDetail.setMedicine(d.getMedicine());				
				receivingNoteDetail.setOrderedPharmacySKUQty(d.getOrderedPharmacySKUQty());
				receivingNoteDetail.setReceivedPharmacySKUQty(d.getTransferedPharmacySKUQty());
				receivingNoteDetail.setItem(d.getItem());
				receivingNoteDetail.setReceivedStoreSKUQty(d.getTransferedStoreSKUQty());
				receivingNoteDetail.setStatus("PENDING");
				
				receivingNoteDetail.setCreatedBy(userService.getUser(request).getId());
				receivingNoteDetail.setCreatedOn(dayService.getDay().getId());
				receivingNoteDetail.setCreatedAt(dayService.getTimeStamp());
				
				receivingNoteDetail = storeToPharmacyRNDetailRepository.save(receivingNoteDetail);
				
				for(StoreToPharmacyBatch b : d.getStoreToPharmacyBatches()) {
					b.setStoreToPharmacyRNDetail(receivingNoteDetail);
					storeToPharmacyBatchRepository.save(b);
				}
				
				
			}
			receiveNote = storeToPharmacyRNRepository.findById(note.getId()).get();			
		}else {
			receiveNote = nt.get();
		}
		
		
		StoreToPharmacyRNModel model = new StoreToPharmacyRNModel();
		List<StoreToPharmacyRNDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(receiveNote.getId());
		model.setNo(receiveNote.getNo());
		model.setPharmacy(receiveNote.getPharmacy());
		model.setStoreToPharmacyTO(receiveNote.getStoreToPharmacyTO());
		model.setReceivingDate(receiveNote.getReceivingDate());
		model.setStatus(receiveNote.getStatus());
		
		List<StoreToPharmacyRNDetail> ds = storeToPharmacyRNDetailRepository.findAllByStoreToPharmacyRN(receiveNote);
		if(ds != null) {
			for(StoreToPharmacyRNDetail d : ds) {
				StoreToPharmacyRNDetailModel modelDetail = new StoreToPharmacyRNDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedPharmacySKUQty(d.getOrderedPharmacySKUQty());
				modelDetail.setReceivedPharmacySKUQty(d.getReceivedPharmacySKUQty());
				modelDetail.setItem(d.getItem());
				modelDetail.setReceivedStoreSKUQty(d.getReceivedStoreSKUQty());
				modelDetail.setStoreToPharmacyRN(d.getStoreToPharmacyRN());
				
				List<StoreToPharmacyBatch> bs = storeToPharmacyBatchRepository.findAllByStoreToPharmacyRNDetail(d);				
				modelDetail.setStoreToPharmacyBatches(bs);
				
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setStoreToPharmacyRNDetails(modelDetails);
		}
		
		if(receiveNote.getCreatedAt() != null) {
			model.setCreated(receiveNote.getCreatedAt().toString()+" | "+userService.getUserById(receiveNote.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(receiveNote.getVerifiedAt() != null) {
			model.setVerified(receiveNote.getVerifiedAt().toString()+" | "+userService.getUserById(receiveNote.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(receiveNote.getApprovedAt() != null) {
			model.setApproved(receiveNote.getApprovedAt().toString()+" | "+userService.getUserById(receiveNote.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		return model;
	}
	
	@Override
	public StoreToPharmacyRNModel approveReceivingNote(StoreToPharmacyRN receiveNote, HttpServletRequest request) {
		
		//update pharmacy batch
		for(StoreToPharmacyRNDetail detail : receiveNote.getStoreToPharmacyRNDetails()) {
			
			Optional<PharmacyMedicine> pharmacyMedicine_ = pharmacyMedicineRepository.findByPharmacyAndMedicine(receiveNote.getPharmacy(), detail.getMedicine());
			//update stock
			double originalStock = pharmacyMedicine_.get().getStock();
			double newStock = originalStock + detail.getReceivedPharmacySKUQty();
			pharmacyMedicine_.get().setStock(newStock);
			pharmacyMedicineRepository.save(pharmacyMedicine_.get());
			
			//update pharmacy stock card
			if(detail.getReceivedPharmacySKUQty() > 0) {
				PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
				pharmacyStockCard.setMedicine(detail.getMedicine());
				pharmacyStockCard.setPharmacy(receiveNote.getPharmacy());
				pharmacyStockCard.setQtyIn(detail.getReceivedPharmacySKUQty());
				pharmacyStockCard.setQtyOut(0);
				pharmacyStockCard.setBalance(newStock);
				pharmacyStockCard.setReference("Medicine received # " + receiveNote.getNo());
				
				pharmacyStockCard.setCreatedBy(userService.getUserId(request));
				pharmacyStockCard.setCreatedOn(dayService.getDayId());
				pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
				
				pharmacyStockCardRepository.save(pharmacyStockCard);
			}
			
			for(StoreToPharmacyBatch batch : detail.getStoreToPharmacyBatches()) {
				
				PharmacyMedicineBatch pharmacyMedicineBatch = new PharmacyMedicineBatch();
				pharmacyMedicineBatch.setNo(batch.getNo());
				pharmacyMedicineBatch.setMedicine(batch.getStoreToPharmacyRNDetail().getMedicine());
				pharmacyMedicineBatch.setManufacturedDate(batch.getManufacturedDate());
				pharmacyMedicineBatch.setExpiryDate(batch.getExpiryDate());
				pharmacyMedicineBatch.setQty(batch.getPharmacySKUQty());
				pharmacyMedicineBatch.setPharmacy(receiveNote.getPharmacy());
				
				pharmacyMedicineBatchRepository.save(pharmacyMedicineBatch);
			}
		}
		
		
		
		
		StoreToPharmacyRNModel model = new StoreToPharmacyRNModel();
		List<StoreToPharmacyRNDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(receiveNote.getId());
		model.setNo(receiveNote.getNo());
		model.setPharmacy(receiveNote.getPharmacy());
		model.setStoreToPharmacyTO(receiveNote.getStoreToPharmacyTO());
		model.setReceivingDate(receiveNote.getReceivingDate());
		model.setStatus(receiveNote.getStatus());
		
		List<StoreToPharmacyRNDetail> ds = storeToPharmacyRNDetailRepository.findAllByStoreToPharmacyRN(receiveNote);
		if(ds != null) {
			for(StoreToPharmacyRNDetail d : ds) {
				StoreToPharmacyRNDetailModel modelDetail = new StoreToPharmacyRNDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedPharmacySKUQty(d.getOrderedPharmacySKUQty());
				modelDetail.setReceivedPharmacySKUQty(d.getReceivedPharmacySKUQty());
				modelDetail.setItem(d.getItem());
				modelDetail.setReceivedStoreSKUQty(d.getReceivedStoreSKUQty());
				modelDetail.setStoreToPharmacyRN(d.getStoreToPharmacyRN());
				
				List<StoreToPharmacyBatch> bs = storeToPharmacyBatchRepository.findAllByStoreToPharmacyRNDetail(d);				
				modelDetail.setStoreToPharmacyBatches(bs);
				
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setStoreToPharmacyRNDetails(modelDetails);
		}
		
		if(receiveNote.getCreatedAt() != null) {
			model.setCreated(receiveNote.getCreatedAt().toString()+" | "+userService.getUserById(receiveNote.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(receiveNote.getVerifiedAt() != null) {
			model.setVerified(receiveNote.getVerifiedAt().toString()+" | "+userService.getUserById(receiveNote.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(receiveNote.getApprovedAt() != null) {
			model.setApproved(receiveNote.getApprovedAt().toString()+" | "+userService.getUserById(receiveNote.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		return model;
	}
	
	
	
	
	
	
	
	@Override
	public boolean saveDetail(StoreToPharmacyRNDetail detail, HttpServletRequest request) {
		
		Optional<StoreToPharmacyRNDetail> d = storeToPharmacyRNDetailRepository.findById(detail.getId());
		if(d.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		Optional<Item> i = itemRepository.findByName(detail.getItem().getName());
		if(i.isEmpty()) {
			throw new NotFoundException("Item not found");
		}
		
		
		//d.get().setItem(i.get());
		//d.get().setTransferedStoreSKUQty(detail.getTransferedStoreSKUQty());
		//d.get().setTransferedPharmacySKUQty(detail.getTransferedStoreSKUQty() * imc.get().getCoefficient());
		
		storeToPharmacyRNDetailRepository.save(d.get());
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		return true;
	}
	
	
	
	

	@Override
	public RecordModel requestReceivingNoteNo() {
		Long id = 1L;
		try {
			id = storeToPharmacyRNRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PGRN",id.toString()));
		return model;
	}
}
