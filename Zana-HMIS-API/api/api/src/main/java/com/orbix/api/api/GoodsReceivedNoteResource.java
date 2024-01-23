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

import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.GoodsReceivedNoteDetail;
import com.orbix.api.domain.GoodsReceivedNoteDetailBatch;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.domain.Store;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.GoodsReceivedNoteDetailModel;
import com.orbix.api.models.GoodsReceivedNoteModel;
import com.orbix.api.models.LocalPurchaseOrderDetailModel;
import com.orbix.api.models.LocalPurchaseOrderModel;
import com.orbix.api.repositories.GoodsReceivedNoteDetailBatchRepository;
import com.orbix.api.repositories.GoodsReceivedNoteDetailRepository;
import com.orbix.api.repositories.GoodsReceivedNoteRepository;
import com.orbix.api.repositories.LocalPurchaseOrderDetailRepository;
import com.orbix.api.repositories.LocalPurchaseOrderRepository;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.service.GoodsReceivedNoteService;
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
public class GoodsReceivedNoteResource {
	
	private final GoodsReceivedNoteService goodsReceivedNoteService;
	private final LocalPurchaseOrderService localPurchaseOrderService;
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	private final LocalPurchaseOrderDetailRepository localPurchaseOrderDetailRepository;
	private final UserService userService;
	private final GoodsReceivedNoteRepository goodsReceivedNoteRepository;
	private final GoodsReceivedNoteDetailRepository goodsReceivedNoteDetailRepository;
	private final GoodsReceivedNoteDetailBatchRepository goodsReceivedNoteDetailBatchRepository;
	private final StoreRepository storeRepository;
	
	@GetMapping("/goods_received_notes")
	//@PreAuthorize("hasAnyAuthority('GOO-ALL')")
	public ResponseEntity<List<GoodsReceivedNote>> getVisibleGoodsReceivedNotes(
			@RequestParam(name = "store_id") Long storeId,
			HttpServletRequest request){
		
		Optional<Store> store_ = storeRepository.findById(storeId);
		if(store_.isEmpty()) {
			throw new NotFoundException("Store not found");
		}
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("VERIFIED");
		statuses.add("APPROVED");
		statuses.add("REJECTED");
		statuses.add("SUBMITTED");
		statuses.add("RETURNED");
		
		List<GoodsReceivedNote> grns = goodsReceivedNoteRepository.findAllByStoreAndStatusIn(store_.get(), statuses);
		
		

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes").toUriString());
		return ResponseEntity.created(uri).body(grns);
	}
	
	@PostMapping("/goods_received_notes/create")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-CREATE')")
	public ResponseEntity<GoodsReceivedNote>saveGoodsReceivedNote(
			@RequestBody LocalPurchaseOrderModel localPurchaseOrder,
			HttpServletRequest request){
		
		Optional<LocalPurchaseOrder> localPurchaseOrder_ = localPurchaseOrderRepository.findByNo(localPurchaseOrder.getNo());
		if(localPurchaseOrder_.isEmpty()) {
			throw new NotFoundException("Local Purchase Order not found");
		}
		
		if(localPurchaseOrder.getStore().getId() != localPurchaseOrder_.get().getStore().getId()) {
			throw new InvalidOperationException("Order not designated to the selected store");
		}
		
		if(!localPurchaseOrder_.get().getStatus().equals("SUBMITTED")) {
			throw new InvalidOperationException("Could not create GRN. Local Purchase Order not submitted");
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/create").toUriString());
		return ResponseEntity.created(uri).body(goodsReceivedNoteService.create(localPurchaseOrder_.get(), request));
	}
	
	@GetMapping("/goods_received_notes/search")
	//@PreAuthorize("hasAnyAuthority('LOCAL_PURCHASE_ORDER-ALL')")
	public ResponseEntity<GoodsReceivedNoteModel> searchSupplierOrderAndSupplier(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<GoodsReceivedNote> grn_ = goodsReceivedNoteRepository.findById(id);
		if(grn_.isEmpty()) {
			throw new NotFoundException("GRN not found");
		}		
		
		GoodsReceivedNoteModel model = new GoodsReceivedNoteModel();
		List<GoodsReceivedNoteDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(grn_.get().getId());
		model.setNo(grn_.get().getNo());
		model.setStatus(grn_.get().getStatus());
		model.setStatusDescription(grn_.get().getStatusDescription());
		model.setLocalPurchaseOrder(grn_.get().getLocalPurchaseOrder());
		if(grn_.get().getGoodsReceivedNoteDetails() != null) {
			for(GoodsReceivedNoteDetail d : grn_.get().getGoodsReceivedNoteDetails()) {
				GoodsReceivedNoteDetailModel modelDetail = new GoodsReceivedNoteDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setItem(d.getItem());
				modelDetail.setOrderedQty(d.getOrderedQty());
				modelDetail.setReceivedQty(d.getReceivedQty());
				modelDetail.setPrice(d.getPrice());
				modelDetail.setStatus(d.getStatus());
				
				modelDetail.setGoodsReceivedNoteDetailBatches(d.getGoodsReceivedNoteDetailBatches());
				
				modelDetails.add(modelDetail);
			}
			model.setGoodsReceivedNoteDetails(modelDetails);
		}
		
		if(grn_.get().getCreatedAt() != null) {
			model.setCreated(grn_.get().getCreatedAt().toString()+" | "+userService.getUserById(grn_.get().getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(grn_.get().getVerifiedAt() != null) {
			model.setVerified(grn_.get().getVerifiedAt().toString()+" | "+userService.getUserById(grn_.get().getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(grn_.get().getApprovedAt() != null) {
			model.setApproved(grn_.get().getApprovedAt().toString()+" | "+userService.getUserById(grn_.get().getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/search").toUriString());
		return ResponseEntity.created(uri).body(model);
		
	}
	
	@PostMapping("/goods_received_notes/save_detail_qty")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-UPDATE')")
	public ResponseEntity<GoodsReceivedNoteDetail>saveGoodsReceivedNoteDetailQty(
			@RequestBody GoodsReceivedNoteDetailModel goodsReceivedNoteDetail,
			HttpServletRequest request){
		
		Optional<GoodsReceivedNote> goodsReceivedNote_ = goodsReceivedNoteRepository.findById(goodsReceivedNoteDetail.getGoodsReceivedNote().getId());
		if(goodsReceivedNote_.isEmpty()) {
			throw new NotFoundException("Goods Received Note not found");
		}		
		if(!goodsReceivedNote_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not update GRN. Only Pending GRN can be updated");
		}
		Optional<GoodsReceivedNoteDetail> goodsReceivedNoteDetail_ = goodsReceivedNoteDetailRepository.findById(goodsReceivedNoteDetail.getId());
		if(goodsReceivedNoteDetail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(goodsReceivedNoteDetail_.get().getStatus() != null) {
			if(goodsReceivedNoteDetail_.get().getStatus().equals("VERIFIED")) {
				throw new InvalidOperationException("Can not change. Already verified");
			}
		}
		
		if(goodsReceivedNoteDetail_.get().getGoodsReceivedNote().getId() != goodsReceivedNote_.get().getId()) {
			throw new InvalidOperationException("Invalid detail selected");
		}
		if(goodsReceivedNoteDetail.getOrderedQty() != goodsReceivedNoteDetail_.get().getOrderedQty()) {
			throw new InvalidOperationException("Ordered qty does not match with the available qty");
		}
		if(goodsReceivedNoteDetail.getReceivedQty() < 0) {
			throw new InvalidOperationException("Received Qty must not be less than zero");
		}
		if(goodsReceivedNoteDetail.getReceivedQty() > goodsReceivedNoteDetail.getOrderedQty()) {
			throw new InvalidOperationException("Received Qty must not exceed ordered qty");
		}
		
		goodsReceivedNoteDetail_.get().setReceivedQty(goodsReceivedNoteDetail.getReceivedQty());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/save_detail_qty").toUriString());
		return ResponseEntity.created(uri).body(goodsReceivedNoteDetailRepository.save(goodsReceivedNoteDetail_.get()));
	}
	
	@PostMapping("/goods_received_notes/verify_detail_qty")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-UPDATE')")
	public ResponseEntity<GoodsReceivedNoteDetail>verifyGoodsReceivedNoteDetailQty(
			@RequestBody GoodsReceivedNoteDetailModel goodsReceivedNoteDetail,
			HttpServletRequest request){
		
		Optional<GoodsReceivedNote> goodsReceivedNote_ = goodsReceivedNoteRepository.findById(goodsReceivedNoteDetail.getGoodsReceivedNote().getId());
		if(goodsReceivedNote_.isEmpty()) {
			throw new NotFoundException("Goods Received Note not found");
		}		
		if(!goodsReceivedNote_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not update GRN. Only Pending GRN can be updated");
		}
		Optional<GoodsReceivedNoteDetail> goodsReceivedNoteDetail_ = goodsReceivedNoteDetailRepository.findById(goodsReceivedNoteDetail.getId());
		if(goodsReceivedNoteDetail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(goodsReceivedNoteDetail_.get().getStatus() != null) {
			if(goodsReceivedNoteDetail_.get().getStatus().equals("VERIFIED")) {
				throw new InvalidOperationException("Can not verify. Already verified");
			}
		}
		
		if(goodsReceivedNoteDetail_.get().getGoodsReceivedNote().getId() != goodsReceivedNote_.get().getId()) {
			throw new InvalidOperationException("Invalid detail selected");
		}
		if(goodsReceivedNoteDetail.getOrderedQty() != goodsReceivedNoteDetail_.get().getOrderedQty()) {
			throw new InvalidOperationException("Ordered qty does not match with the available qty");
		}
		if(goodsReceivedNoteDetail.getReceivedQty() < 0) {
			throw new InvalidOperationException("Received Qty must not be less than zero");
		}
		if(goodsReceivedNoteDetail.getReceivedQty() > goodsReceivedNoteDetail.getOrderedQty()) {
			throw new InvalidOperationException("Received Qty must not exceed ordered qty");
		}
		if(goodsReceivedNoteDetail.getReceivedQty() != goodsReceivedNoteDetail_.get().getReceivedQty()) {
			throw new InvalidOperationException("Qty Mismatch in received qty! Please save Qty before verifying");
		}
		
		
		double batchQty = 0;
		for(GoodsReceivedNoteDetailBatch batch : goodsReceivedNoteDetail_.get().getGoodsReceivedNoteDetailBatches()) {
			batchQty = batchQty + batch.getQty();
		}
		if(batchQty != goodsReceivedNoteDetail_.get().getReceivedQty()) {
			throw new InvalidOperationException("Can not verify. Batch quantities are not equal to total received quantities");
		}
		
		
		goodsReceivedNoteDetail_.get().setStatus("VERIFIED");
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/save_detail_qty").toUriString());
		return ResponseEntity.created(uri).body(goodsReceivedNoteDetailRepository.save(goodsReceivedNoteDetail_.get()));
	}
	
	@PostMapping("/goods_received_notes/approve")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-APPROVE')")
	public ResponseEntity<GoodsReceivedNote>approveGoodsReceivedNote(
			@RequestBody GoodsReceivedNoteModel goodsReceivedNote,
			HttpServletRequest request){
		
		Optional<GoodsReceivedNote> goodsReceivedNote_ = goodsReceivedNoteRepository.findById(goodsReceivedNote.getId());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/approve").toUriString());
		return ResponseEntity.created(uri).body(goodsReceivedNoteService.approve(goodsReceivedNote_.get(), request));
	}
	
	@PostMapping("/goods_received_notes/add_batch")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-UPDATE')")
	public void goodsReceivedAddBatch(
			@RequestBody GoodsReceivedNoteDetailBatch goodsReceivedNoteDetailBatch,
			HttpServletRequest request){
		
		Optional<GoodsReceivedNoteDetail> goodsReceivedNoteDetail_ = goodsReceivedNoteDetailRepository.findById(goodsReceivedNoteDetailBatch.getGoodsReceivedNoteDetail().getId());
		if(goodsReceivedNoteDetail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(goodsReceivedNoteDetailBatch.getQty() <= 0) {
			throw new InvalidOperationException("Invalid qty value. Qty must be more than zero");
		}
		GoodsReceivedNoteDetailBatch batch = new GoodsReceivedNoteDetailBatch();
		batch.setNo(goodsReceivedNoteDetailBatch.getNo());
		batch.setManufacturedDate(goodsReceivedNoteDetailBatch.getManufacturedDate());
		batch.setExpiryDate(goodsReceivedNoteDetailBatch.getExpiryDate());
		batch.setQty(goodsReceivedNoteDetailBatch.getQty());
		batch.setGoodsReceivedNoteDetail(goodsReceivedNoteDetail_.get());
		
		goodsReceivedNoteDetailBatchRepository.save(batch);
		
		//URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/add_batch").toUriString());
		//return ResponseEntity.created(uri).body(goodsReceivedNoteService.approve(goodsReceivedNote_.get(), request));
	}
	
	@PostMapping("/goods_received_notes/delete_batch")
	@PreAuthorize("hasAnyAuthority('GOODS_RECEIVED_NOTE-ALL','GOODS_RECEIVED_NOTE-UPDATE')")
	public void goodsReceivedDelateBatch(
			@RequestBody GoodsReceivedNoteDetailBatch goodsReceivedNoteDetailBatch,
			HttpServletRequest request){
		
		Optional<GoodsReceivedNoteDetailBatch> batch_ = goodsReceivedNoteDetailBatchRepository.findById(goodsReceivedNoteDetailBatch.getId());
		if(batch_.isEmpty()) {
			throw new NotFoundException("Batch not found");
		}
		
		goodsReceivedNoteDetailBatchRepository.delete(batch_.get());
		
		//URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/goods_received_notes/add_batch").toUriString());
		//return ResponseEntity.created(uri).body(goodsReceivedNoteService.approve(goodsReceivedNote_.get(), request));
	}
}
