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
import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRODetail;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PharmacyToPharmacyRODetailModel;
import com.orbix.api.models.PharmacyToPharmacyROModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRODetailRepository;
import com.orbix.api.repositories.PharmacyToPharmacyRORepository;
import com.orbix.api.repositories.PharmacyRepository;
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
public class PharmacyToPharmacyROServiceImpl implements PharmacyToPharmacyROService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyToPharmacyRORepository pharmacyToPharmacyRORepository;
	private final PharmacyToPharmacyRODetailRepository pharmacyToPharmacyRODetailRepository;
	private final MedicineRepository medicineRepository;
	
	@Override
	public PharmacyToPharmacyROModel save(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro = new PharmacyToPharmacyRO();
		
		if(pharmacyToPharmacyRO.getId() == null) {
			Optional<Pharmacy> requestingPharmacy_ = pharmacyRepository.findById(pharmacyToPharmacyRO.getRequestingPharmacy().getId());
			if(requestingPharmacy_.isEmpty()) {
				throw new NotFoundException("Pharmacy not found");
			}
			
			Optional<Pharmacy> deliveringPharmacy_ = pharmacyRepository.findById(pharmacyToPharmacyRO.getDeliveringPharmacy().getId());
			if(deliveringPharmacy_.isEmpty()) {
				throw new NotFoundException("Delivering Pharmacy not found");
			}
			if(!pharmacyToPharmacyRO.getRequestingPharmacy().getCode().equals(requestingPharmacy_.get().getCode())) {
				throw new InvalidOperationException("Invalid Pharmacy");
			}
			
			if(requestingPharmacy_.get().getId() == deliveringPharmacy_.get().getId()) {
				throw new InvalidOperationException("Order can not be placed in the same pharmacy");
			}
			
			pharmacyToPharmacyRO.setCreatedBy(userService.getUser(request).getId());
			pharmacyToPharmacyRO.setCreatedOn(dayService.getDay().getId());
			pharmacyToPharmacyRO.setCreatedAt(dayService.getTimeStamp());
			
			pharmacyToPharmacyRO.setStatus("PENDING");
			pharmacyToPharmacyRO.setStatusDescription("Order pending for verification");
			
			ro = pharmacyToPharmacyRO;
			ro.setRequestingPharmacy(requestingPharmacy_.get());
			ro.setDeliveringPharmacy(deliveringPharmacy_.get());
		}else {
			Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
			if(pts.isEmpty()) {
				throw new NotFoundException("Order not found");
			}
			if(!pts.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Can not edit. Only pending orders can be edited");
			}
			if(!pharmacyToPharmacyRO.getNo().equals(pts.get().getNo())) {
				throw new InvalidOperationException("Editing order no is not allowed");
			}
			if(pharmacyToPharmacyRO.getRequestingPharmacy().getId() != pts.get().getRequestingPharmacy().getId()) {
				throw new InvalidOperationException("Editing requesting pharmacy is not allowed");
			}
			if(pharmacyToPharmacyRO.getDeliveringPharmacy().getId() != pts.get().getDeliveringPharmacy().getId()) {
				throw new InvalidOperationException("Editing delivering pharmacy is not allowed");
			}
			pts.get().setValidUntil(pharmacyToPharmacyRO.getValidUntil());
			ro = pts.get();
		}
		ro = pharmacyToPharmacyRORepository.save(ro);
		/*PharmacyToPharmacyROModel model = new PharmacyToPharmacyROModel();
		
		List<PharmacyToPharmacyRODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setPharmacy(ro.getPharmacy());
		model.setOrderDate(ro.getOrderDate());
		model.setValidUntil(ro.getValidUntil());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		if(ro.getPharmacyToPharmacyRODetails() != null) {
			for(PharmacyToPharmacyRODetail d : ro.getPharmacyToPharmacyRODetails()) {
				PharmacyToPharmacyRODetailModel modelDetail = new PharmacyToPharmacyRODetailModel();
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
			model.setPharmacyToPharmacyRODetails(modelDetails);
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
	public PharmacyToPharmacyROModel verify(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro;
		
		Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToPharmacyRO.getRequestingPharmacy().getId() != pts.get().getRequestingPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not verify. Only pending orders can be verified");
		}
		if(pts.get().getPharmacyToPharmacyRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not verify. Order has no items");
		}
		
		pts.get().setStatus("VERIFIED");
		pts.get().setStatusDescription("Order awaiting for approval");
		
		pts.get().setVerifiedBy(userService.getUser(request).getId());
		pts.get().setVerifiedOn(dayService.getDay().getId());
		pts.get().setVerifiedAt(dayService.getTimeStamp());
		
		ro = pharmacyToPharmacyRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToPharmacyROModel approve(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro;
		
		Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToPharmacyRO.getRequestingPharmacy().getId() != pts.get().getRequestingPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("VERIFIED")) {
			throw new InvalidOperationException("Could not approve. Only verified orders can be approved");
		}
		if(pts.get().getPharmacyToPharmacyRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not approve. Order has no items");
		}
		
		pts.get().setStatus("APPROVED");
		pts.get().setStatusDescription("Order awaiting for submission");
		
		pts.get().setApprovedBy(userService.getUser(request).getId());
		pts.get().setApprovedOn(dayService.getDay().getId());
		pts.get().setApprovedAt(dayService.getTimeStamp());
		
		ro = pharmacyToPharmacyRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToPharmacyROModel submit(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro;
		
		Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(pharmacyToPharmacyRO.getRequestingPharmacy().getId() != pts.get().getRequestingPharmacy().getId()) {
			throw new InvalidOperationException("Could not process. Order does not belong to this pharmacy");
		}
		if(!pts.get().getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not submit. Only approved orders can be submitted");
		}
		if(pts.get().getPharmacyToPharmacyRODetails().isEmpty()) {
			throw new InvalidOperationException("Could not submit. Order has no items");
		}
		
		pts.get().setStatus("SUBMITTED");
		pts.get().setStatusDescription("Submited to delivering pharmacy. Order awaiting for processing");
		
		ro = pharmacyToPharmacyRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToPharmacyROModel _return(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro;
		
		Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!pts.get().getStatus().equals("SUBMITTED")) {
			throw new InvalidOperationException("Could not returned. Only submitted orders can be returned");
		}
		
		pts.get().setStatus("RETURNED");
		pts.get().setStatusDescription("Order returned for correction");
		
		ro = pharmacyToPharmacyRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public PharmacyToPharmacyROModel reject(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request) {
		
		PharmacyToPharmacyRO ro;
		
		Optional<PharmacyToPharmacyRO> pts = pharmacyToPharmacyRORepository.findById(pharmacyToPharmacyRO.getId());
		if(pts.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!pts.get().getStatus().equals("SUBMITTED")) {
			throw new InvalidOperationException("Could not reject. Only submitted orders can be rejected");
		}
		
		pts.get().setStatus("REJECTED");
		pts.get().setStatusDescription("Order rejected");
		
		ro = pharmacyToPharmacyRORepository.save(pts.get());
		
		return(showOrder(ro));
	}
	
	@Override
	public boolean saveDetail(PharmacyToPharmacyRODetail detail, HttpServletRequest request) {
		
		Optional<PharmacyToPharmacyRO> ro = pharmacyToPharmacyRORepository.findById(detail.getPharmacyToPharmacyRO().getId());
		if(ro.isEmpty()) {
			throw new NotFoundException("Requisition order not found");
		}
		Optional<Medicine> med = medicineRepository.findByName(detail.getMedicine().getName());
		if(med.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		if(detail.getId() == null) {
			List<PharmacyToPharmacyRODetail> det = pharmacyToPharmacyRODetailRepository.findAllByPharmacyToPharmacyROAndMedicine(ro.get(), med.get());
			if(!det.isEmpty()) {
				throw new InvalidOperationException("Duplicates are not allowed");
			}
		}
		
		
		PharmacyToPharmacyRODetail d = new PharmacyToPharmacyRODetail();
		d.setId(detail.getId());
		d.setPharmacyToPharmacyRO(ro.get());
		d.setMedicine(med.get());
		d.setOrderedQty(detail.getOrderedQty());
		d.setReceivedQty(0);
		d.setStatus("PENDING");
		
		d.setCreatedBy(userService.getUser(request).getId());
		d.setCreatedOn(dayService.getDay().getId());
		d.setCreatedAt(dayService.getTimeStamp());
		
		pharmacyToPharmacyRODetailRepository.save(d);
	
		return true;
	}
	
	private PharmacyToPharmacyROModel showOrder(PharmacyToPharmacyRO ro) {
		PharmacyToPharmacyROModel model = new PharmacyToPharmacyROModel();
		List<PharmacyToPharmacyRODetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setRequestingPharmacy(ro.getRequestingPharmacy());
		model.setDeliveringPharmacy(ro.getDeliveringPharmacy());
		model.setOrderDate(ro.getOrderDate());
		model.setValidUntil(ro.getValidUntil());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		if(ro.getPharmacyToPharmacyRODetails() != null) {
			for(PharmacyToPharmacyRODetail d : ro.getPharmacyToPharmacyRODetails()) {
				PharmacyToPharmacyRODetailModel modelDetail = new PharmacyToPharmacyRODetailModel();
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
			model.setPharmacyToPharmacyRODetails(modelDetails);
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
			id = pharmacyToPharmacyRORepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PPR",id.toString()));
		return model;
	}	
}
