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
import com.orbix.api.domain.Item;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.SupplierItemPrice;
import com.orbix.api.domain.Item;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.domain.Store;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.LocalPurchaseOrderDetailModel;
import com.orbix.api.models.LocalPurchaseOrderModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.repositories.LocalPurchaseOrderDetailRepository;
import com.orbix.api.repositories.LocalPurchaseOrderRepository;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.repositories.SupplierItemPriceRepository;
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
public class LocalPurchaseOrderServiceImpl implements LocalPurchaseOrderService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final SupplierRepository supplierRepository;
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	private final LocalPurchaseOrderDetailRepository localPurchaseOrderDetailRepository;
	private final ItemRepository itemRepository;
	private final SupplierItemPriceRepository supplierItemPriceRepository;
	private final StoreRepository storeRepository;
	
	@Override
	public LocalPurchaseOrderModel save(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		if(localPurchaseOrder.getId() == null) {
			Optional<Supplier> supplier_ = supplierRepository.findById(localPurchaseOrder.getSupplier().getId());
			if(supplier_.isEmpty()) {
				throw new NotFoundException("Supplier not found");
			}
			
			Optional<Store> store_ = storeRepository.findById(localPurchaseOrder.getStore().getId());
			if(store_.isEmpty()) {
				throw new NotFoundException("Store not found");
			}
			
			
			localPurchaseOrder.setSupplier(supplier_.get());
			localPurchaseOrder.setStore(store_.get());
			
			localPurchaseOrder.setCreatedBy(userService.getUser(request).getId());
			localPurchaseOrder.setCreatedOn(dayService.getDay().getId());
			localPurchaseOrder.setCreatedAt(dayService.getTimeStamp());
			
			localPurchaseOrder.setStatus("PENDING");
			localPurchaseOrder.setStatusDescription("Order pending for verification");
			lpo = localPurchaseOrder;
		}else {
			Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
			if(lpo_.isEmpty()) {
				throw new NotFoundException("Order not found");
			}
			if(!lpo_.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Can not edit. Only pending orders can be edited");
			}
			if(!localPurchaseOrder.getNo().equals(lpo_.get().getNo())) {
				throw new InvalidOperationException("Editing order no is not allowed");
			}
			if(localPurchaseOrder.getSupplier().getId() != lpo_.get().getSupplier().getId()) {
				throw new InvalidOperationException("Editing supplier is not allowed");
			}
			lpo_.get().setValidUntil(localPurchaseOrder.getValidUntil());
			lpo = lpo_.get();
		}
		lpo = localPurchaseOrderRepository.save(lpo);		
		return(showOrder(lpo));
	}
	
	@Override
	public LocalPurchaseOrderModel verify(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(!lpo_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not verify. Only pending orders can be verified");
		}
		if(lpo_.get().getLocalPurchaseOrderDetails().isEmpty()) {
			throw new InvalidOperationException("Could not verify. Order has no items");
		}
		
		lpo_.get().setStatus("VERIFIED");
		lpo_.get().setStatusDescription("Order awaiting for approval");
		
		lpo_.get().setVerifiedBy(userService.getUser(request).getId());
		lpo_.get().setVerifiedOn(dayService.getDay().getId());
		lpo_.get().setVerifiedAt(dayService.getTimeStamp());
		
		lpo = localPurchaseOrderRepository.save(lpo_.get());
		
		return(showOrder(lpo));
	}
	
	@Override
	public LocalPurchaseOrderModel approve(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(!lpo_.get().getStatus().equals("VERIFIED")) {
			throw new InvalidOperationException("Could not approve. Only verified orders can be approved");
		}
		if(lpo_.get().getLocalPurchaseOrderDetails().isEmpty()) {
			throw new InvalidOperationException("Could not approve. Order has no items");
		}
		
		lpo_.get().setStatus("APPROVED");
		lpo_.get().setStatusDescription("Order awaiting for submission");
		
		lpo_.get().setApprovedBy(userService.getUser(request).getId());
		lpo_.get().setApprovedOn(dayService.getDay().getId());
		lpo_.get().setApprovedAt(dayService.getTimeStamp());
		
		lpo = localPurchaseOrderRepository.save(lpo_.get());
		
		return(showOrder(lpo));
	}
	
	@Override
	public LocalPurchaseOrderModel submit(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		if(!lpo_.get().getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not submit. Only approved orders can be submitted");
		}
		if(lpo_.get().getLocalPurchaseOrderDetails().isEmpty()) {
			throw new InvalidOperationException("Could not submit. Order has no items");
		}
		
		lpo_.get().setStatus("SUBMITTED");
		lpo_.get().setStatusDescription("Submited to supplier. Order awaiting for delivery");
		
		lpo = localPurchaseOrderRepository.save(lpo_.get());
		
		return(showOrder(lpo));
	}
	
	@Override
	public LocalPurchaseOrderModel _return(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!(lpo_.get().getStatus().equals("PENDING") || lpo_.get().getStatus().equals("VERIFIED"))) {
			throw new InvalidOperationException("Could not returned. Only PENDING or VERIFIED orders can be returned");
		}
		
		lpo_.get().setStatus("RETURNED");
		lpo_.get().setStatusDescription("Order returned for ammendment");
		
		lpo = localPurchaseOrderRepository.save(lpo_.get());
		
		return(showOrder(lpo));
	}
	
	@Override
	public LocalPurchaseOrderModel reject(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		
		LocalPurchaseOrder lpo;
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(localPurchaseOrder.getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}		
		if(!(lpo_.get().getStatus().equals("PENDING") || lpo_.get().getStatus().equals("VERIFIED"))) {
			throw new InvalidOperationException("Could not reject. Only PENDING or VERIFIED orders can be rejected");
		}
		
		lpo_.get().setStatus("REJECTED");
		lpo_.get().setStatusDescription("Order rejected");
		
		lpo = localPurchaseOrderRepository.save(lpo_.get());
		
		return(showOrder(lpo));
	}
	
	@Override
	public boolean saveDetail(LocalPurchaseOrderDetail lpoDetail, HttpServletRequest request) {
		
		Optional<LocalPurchaseOrder> lpo_ = localPurchaseOrderRepository.findById(lpoDetail.getLocalPurchaseOrder().getId());
		if(lpo_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		
		if(!lpo_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not edit. only PENDING LPO can be edited");
		}
		
		Optional<Item> item_ = itemRepository.findById(lpoDetail.getItem().getId());
		if(item_.isEmpty()) {
			throw new NotFoundException("Item not found");
		}
		
		//check if supplier supplier the given item
		Optional<SupplierItemPrice> supplierItemPrice_ = supplierItemPriceRepository.findBySupplierAndItem(lpo_.get().getSupplier(), item_.get());
		if(supplierItemPrice_.isEmpty()) {
			throw new InvalidOperationException("Item not valid for this supplier");
		}
		
		if(lpoDetail.getId() == null) {
			List<LocalPurchaseOrderDetail> detail_ = localPurchaseOrderDetailRepository.findAllByLocalPurchaseOrderAndItem(lpo_.get(), item_.get());
			if(!detail_.isEmpty()) {
				throw new InvalidOperationException("Duplicates items are not allowed");
			}
		}
		
		
		LocalPurchaseOrderDetail detail = new LocalPurchaseOrderDetail();
		detail.setId(lpoDetail.getId());
		detail.setLocalPurchaseOrder(lpo_.get());
		detail.setItem(item_.get());
		detail.setQty(lpoDetail.getQty());
		detail.setPrice(supplierItemPrice_.get().getPrice());
		
		
		detail.setCreatedBy(userService.getUser(request).getId());
		detail.setCreatedOn(dayService.getDay().getId());
		detail.setCreatedAt(dayService.getTimeStamp());
		
		localPurchaseOrderDetailRepository.save(detail);
	
		return true;
	}
	
	private LocalPurchaseOrderModel showOrder(LocalPurchaseOrder ro) {
		LocalPurchaseOrderModel model = new LocalPurchaseOrderModel();
		List<LocalPurchaseOrderDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setSupplier(ro.getSupplier());
		model.setOrderDate(ro.getOrderDate());
		model.setValidUntil(ro.getValidUntil());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		if(ro.getLocalPurchaseOrderDetails() != null) {
			for(LocalPurchaseOrderDetail d : ro.getLocalPurchaseOrderDetails()) {
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
			id = localPurchaseOrderRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("LPO",id.toString()));
		return model;
	}
}
