/**
 * 
 */
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

import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.LocalPurchaseOrderDetailModel;
import com.orbix.api.models.LocalPurchaseOrderModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.ItemMedicineCoefficientRepository;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.repositories.LocalPurchaseOrderDetailRepository;
import com.orbix.api.repositories.LocalPurchaseOrderRepository;
import com.orbix.api.service.LocalPurchaseOrderService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author Godfrey
 *
 */
@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class LocalPurchaseOrderResource {

	private final LocalPurchaseOrderService localPurchaseOrderService;
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	private final LocalPurchaseOrderDetailRepository localPurchaseOrderDetailRepository;
	private final UserService userService;
	
	@GetMapping("/local_purchase_orders")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<List<LocalPurchaseOrder>> getVisibleLocalPurchaseOrders(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("VERIFIED");
		statuses.add("APPROVED");
		statuses.add("REJECTED");
		statuses.add("SUBMITTED");
		statuses.add("RETURNED");
		statuses.add("RECEIVED");
		
		List<LocalPurchaseOrder> orders = localPurchaseOrderRepository.findAllByStatusIn(statuses);
		
		

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders").toUriString());
		return ResponseEntity.created(uri).body(orders);
	}
	
	
	@PostMapping("/local_purchase_orders/save")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL','LOCAL_PURCHASE_ORDER-CREATE','LOCAL_PURCHASE_ORDER-UPDATE')")
	public ResponseEntity<LocalPurchaseOrderModel>saveLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/save").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.save(localPurchaseOrder, request));
	}
	
	@PostMapping("/local_purchase_orders/verify")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel>verifyLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/verify").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.verify(localPurchaseOrder, request));
	}
	
	@PostMapping("/local_purchase_orders/approve")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel> approveLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/approve").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.approve(localPurchaseOrder, request));
	}
	
	@PostMapping("/local_purchase_orders/submit")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel>submitLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/submit").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.submit(localPurchaseOrder, request));
	}
	
	@PostMapping("/local_purchase_orders/return")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel>returnLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/return").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService._return(localPurchaseOrder, request));
	}
	
	@PostMapping("/local_purchase_orders/reject")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel>rejectLocalPurchaseOrder(
			@RequestBody LocalPurchaseOrder localPurchaseOrder,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/reject").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.reject(localPurchaseOrder, request));
	}
	
	/*@GetMapping("/supplier_to_store_r_os/load_supplier_orders_by_supplier")
	public List<LocalPurchaseOrder> loadSupplierOrdersBySupplier(
			@RequestParam(name = "supplier_id") Long supplierId){
		Optional<Supplier> pharm = supplierRepository.findById(supplierId);
		if(pharm.isEmpty()) {
			throw new NotFoundException("Supplier not found");
		}
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("VERIFIED");
		statuses.add("APPROVED");
		statuses.add("SUBMITTED");
		statuses.add("IN-PROCESS");
		statuses.add("GOODS-ISSUED");
		statuses.add("COMPLETED");
		statuses.add("RETURNED");
		statuses.add("REJECTED");
		return localPurchaseOrderRepository.findBySupplierAndStatusIn(pharm.get(), statuses);
	}*/
	
	/*@GetMapping("/supplier_to_store_r_os/load_supplier_orders")
	public List<LocalPurchaseOrder> loadSupplierOrders(){

		List<String> statuses = new ArrayList<>();
		statuses.add("SUBMITTED");
		statuses.add("IN-PROCESS");
		statuses.add("GOODS-ISSUED");
		
		return localPurchaseOrderRepository.findByStatusIn(statuses);
	}*/
	
	@GetMapping("/local_purchase_orders/request_no")
	public RecordModel requestNo(){
		return localPurchaseOrderService.requestRequestOrderNo();
	}
	
	@GetMapping("/local_purchase_orders/get")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel> getLocalPurchaseOrder(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(id);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		
		LocalPurchaseOrderModel model = new LocalPurchaseOrderModel();
		List<LocalPurchaseOrderDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setSupplier(lpo_.get().getSupplier());
		model.setStore(lpo_.get().getStore());
		model.setOrderDate(lpo_.get().getOrderDate());
		model.setValidUntil(lpo_.get().getValidUntil());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getLocalPurchaseOrderDetails() != null) {
			for(LocalPurchaseOrderDetail d : lpo_.get().getLocalPurchaseOrderDetails()) {
				LocalPurchaseOrderDetailModel modelDetail = new LocalPurchaseOrderDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setItem(d.getItem());
				modelDetail.setQty(d.getQty());
				modelDetail.setPrice(d.getPrice());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setLocalPurchaseOrderDetails(modelDetails);
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
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/get").toUriString());
		return ResponseEntity.created(uri).body(model);
		
	}
	
	@GetMapping("/local_purchase_orders/search")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel> searchSupplierOrderAndSupplier(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(id);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		
		LocalPurchaseOrderModel model = new LocalPurchaseOrderModel();
		List<LocalPurchaseOrderDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setSupplier(lpo_.get().getSupplier());
		model.setStore(lpo_.get().getStore());
		model.setOrderDate(lpo_.get().getOrderDate());
		model.setValidUntil(lpo_.get().getValidUntil());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getLocalPurchaseOrderDetails() != null) {
			for(LocalPurchaseOrderDetail d : lpo_.get().getLocalPurchaseOrderDetails()) {
				LocalPurchaseOrderDetailModel modelDetail = new LocalPurchaseOrderDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setItem(d.getItem());
				modelDetail.setQty(d.getQty());
				modelDetail.setPrice(d.getPrice());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setLocalPurchaseOrderDetails(modelDetails);
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
		return ResponseEntity.created(uri).body(model);
		
	}
	
	@GetMapping("/local_purchase_orders/search_by_no")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<LocalPurchaseOrderModel> searchSupplierOrderByNoAndSupplier(
			@RequestParam(name = "no") String no,
			HttpServletRequest request){
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findByNo(no);
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		
		LocalPurchaseOrderModel model = new LocalPurchaseOrderModel();
		List<LocalPurchaseOrderDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(lpo_.get().getId());
		model.setNo(lpo_.get().getNo());
		model.setSupplier(lpo_.get().getSupplier());
		model.setOrderDate(lpo_.get().getOrderDate());
		model.setValidUntil(lpo_.get().getValidUntil());
		model.setStatus(lpo_.get().getStatus());
		model.setStatusDescription(lpo_.get().getStatusDescription());
		if(lpo_.get().getLocalPurchaseOrderDetails() != null) {
			for(LocalPurchaseOrderDetail d : lpo_.get().getLocalPurchaseOrderDetails()) {
				LocalPurchaseOrderDetailModel modelDetail = new LocalPurchaseOrderDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setItem(d.getItem());
				modelDetail.setQty(d.getQty());
				modelDetail.setPrice(d.getPrice());
				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setLocalPurchaseOrderDetails(modelDetails);
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
	
	@PostMapping("/local_purchase_orders/save_detail")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<Boolean>saveLocalPurchaseOrderDetail(
			@RequestBody LocalPurchaseOrderDetail detail,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/save_detail").toUriString());
		return ResponseEntity.created(uri).body(localPurchaseOrderService.saveDetail(detail, request));
	}
	
	@PostMapping("/local_purchase_orders/delete_detail")
	@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public boolean deleteLocalPurchaseOrderDetail(
			@RequestBody LocalPurchaseOrderDetail detail,
			HttpServletRequest request){
		
		Optional<LocalPurchaseOrderDetail> detail_ = localPurchaseOrderDetailRepository.findById(detail.getId());
		if(detail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(detail_.get().getLocalPurchaseOrder().getId() != detail.getLocalPurchaseOrder().getId()) {
			throw new InvalidOperationException("Could not delete, order do not match");
		}
		if(!detail_.get().getLocalPurchaseOrder().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Only pending orders can be  modified");
		}

		URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/local_purchase_orders/delete_detail").toUriString());
		localPurchaseOrderDetailRepository.delete(detail_.get());
		return true;
	}	
}
