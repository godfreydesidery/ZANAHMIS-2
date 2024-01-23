/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.PharmacyToStoreRODetail;
import com.orbix.api.domain.Store;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PharmacyToStoreRODetailModel;
import com.orbix.api.models.PharmacyToStoreROModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyToStoreRODetailRepository;
import com.orbix.api.repositories.PharmacyToStoreRORepository;
import com.orbix.api.repositories.StoreRepository;
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
public class PharmacyToStoreROServiceImpl implements PharmacyToStoreROService {
	
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyToStoreRORepository pharmacyToStoreRORepository;
	private final PharmacyToStoreRODetailRepository pharmacyToStoreRODetailRepository;
	private final MedicineRepository medicineRepository;
	private final StoreRepository storeRepository;
	
	@Override
	public PharmacyToStoreROModel save(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro = new PharmacyToStoreRO();
		
		if(pharmacyToStoreRO.getId() == null) {
			Optional<Pharmacy> pharm = pharmacyRepository.findById(pharmacyToStoreRO.getPharmacy().getId());
			if(pharm.isEmpty()) {
				throw new NotFoundException("Pharmacy not found");
			}
			
			Optional<Store> store_ = storeRepository.findById(pharmacyToStoreRO.getStore().getId());
			if(store_.isEmpty()) {
				throw new NotFoundException("Store not found");
			}
			if(!pharmacyToStoreRO.getStore().getCode().equals(store_.get().getCode())) {
				throw new InvalidOperationException("Invalid Store");
			}
			
			pharmacyToStoreRO.setCreatedBy(userService.getUser(request).getId());
			pharmacyToStoreRO.setCreatedOn(dayService.getDay().getId());
			pharmacyToStoreRO.setCreatedAt(dayService.getTimeStamp());
			
			pharmacyToStoreRO.setStatus("PENDING");
			pharmacyToStoreRO.setStatusDescription("Order pending for verification");
			
			ro = pharmacyToStoreRO;
			ro.setStore(store_.get());
		}else {
			Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
			if(pts.isEmpty()) {
				throw new NotFoundException("Order not found");
			}
			if(!pts.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Can not edit. Only pending orders can be edited");
			}
			if(!pharmacyToStoreRO.getNo().equals(pts.get().getNo())) {
				throw new InvalidOperationException("Editing order no is not allowed");
			}
			if(pharmacyToStoreRO.getPharmacy().getId() != pts.get().getPharmacy().getId()) {
				throw new InvalidOperationException("Editing pharmacy is not allowed");
			}
			pts.get().setValidUntil(pharmacyToStoreRO.getValidUntil());
			ro = pts.get();
		}
		ro = pharmacyToStoreRORepository.save(ro);
		/*PharmacyToStoreROModel model = new PharmacyToStoreROModel();
		
		List<PharmacyToStoreRODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setPharmacy(ro.getPharmacy());
		model.setOrderDate(ro.getOrderDate());
		model.setValidUntil(ro.getValidUntil());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		if(ro.getPharmacyToStoreRODetails() != null) {
			for(PharmacyToStoreRODetail d : ro.getPharmacyToStoreRODetails()) {
				PharmacyToStoreRODetailModel modelDetail = new PharmacyToStoreRODetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setReceivedQty(d.getReceivedQty());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToStoreRODetails(modelDetails);
		}
		
		if(ro.getCreatedAt() != null) {
			model.setCreated(ro.getCreatedAt().toString()+" | "+userService.getUserById(ro.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(ro.getVerifiedAt() != null) {
			model.setVerified(ro.getVerifiedAt().toString()+" | "+userService.getUserById(ro.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(ro.getApprovedAt() != null) {
			model.setApproved(ro.getApprovedAt().toString()+" | "+userService.getUserById(ro.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		return model;*/
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToStoreROModel verify(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro;
		
		Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToStoreRO.getPharmacy().getId() != pts.get().getPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not verify. Only pending orders can be verified");
		}
		if(pts.get().getPharmacyToStoreRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not verify. Order has no items");
		}
		
		pts.get().setStatus("VERIFIED");
		pts.get().setStatusDescription("Order awaiting for approval");
		
		pts.get().setVerifiedBy(userService.getUser(request).getId());
		pts.get().setVerifiedOn(dayService.getDay().getId());
		pts.get().setVerifiedAt(dayService.getTimeStamp());
		
		ro = pharmacyToStoreRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToStoreROModel approve(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro;
		
		Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToStoreRO.getPharmacy().getId() != pts.get().getPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("VERIFIED")) {
			throw new InvalidOperationException("Could not approve. Only verified orders can be approved");
		}
		if(pts.get().getPharmacyToStoreRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not approve. Order has no items");
		}
		
		pts.get().setStatus("APPROVED");
		pts.get().setStatusDescription("Order awaiting for submission");
		
		pts.get().setApprovedBy(userService.getUser(request).getId());
		pts.get().setApprovedOn(dayService.getDay().getId());
		pts.get().setApprovedAt(dayService.getTimeStamp());
		
		ro = pharmacyToStoreRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToStoreROModel submit(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro;
		
		Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToStoreRO.getPharmacy().getId() != pts.get().getPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not submit. Only approved orders can be submitted");
		}
		if(pts.get().getPharmacyToStoreRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not submit. Order has no items");
		}
		
		pts.get().setStatus("SUBMITTED");
		pts.get().setStatusDescription("Submited to store. Order awaiting for processing");
		
		ro = pharmacyToStoreRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToStoreROModel _return(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro;
		
		Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!pts.get().getStatus().equals("SUBMITTED")) {
			throw new InvalidOperationException("Could not returned. Only submitted orders can be returned");
		}
		
		pts.get().setStatus("RETURNED");
		pts.get().setStatusDescription("Order returned for correction");
		
		ro = pharmacyToStoreRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToStoreROModel reject(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request) {
		
		PharmacyToStoreRO ro;
		
		Optional<PharmacyToStoreRO> pts = pharmacyToStoreRORepository.findById(pharmacyToStoreRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!pts.get().getStatus().equals("SUBMITTED")) {
			throw new InvalidOperationException("Could not reject. Only submitted orders can be rejected");
		}
		
		pts.get().setStatus("REJECTED");
		pts.get().setStatusDescription("Order rejected");
		
		ro = pharmacyToStoreRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public boolean saveDetail(PharmacyToStoreRODetail detail, HttpServletRequest request) {
		
		Optional<PharmacyToStoreRO> ro = pharmacyToStoreRORepository.findById(detail.getPharmacyToStoreRO().getId());
		if(ro.isEmpty()) {
			throw new NotFoundException("Requisition order not found");
		}
		Optional<Medicine> med = medicineRepository.findByName(detail.getMedicine().getName());
		if(med.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		if(detail.getId() == null) {
			List<PharmacyToStoreRODetail> det = pharmacyToStoreRODetailRepository.findAllByPharmacyToStoreROAndMedicine(ro.get(), med.get());
			if(!det.isEmpty()) {
				throw new InvalidOperationException("Duplicates are not allowed");
			}
		}
		
		
		PharmacyToStoreRODetail d = new PharmacyToStoreRODetail();
		d.setId(detail.getId());
		d.setPharmacyToStoreRO(ro.get());
		d.setMedicine(med.get());
		d.setOrderedQty(detail.getOrderedQty());
		d.setReceivedQty(0);
		d.setStatus("PENDING");
		
		d.setCreatedBy(userService.getUser(request).getId());
		d.setCreatedOn(dayService.getDay().getId());
		d.setCreatedAt(dayService.getTimeStamp());
		
		pharmacyToStoreRODetailRepository.save(d);
	
		return true;
	}
	
	private PharmacyToStoreROModel showOrder(PharmacyToStoreRO ro) {
		PharmacyToStoreROModel model = new PharmacyToStoreROModel();
		List<PharmacyToStoreRODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setPharmacy(ro.getPharmacy());
		model.setStore(ro.getStore());
		model.setOrderDate(ro.getOrderDate());
		model.setValidUntil(ro.getValidUntil());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		if(ro.getPharmacyToStoreRODetails() != null) {
			for(PharmacyToStoreRODetail d : ro.getPharmacyToStoreRODetails()) {
				PharmacyToStoreRODetailModel modelDetail = new PharmacyToStoreRODetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setMedicine(d.getMedicine());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setReceivedQty(d.getReceivedQty());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPharmacyToStoreRODetails(modelDetails);
		}
		
		if(ro.getCreatedAt() != null) {
			model.setCreated(ro.getCreatedAt().toString()+" | "+userService.getUserById(ro.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(ro.getVerifiedAt() != null) {
			model.setVerified(ro.getVerifiedAt().toString()+" | "+userService.getUserById(ro.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(ro.getApprovedAt() != null) {
			model.setApproved(ro.getApprovedAt().toString()+" | "+userService.getUserById(ro.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}
		
		return model;
	}

	@Override
	public RecordModel requestRequestOrderNo() {
		Long id = 1L;
		try {
			id = pharmacyToStoreRORepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PSR",id.toString()));
		return model;
	}	
}
