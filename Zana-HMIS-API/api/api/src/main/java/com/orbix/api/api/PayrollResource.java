package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Payroll;
import com.orbix.api.domain.PayrollDetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PayrollDetailModel;
import com.orbix.api.models.PayrollModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.PayrollDetailRepository;
import com.orbix.api.repositories.PayrollRepository;
import com.orbix.api.service.PayrollService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class PayrollResource {
	private final PayrollService payrollService;
	private final PayrollRepository payrollRepository;
	private final PayrollDetailRepository payrollDetailRepository;
	private final UserService userService;
	
	@GetMapping("/payrolls")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<List<Payroll>> getVisiblePayrolls(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("VERIFIED");
		statuses.add("APPROVED");
		statuses.add("REJECTED");
		statuses.add("SUBMITTED");
		statuses.add("RETURNED");
		statuses.add("RECEIVED");
		
		List<Payroll> orders = payrollRepository.findAllByStatusIn(statuses);
		
		

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls").toUriString());
		return ResponseEntity.created(uri).body(orders);
	}
	
	
	@PostMapping("/payrolls/save")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL','LOCAL_PURCHASE_ORDER-CREATE','LOCAL_PURCHASE_ORDER-UPDATE')")
	public ResponseEntity<PayrollModel>savePayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/save").toUriString());
		return ResponseEntity.created(uri).body(payrollService.save(payroll, request));
	}
	
	@PostMapping("/payrolls/verify")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel>verifyPayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/verify").toUriString());
		return ResponseEntity.created(uri).body(payrollService.verify(payroll, request));
	}
	
	@PostMapping("/payrolls/approve")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel> approvePayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/approve").toUriString());
		return ResponseEntity.created(uri).body(payrollService.approve(payroll, request));
	}
	
	@PostMapping("/payrolls/submit")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel>submitPayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/submit").toUriString());
		return ResponseEntity.created(uri).body(payrollService.submit(payroll, request));
	}
	
	@PostMapping("/payrolls/return")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel>returnPayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/return").toUriString());
		return ResponseEntity.created(uri).body(payrollService._return(payroll, request));
	}
	
	@PostMapping("/payrolls/reject")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel>rejectPayroll(
			@RequestBody Payroll payroll,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/reject").toUriString());
		return ResponseEntity.created(uri).body(payrollService.reject(payroll, request));
	}
	
	@GetMapping("/payrolls/request_no")
	public RecordModel requestNo(){
		return payrollService.requestPayrollNo();
	}
	
	@GetMapping("/payrolls/get")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel> getPayroll(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Payroll> lpo_ = payrollRepository.findById(id);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		
		
		PayrollModel model = new PayrollModel();
		List<PayrollDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getPayrollDetails() != null) {
			for(PayrollDetail d : lpo_.get().getPayrollDetails()) {
				PayrollDetailModel modelDetail = new PayrollDetailModel();
				modelDetail.setId(d.getId());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPayrollDetailModels(modelDetails);
		}
		
		if(lpo_.get().getCreatedAt() != null) {
			model.setCreated(lpo_.get().getCreatedAt().toString()+" | "+userService.getUserById(lpo_.get().getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(lpo_.get().getVerifiedAt() != null) {
			model.setVerified(lpo_.get().getVerifiedAt().toString()+" | "+userService.getUserById(lpo_.get().getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(lpo_.get().getApprovedAt() != null) {
			model.setApproved(lpo_.get().getApprovedAt().toString()+" | "+userService.getUserById(lpo_.get().getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/get").toUriString());
		return ResponseEntity.created(uri).body(payrollService.get(lpo_.get(), request));
		
	}
	
	@GetMapping("/payrolls/search")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel> searchSupplierOrderAndSupplier(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Payroll> lpo_ = payrollRepository.findById(id);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		
		PayrollModel model = new PayrollModel();
		List<PayrollDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getPayrollDetails() != null) {
			for(PayrollDetail d : lpo_.get().getPayrollDetails()) {
				PayrollDetailModel modelDetail = new PayrollDetailModel();
				modelDetail.setId(d.getId());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPayrollDetailModels(modelDetails);
		}
		
		if(lpo_.get().getCreatedAt() != null) {
			model.setCreated(lpo_.get().getCreatedAt().toString()+" | "+userService.getUserById(lpo_.get().getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(lpo_.get().getVerifiedAt() != null) {
			model.setVerified(lpo_.get().getVerifiedAt().toString()+" | "+userService.getUserById(lpo_.get().getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(lpo_.get().getApprovedAt() != null) {
			model.setApproved(lpo_.get().getApprovedAt().toString()+" | "+userService.getUserById(lpo_.get().getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/supplier_to_store_r_os/search").toUriString());
		return ResponseEntity.created(uri).body(payrollService.get(lpo_.get(), request));
		
	}
	
	@GetMapping("/payrolls/search_by_no")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<PayrollModel> searchSupplierOrderByNoAndSupplier(
			@RequestParam(name = "no") String no,
			HttpServletRequest request){
		Optional<Payroll> lpo_ = payrollRepository.findByNo(no);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		
		PayrollModel model = new PayrollModel();
		List<PayrollDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getPayrollDetails() != null) {
			for(PayrollDetail d : lpo_.get().getPayrollDetails()) {
				PayrollDetailModel modelDetail = new PayrollDetailModel();
				modelDetail.setId(d.getId());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPayrollDetailModels(modelDetails);
		}
		
		if(lpo_.get().getCreatedAt() != null) {
			model.setCreated(lpo_.get().getCreatedAt().toString()+" | "+userService.getUserById(lpo_.get().getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(lpo_.get().getVerifiedAt() != null) {
			model.setVerified(lpo_.get().getVerifiedAt().toString()+" | "+userService.getUserById(lpo_.get().getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(lpo_.get().getApprovedAt() != null) {
			model.setApproved(lpo_.get().getApprovedAt().toString()+" | "+userService.getUserById(lpo_.get().getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}			
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/supplier_to_store_r_os/search_by_no").toUriString());
		return ResponseEntity.created(uri).body(model);
		
	}
	
	@PostMapping("/payrolls/save_detail")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<Boolean>savePayrollDetail(
			@RequestBody PayrollDetail detail,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/save_detail").toUriString());
		return ResponseEntity.created(uri).body(payrollService.saveDetail(detail, request));
	}
	
	@PostMapping("/payrolls/delete_detail")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public boolean deletePayrollDetail(
			@RequestBody PayrollDetail detail,
			HttpServletRequest request){
		
		Optional<PayrollDetail> detail_ = payrollDetailRepository.findById(detail.getId());
		if(detail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(detail_.get().getPayroll().getId() != detail.getPayroll().getId()) {
			throw new InvalidOperationException("Could not delete, order do not match");
		}
		if(!detail_.get().getPayroll().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Only pending orders can be  modified");
		}

		URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/payrolls/delete_detail").toUriString());
		payrollDetailRepository.delete(detail_.get());
		return true;
	}	
}
