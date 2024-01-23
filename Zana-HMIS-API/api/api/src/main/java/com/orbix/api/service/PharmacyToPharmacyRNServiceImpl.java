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
import com.orbix.api.domain.Item;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyMedicineBatch;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.PharmacyToPharmacyBatch;
import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRN;
import com.orbix.api.domain.PharmacyToPharmacyRNDetail;
import com.orbix.api.domain.PharmacyToPharmacyTO;
import com.orbix.api.domain.PharmacyToPharmacyTODetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.PharmacyToPharmacyRNDetailModel;
import com.orbix.api.models.PharmacyToPharmacyRNModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ItemMedicineCoefficientRepository;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyMedicineBatchRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.PharmacyToPharmacyBatchRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRODetailRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRORepository;
import com.orbix.api.repositories.PharmacyToPharmacyRNDetailRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRNRepository;
import com.orbix.api.repositories.PharmacyToPharmacyTODetailRepository;
import com.orbix.api.repositories.PharmacyToPharmacyTORepository;
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
public class PharmacyToPharmacyRNServiceImpl implements PharmacyToPharmacyRNService {
	
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyToPharmacyRORepository pharmacyToPharmacyRORepository;
	private final PharmacyToPharmacyRODetailRepository pharmacyToPharmacyRODetailRepository;
	private final MedicineRepository medicineRepository;
	private final PharmacyToPharmacyTORepository pharmacyToPharmacyTORepository;
	private final PharmacyToPharmacyTODetailRepository pharmacyToPharmacyTODetailRepository;
	private final PharmacyToPharmacyRNRepository pharmacyToPharmacyRNRepository;
	private final PharmacyToPharmacyRNDetailRepository pharmacyToPharmacyRNDetailRepository;
	private final ItemRepository itemRepository;
	private final ItemMedicineCoefficientRepository itemMedicineCoefficientRepository;
	//private final PharmacyToPharmacyBatchRepository pharmacyToPharmacyBatchRepository;
	private final PharmacyMedicineBatchRepository pharmacyMedicineBatchRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	private final PharmacyToPharmacyBatchRepository pharmacyToPharmacyBatchRepository;
	
	@Override
	public PharmacyToPharmacyRNModel createReceivingNote(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyTO> t = pharmacyToPharmacyTORepository.findByPharmacyToPharmacyRO(pharmacyToPharmacyRO);
		if(t.isEmpty()) {
			throw new NotFoundException("Corresponding transfer order not found");
		}
		
		if(!t.get().getStatus().equals("GOODS-ISSUED")) {
			throw new InvalidOperationException("Could not create/process GRN. Goods not issued");
		}
		
		Optional<PharmacyToPharmacyRN> nt = pharmacyToPharmacyRNRepository.findByPharmacyToPharmacyTO(t.get());
		PharmacyToPharmacyRN note;
		PharmacyToPharmacyRN receiveNote;
		if(nt.isEmpty()) {
			note = new PharmacyToPharmacyRN();
			note.setNo(this.requestReceivingNoteNo().getNo());
			note.setReceivingDate(LocalDate.now());
			note.setPharmacyToPharmacyTO(t.get());
			note.setRequestingPharmacy(t.get().getRequestingPharmacy());
			note.setDeliveringPharmacy(pharmacyToPharmacyRO.getDeliveringPharmacy());
			note.setStatus("PENDING");
			
			note.setCreatedBy(userService.getUser(request).getId());
			note.setCreatedOn(dayService.getDay().getId());
			note.setCreatedAt(dayService.getTimeStamp());
			
			note = pharmacyToPharmacyRNRepository.save(note);
			
			for(PharmacyToPharmacyTODetail d : t.get().getPharmacyToPharmacyTODetails()) {
				PharmacyToPharmacyRNDetail receivingNoteDetail = new PharmacyToPharmacyRNDetail();
				receivingNoteDetail.setPharmacyToPharmacyRN(note);
				receivingNoteDetail.setMedicine(d.getMedicine());				
				receivingNoteDetail.setOrderedQty(d.getOrderedQty());
				receivingNoteDetail.setReceivedQty(d.getTransferedQty());
				receivingNoteDetail.setStatus("PENDING");
				
				receivingNoteDetail.setCreatedBy(userService.getUser(request).getId());
				receivingNoteDetail.setCreatedOn(dayService.getDay().getId());
				receivingNoteDetail.setCreatedAt(dayService.getTimeStamp());
				
				receivingNoteDetail = pharmacyToPharmacyRNDetailRepository.save(receivingNoteDetail);
				
				for(PharmacyToPharmacyBatch b : d.getPharmacyToPharmacyBatches()) {
					b.setPharmacyToPharmacyRNDetail(receivingNoteDetail);
					pharmacyToPharmacyBatchRepository.save(b);
				}
				
				
			}
			receiveNote = pharmacyToPharmacyRNRepository.findById(note.getId()).get();			
		}else {
			receiveNote = nt.get();
		}
		
		
		PharmacyToPharmacyRNModel model = new PharmacyToPharmacyRNModel();
		List<PharmacyToPharmacyRNDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(receiveNote.getId());
		model.setNo(receiveNote.getNo());
		model.setRequestingPharmacy(receiveNote.getRequestingPharmacy());
		model.setDeliveringPharmacy(receiveNote.getDeliveringPharmacy());
		model.setPharmacyToPharmacyTO(receiveNote.getPharmacyToPharmacyTO());
		model.setReceivingDate(receiveNote.getReceivingDate());
		model.setStatus(receiveNote.getStatus());
		
		List<PharmacyToPharmacyRNDetail> ds = pharmacyToPharmacyRNDetailRepository.findAllByPharmacyToPharmacyRN(receiveNote);
		if(ds != null) {
			for(PharmacyToPharmacyRNDetail d : ds) {
				PharmacyToPharmacyRNDetailModel modelDetail = new PharmacyToPharmacyRNDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setReceivedQty(d.getReceivedQty());
				modelDetail.setPharmacyToPharmacyRN(d.getPharmacyToPharmacyRN());
				
				List<PharmacyToPharmacyBatch> bs = pharmacyToPharmacyBatchRepository.findAllByPharmacyToPharmacyRNDetail(d);				
				modelDetail.setPharmacyToPharmacyBatches(bs);
				
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToPharmacyRNDetails(modelDetails);
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
	public PharmacyToPharmacyRNModel approveReceivingNote(PharmacyToPharmacyRN receiveNote, HttpServletRequest request) {
		
		//update pharmacy batch
		for(PharmacyToPharmacyRNDetail detail : receiveNote.getPharmacyToPharmacyRNDetails()) {
			
			Optional<PharmacyMedicine> pharmacyMedicine_ = pharmacyMedicineRepository.findByPharmacyAndMedicine(receiveNote.getRequestingPharmacy(), detail.getMedicine());
			//update stock
			double originalStock = pharmacyMedicine_.get().getStock();
			double newStock = originalStock + detail.getReceivedQty();
			pharmacyMedicine_.get().setStock(newStock);
			pharmacyMedicineRepository.save(pharmacyMedicine_.get());
			
			//update pharmacy stock card
			if(detail.getReceivedQty() > 0) {
				PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
				pharmacyStockCard.setMedicine(detail.getMedicine());
				pharmacyStockCard.setPharmacy(receiveNote.getRequestingPharmacy());
				pharmacyStockCard.setQtyIn(detail.getReceivedQty());
				pharmacyStockCard.setQtyOut(0);
				pharmacyStockCard.setBalance(newStock);
				pharmacyStockCard.setReference("Medicine received # " + receiveNote.getNo());
				
				pharmacyStockCard.setCreatedBy(userService.getUserId(request));
				pharmacyStockCard.setCreatedOn(dayService.getDayId());
				pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
				
				pharmacyStockCardRepository.save(pharmacyStockCard);
			}
			
			/*for(PharmacyToPharmacyBatch batch : detail.getPharmacyToPharmacyBatches()) {
				
				PharmacyMedicineBatch pharmacyMedicineBatch = new PharmacyMedicineBatch();
				pharmacyMedicineBatch.setNo(batch.getNo());
				pharmacyMedicineBatch.setMedicine(batch.getPharmacyToPharmacyRNDetail().getMedicine());
				pharmacyMedicineBatch.setManufacturedDate(batch.getManufacturedDate());
				pharmacyMedicineBatch.setExpiryDate(batch.getExpiryDate());
				pharmacyMedicineBatch.setQty(batch.getPharmacySKUQty());
				pharmacyMedicineBatch.setPharmacy(receiveNote.getPharmacy());
				
				pharmacyMedicineBatchRepository.save(pharmacyMedicineBatch);
			}*/
		}
		
		
		
		
		PharmacyToPharmacyRNModel model = new PharmacyToPharmacyRNModel();
		List<PharmacyToPharmacyRNDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(receiveNote.getId());
		model.setNo(receiveNote.getNo());
		model.setRequestingPharmacy(receiveNote.getRequestingPharmacy());
		model.setPharmacyToPharmacyTO(receiveNote.getPharmacyToPharmacyTO());
		model.setReceivingDate(receiveNote.getReceivingDate());
		model.setStatus(receiveNote.getStatus());
		
		List<PharmacyToPharmacyRNDetail> ds = pharmacyToPharmacyRNDetailRepository.findAllByPharmacyToPharmacyRN(receiveNote);
		if(ds != null) {
			for(PharmacyToPharmacyRNDetail d : ds) {
				PharmacyToPharmacyRNDetailModel modelDetail = new PharmacyToPharmacyRNDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setReceivedQty(d.getReceivedQty());
				modelDetail.setPharmacyToPharmacyRN(d.getPharmacyToPharmacyRN());
				
				//List<PharmacyToPharmacyBatch> bs = pharmacyToPharmacyBatchRepository.findAllByPharmacyToPharmacyRNDetail(d);				
				//modelDetail.setPharmacyToPharmacyBatches(bs);
				
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToPharmacyRNDetails(modelDetails);
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
	public boolean saveDetail(PharmacyToPharmacyRNDetail detail, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyRNDetail> d = pharmacyToPharmacyRNDetailRepository.findById(detail.getId());
		if(d.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		//Optional<Item> i = itemRepository.findByName(detail.getItem().getName());
		//if(i.isEmpty()) {
			//throw new NotFoundException("Item not found");
		//}
		
		
		//d.get().setItem(i.get());
		//d.get().setTransferedPharmacySKUQty(detail.getTransferedPharmacySKUQty());
		//d.get().setTransferedPharmacySKUQty(detail.getTransferedPharmacySKUQty() * imc.get().getCoefficient());
		
		pharmacyToPharmacyRNDetailRepository.save(d.get());
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		return true;
	}
	
	
	
	

	@Override
	public RecordModel requestReceivingNoteNo() {
		Long id = 1L;
		try {
			id = pharmacyToPharmacyRNRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PPRN",id.toString()));
		return model;
	}
}
