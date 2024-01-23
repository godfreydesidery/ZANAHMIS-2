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
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRODetail;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyMedicineBatch;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.PharmacyToPharmacyTO;
import com.orbix.api.domain.PharmacyToPharmacyTODetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.PharmacyToPharmacyTODetailModel;
import com.orbix.api.models.PharmacyToPharmacyTOModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRODetailRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRORepository;
import com.orbix.api.repositories.PharmacyMedicineBatchRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
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
public class PharmacyToPharmacyTOServiceImpl implements PharmacyToPharmacyTOService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyToPharmacyRORepository pharmacyToPharmacyRORepository;
	private final PharmacyToPharmacyRODetailRepository pharmacyToPharmacyRODetailRepository;
	private final PharmacyToPharmacyTORepository pharmacyToPharmacyTORepository;
	private final PharmacyToPharmacyTODetailRepository pharmacyToPharmacyTODetailRepository;
	private final MedicineRepository medicineRepository;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	private final PharmacyMedicineBatchRepository pharmacyMedicineBatchRepository;
	
	@Override
	public PharmacyToPharmacyTOModel createOrder(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyRO> ro = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		
		Optional<PharmacyToPharmacyTO> to = pharmacyToPharmacyTORepository.findByPharmacyToPharmacyRO(pharmacyToPharmacyRO);
		
		PharmacyToPharmacyTO order = new PharmacyToPharmacyTO();
		if(to.isEmpty()) {
			if(ro.get().getStatus().equals("SUBMITTED")) {
				ro.get().setStatus("IN-PROCESS");
				ro.get().setStatusDescription("Order under process");
				pharmacyToPharmacyRORepository.save(ro.get());
			}else {
				throw new InvalidOperationException("Could not create transfer order. Can only create transfer order for submitted requests");
			}
			order.setNo(this.requestTransferOrderNo().getNo());
			order.setOrderDate(LocalDate.now());
			order.setPharmacyToPharmacyRO(pharmacyToPharmacyRO);
			order.setRequestingPharmacy(ro.get().getRequestingPharmacy());
			order.setDeliveringPharmacy(ro.get().getDeliveringPharmacy());
			order.setStatus("PENDING");
			order.setStatusDescription("Order awaiting for verification");
			
			order.setCreatedBy(userService.getUser(request).getId());
			order.setCreatedOn(dayService.getDay().getId());
			order.setCreatedAt(dayService.getTimeStamp());
			
			order = pharmacyToPharmacyTORepository.save(order);
			
			for(PharmacyToPharmacyRODetail d : pharmacyToPharmacyRO.getPharmacyToPharmacyRODetails()) {
				PharmacyToPharmacyTODetail orderDetail = new PharmacyToPharmacyTODetail();
				orderDetail.setPharmacyToPharmacyTO(order);
				orderDetail.setMedicine(d.getMedicine());
				orderDetail.setOrderedQty(d.getOrderedQty());
				orderDetail.setStatus("PENDING");
				
				orderDetail.setCreatedBy(userService.getUser(request).getId());
				orderDetail.setCreatedOn(dayService.getDay().getId());
				orderDetail.setCreatedAt(dayService.getTimeStamp());
				
				pharmacyToPharmacyTODetailRepository.save(orderDetail);
				
			}
			//order = pharmacyToPharmacyTORepository.findByPharmacyToPharmacyRO(pharmacyToPharmacyRO).get();
			order = pharmacyToPharmacyTORepository.findById(order.getId()).get();
		}else {
			order = to.get();
		}
		
		
		/*PharmacyToPharmacyTOModel model = new PharmacyToPharmacyTOModel();
		List<PharmacyToPharmacyTODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(order.getId());
		model.setNo(order.getNo());
		model.setPharmacy(order.getPharmacy());
		model.setPharmacyToPharmacyRO(order.getPharmacyToPharmacyRO());
		model.setOrderDate(order.getOrderDate());
		model.setStatus(order.getStatus());
		if(order.getPharmacyToPharmacyTODetails() != null) {
			for(PharmacyToPharmacyTODetail d : order.getPharmacyToPharmacyTODetails()) {
				PharmacyToPharmacyTODetailModel modelDetail = new PharmacyToPharmacyTODetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedPharmacySKUQty(d.getOrderedPharmacySKUQty());
				modelDetail.setPharmacyToPharmacyTO(d.getPharmacyToPharmacyTO());

				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToPharmacyTODetails(modelDetails);
		}
		
		if(order.getCreatedAt() != null) {
			model.setCreated(order.getCreatedAt().toString()+" | "+userService.getUserById(order.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(order.getVerifiedAt() != null) {
			model.setVerified(order.getVerifiedAt().toString()+" | "+userService.getUserById(order.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(order.getApprovedAt() != null) {
			model.setApproved(order.getApprovedAt().toString()+" | "+userService.getUserById(order.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		return model;
		*/
		return showOrder(order);
	}
	
	
	@Override
	public PharmacyToPharmacyTOModel verify(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyTO> to = pharmacyToPharmacyTORepository.findById(pharmacyToPharmacyTO.getId());
		if(to.isEmpty()) {
			throw new NotFoundException("Transfer order not found");
		}
		if(!to.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not verify. Only Pending Transfer Order can be verified");
		}
		to.get().setStatus("VERIFIED");
		to.get().setStatusDescription("Order awaiting for approval");
		
		to.get().setVerifiedBy(userService.getUser(request).getId());
		to.get().setVerifiedOn(dayService.getDay().getId());
		to.get().setVerifiedAt(dayService.getTimeStamp());
		 
		PharmacyToPharmacyTO order = pharmacyToPharmacyTORepository.save(to.get());
		
		return showOrder(order);
	}
	
	@Override
	public PharmacyToPharmacyTOModel approve(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyTO> to = pharmacyToPharmacyTORepository.findById(pharmacyToPharmacyTO.getId());
		if(to.isEmpty()) {
			throw new NotFoundException("Transfer order not found");
		}
		if(!to.get().getStatus().equals("VERIFIED")) {
			throw new InvalidOperationException("Could not approve. Only verified Transfer Order can be approved");
		}
		to.get().setStatus("APPROVED");
		to.get().setStatusDescription("Order submitted for goods issuing");
		
		to.get().setApprovedBy(userService.getUser(request).getId());
		to.get().setApprovedOn(dayService.getDay().getId());
		to.get().setApprovedAt(dayService.getTimeStamp());
		 
		PharmacyToPharmacyTO order = pharmacyToPharmacyTORepository.save(to.get());
		
		return showOrder(order);
	}
	
	@Override
	public PharmacyToPharmacyTOModel issue(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyTO> to = pharmacyToPharmacyTORepository.findById(pharmacyToPharmacyTO.getId());
		if(to.isEmpty()) {
			throw new NotFoundException("Transfer order not found");
		}
		if(!to.get().getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not issue. Only approved Transfer Order can issue goods");
		}
		to.get().setStatus("GOODS-ISSUED");
		to.get().setStatusDescription("Goods issued");
		
		//to.get().setApprovedBy(userService.getUser(request).getId());
		//to.get().setApprovedOn(dayService.getDay().getId());
		//to.get().setApprovedAt(dayService.getTimeStamp());
		 
		PharmacyToPharmacyTO order = pharmacyToPharmacyTORepository.save(to.get());
		
		PharmacyToPharmacyRO ro = order.getPharmacyToPharmacyRO();
		ro.setStatus("GOODS-ISSUED");
		ro.setStatusDescription("Goods issued by pharmacy");
		pharmacyToPharmacyRORepository.save(ro);
		
		/**
		 * Put here goods issue bussiness logic, should update pharmacy stocks
		 */
		
		for(PharmacyToPharmacyTODetail pharmacyToPharmacyTODetail : order.getPharmacyToPharmacyTODetails()) {
			Optional<PharmacyMedicine> pharmacyMedicine_ = pharmacyMedicineRepository.findByPharmacyAndMedicine(order.getDeliveringPharmacy(), pharmacyToPharmacyTODetail.getMedicine());
			double originalStock = pharmacyMedicine_.get().getStock();
			
			//update stock
			if(pharmacyMedicine_.get().getStock() < pharmacyToPharmacyTODetail.getTransferedQty()) {
				throw new InvalidOperationException("Can not issue goods. Stock qty is less than transfer qty at " + pharmacyMedicine_.get().getMedicine().getName());
			}
			double newStock = originalStock - pharmacyToPharmacyTODetail.getTransferedQty();
			pharmacyMedicine_.get().setStock(pharmacyMedicine_.get().getStock() - pharmacyToPharmacyTODetail.getTransferedQty());
			//bbb
			pharmacyMedicineRepository.save(pharmacyMedicine_.get());
			
			//update stock card
			PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
			pharmacyStockCard.setMedicine(pharmacyMedicine_.get().getMedicine());
			pharmacyStockCard.setPharmacy(order.getDeliveringPharmacy());
			pharmacyStockCard.setQtyIn(0);
			pharmacyStockCard.setQtyOut(pharmacyToPharmacyTODetail.getTransferedQty());
			pharmacyStockCard.setBalance(newStock);
			pharmacyStockCard.setReference("Goods transfered to Pharmacy PTPO# " + order.getNo());
			
			pharmacyStockCard.setCreatedBy(userService.getUserId(request));
			pharmacyStockCard.setCreatedOn(dayService.getDayId());
			pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
			
			pharmacyStockCardRepository.save(pharmacyStockCard);
			
			
			//update batches
			
			double minQty = 0;
			
			List<PharmacyMedicineBatch> pharmacyMedicineBatches = pharmacyMedicineBatchRepository.findAllByPharmacyAndMedicineAndQtyGreaterThan(order.getDeliveringPharmacy(), pharmacyMedicine_.get().getMedicine(), minQty);
			
			deductBatch(pharmacyMedicineBatches, pharmacyToPharmacyTODetail.getTransferedQty());
			
			
		}
		
		return showOrder(order);
	}
	
	void deductBatch(List<PharmacyMedicineBatch> pharmacyMedicineBatches, double qty){
		try {
			PharmacyMedicineBatch batch = getEarlierBatch(pharmacyMedicineBatches);
			pharmacyMedicineBatches.remove(batch);
			
			if(qty <= batch.getQty()) {
				batch.setQty(batch.getQty() - qty);
				pharmacyMedicineBatchRepository.save(batch);
			}else if(qty > batch.getQty()) {
				double newToDeduct = batch.getQty();
				batch.setQty(0);
				pharmacyMedicineBatchRepository.save(batch);
				pharmacyMedicineBatches.remove(batch);
				deductBatch(pharmacyMedicineBatches, qty - newToDeduct);
			}	
		}catch(Exception e) {
			//do nothing
		}
			
	}
	
	PharmacyMedicineBatch getEarlierBatch(List<PharmacyMedicineBatch> pharmacyMedicineBatches) {
		List<PharmacyMedicineBatch> newBatchList = new ArrayList<>();
		boolean hasExpiry = false;
		PharmacyMedicineBatch selectedMedicineBatch = null;
		
		for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
			if(pharmacyMedicineBatch.getExpiryDate() != null) {
				hasExpiry = true;
				newBatchList.add(pharmacyMedicineBatch);
			}
		}
		if(hasExpiry == true) {
			pharmacyMedicineBatches = newBatchList;
			LocalDate expiryDate = null;
			for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
				if(expiryDate == null) {
					expiryDate = pharmacyMedicineBatch.getExpiryDate();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}else if(expiryDate.isAfter(pharmacyMedicineBatch.getExpiryDate())) {
					expiryDate = pharmacyMedicineBatch.getExpiryDate();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}
			}
		}
		
		if(hasExpiry == false) {
			Long id = null;
			for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
				if(id == null) {
					id = pharmacyMedicineBatch.getId();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}else if(id > pharmacyMedicineBatch.getId()) {
					id = pharmacyMedicineBatch.getId();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}
			}
		}
		return selectedMedicineBatch;
	}
	
	
	PharmacyToPharmacyTOModel showOrder(PharmacyToPharmacyTO order) {
		
		PharmacyToPharmacyTOModel model = new PharmacyToPharmacyTOModel();
		List<PharmacyToPharmacyTODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(order.getId());
		model.setNo(order.getNo());
		model.setRequestingPharmacy(order.getRequestingPharmacy());
		model.setPharmacyToPharmacyRO(order.getPharmacyToPharmacyRO());
		model.setDeliveringPharmacy(order.getDeliveringPharmacy());
		model.setOrderDate(order.getOrderDate());
		model.setStatus(order.getStatus());
		model.setStatusDescription(order.getStatusDescription());
		if(order.getPharmacyToPharmacyTODetails() != null) {
			for(PharmacyToPharmacyTODetail d : order.getPharmacyToPharmacyTODetails()) {
				PharmacyToPharmacyTODetailModel modelDetail = new PharmacyToPharmacyTODetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setPharmacyToPharmacyTO(d.getPharmacyToPharmacyTO());
				
				modelDetail.setPharmacyToPharmacyBatches(d.getPharmacyToPharmacyBatches());

				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToPharmacyTODetails(modelDetails);
		}
		
		if(order.getCreatedAt() != null) {
			model.setCreated(order.getCreatedAt().toString()+" | "+userService.getUserById(order.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(order.getVerifiedAt() != null) {
			model.setVerified(order.getVerifiedAt().toString()+" | "+userService.getUserById(order.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(order.getApprovedAt() != null) {
			model.setApproved(order.getApprovedAt().toString()+" | "+userService.getUserById(order.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		return model;
	}
	
	
	@Override
	public boolean saveDetail(PharmacyToPharmacyTODetail detail, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyTODetail> d = pharmacyToPharmacyTODetailRepository.findById(detail.getId());
		if(d.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		Optional<Medicine> i = medicineRepository.findByName(detail.getMedicine().getName());
		if(i.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		
		d.get().setMedicine(i.get());
		d.get().setOrderedQty(detail.getOrderedQty());
		d.get().setTransferedQty(detail.getTransferedQty());
		
		pharmacyToPharmacyTODetailRepository.save(d.get());
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		return true;
	}

	@Override
	public RecordModel requestTransferOrderNo() {
		Long id = 1L;
		try {
			id = pharmacyToPharmacyTORepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("SPT",id.toString()));
		return model;
	}	
}
