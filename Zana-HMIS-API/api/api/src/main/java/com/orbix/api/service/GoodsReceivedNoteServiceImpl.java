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
import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.GoodsReceivedNoteDetail;
import com.orbix.api.domain.GoodsReceivedNoteDetailBatch;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.domain.Purchase;
import com.orbix.api.domain.StoreItem;
import com.orbix.api.domain.StoreItemBatch;
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.GoodsReceivedNoteDetailRepository;
import com.orbix.api.repositories.GoodsReceivedNoteRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.LocalPurchaseOrderRepository;
import com.orbix.api.repositories.PurchaseRepository;
import com.orbix.api.repositories.StoreItemBatchRepository;
import com.orbix.api.repositories.StoreItemRepository;
import com.orbix.api.repositories.StoreStockCardRepository;
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
public class GoodsReceivedNoteServiceImpl implements GoodsReceivedNoteService {
	
	private final GoodsReceivedNoteRepository goodsReceivedNoteRepository;
	private final GoodsReceivedNoteDetailRepository goodsReceivedNoteDetailRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	
	private final StoreItemRepository storeItemRepository;
	private final StoreStockCardRepository storeStockCardRepository;
	private final StoreItemBatchRepository storeItemBatchRepository;
	private final PurchaseRepository purchaseRepository;
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	
	@Override
	public GoodsReceivedNote create(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request) {
		Optional<GoodsReceivedNote> goodsReceivedNote_ = goodsReceivedNoteRepository.findByLocalPurchaseOrder(localPurchaseOrder);
		if(goodsReceivedNote_.isPresent()) {
			throw new InvalidOperationException("Can not create GRN by the given LPO. GRN with the given LPO already exist");
		}
		if(goodsReceivedNoteRepository.existsByLocalPurchaseOrder(localPurchaseOrder)) {
			throw new InvalidOperationException("Can not create GRN by the given LPO. GRN with the given LPO already exist");
		}
		GoodsReceivedNote goodsReceivedNote = new GoodsReceivedNote();
		goodsReceivedNote.setNo("NA");
		goodsReceivedNote.setLocalPurchaseOrder(localPurchaseOrder);
		goodsReceivedNote.setStore(localPurchaseOrder.getStore());
		goodsReceivedNote.setStatus("PENDING");
		goodsReceivedNote.setStatusDescription("GRN Pending for verification");
		
		goodsReceivedNote.setCreatedBy(userService.getUser(request).getId());
		goodsReceivedNote.setCreatedOn(dayService.getDay().getId());
		goodsReceivedNote.setCreatedAt(dayService.getTimeStamp());
		
		goodsReceivedNote = goodsReceivedNoteRepository.save(goodsReceivedNote);
		
		goodsReceivedNote.setNo(this.requestRequestGrnNo().getNo());
		
		goodsReceivedNote = goodsReceivedNoteRepository.save(goodsReceivedNote);
		
		List<LocalPurchaseOrderDetail> localPurchaseOrderDetails =  localPurchaseOrder.getLocalPurchaseOrderDetails();
		List<GoodsReceivedNoteDetail> goodsReceivedNoteDetails = new ArrayList<>();
		for(LocalPurchaseOrderDetail lpoDetail : localPurchaseOrderDetails) {
			GoodsReceivedNoteDetail grnDetail = new GoodsReceivedNoteDetail();
			grnDetail.setItem(lpoDetail.getItem());
			grnDetail.setOrderedQty(lpoDetail.getQty());
			grnDetail.setReceivedQty(0);
			grnDetail.setPrice(lpoDetail.getPrice());
			grnDetail.setStatus("NOT VERIFIED");
			grnDetail.setGoodsReceivedNote(goodsReceivedNote);
	
			grnDetail = goodsReceivedNoteDetailRepository.save(grnDetail);
			
			goodsReceivedNoteDetails.add(grnDetail);
		}
		goodsReceivedNote.setGoodsReceivedNoteDetails(goodsReceivedNoteDetails);
		
		return goodsReceivedNoteRepository.save(goodsReceivedNote);
	}
	
	@Override
	public GoodsReceivedNote approve(GoodsReceivedNote goodsReceivedNote, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		/**
		 * validate grn, update stocks, update stock card, record purchases
		 */
		if(!goodsReceivedNote.getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Can not approve. Not a pending GRN");
		}
		for(GoodsReceivedNoteDetail detail : goodsReceivedNote.getGoodsReceivedNoteDetails()) {
			if(!detail.getStatus().equals("VERIFIED")) {
				throw new InvalidOperationException("All the items in the GRN must be verified before approving.");
			}
		}
		
		for(GoodsReceivedNoteDetail detail : goodsReceivedNote.getGoodsReceivedNoteDetails()) {
			Optional<StoreItem> storeItem_ = storeItemRepository.findByStoreAndItem(goodsReceivedNote.getStore(), detail.getItem());
			double originalStock = storeItem_.get().getStock();
			double newStock = originalStock + detail.getReceivedQty();
			//Update stock
			storeItem_.get().setStock(newStock);
			storeItemRepository.save(storeItem_.get());
			
			//Create item store stock card
			if(detail.getReceivedQty() > 0) {
				StoreStockCard storeStockCard = new StoreStockCard();
				storeStockCard.setItem(detail.getItem());
				storeStockCard.setStore(goodsReceivedNote.getStore());
				storeStockCard.setQtyIn(detail.getReceivedQty());
				storeStockCard.setQtyOut(0);
				storeStockCard.setBalance(newStock);
				storeStockCard.setReference("Goods received GRN# " + goodsReceivedNote.getNo());
				
				storeStockCard.setCreatedBy(userService.getUserId(request));
				storeStockCard.setCreatedOn(dayService.getDayId());
				storeStockCard.setCreatedAt(dayService.getTimeStamp());
				
				storeStockCardRepository.save(storeStockCard);
			}
			
			
			//Add Store item batch
			for(GoodsReceivedNoteDetailBatch batch : detail.getGoodsReceivedNoteDetailBatches()) {
				StoreItemBatch storeItemBatch = new StoreItemBatch();
				storeItemBatch.setNo(batch.getNo());
				storeItemBatch.setItem(batch.getGoodsReceivedNoteDetail().getItem());
				storeItemBatch.setManufacturedDate(batch.getManufacturedDate());
				storeItemBatch.setExpiryDate(batch.getExpiryDate());
				storeItemBatch.setQty(batch.getQty());
				storeItemBatch.setStore(goodsReceivedNote.getStore());
				
				storeItemBatchRepository.save(storeItemBatch);
			}
			
		}
		
		if(goodsReceivedNote.getLocalPurchaseOrder() != null) {
			LocalPurchaseOrder localPurchaseOrder = goodsReceivedNote.getLocalPurchaseOrder();
			for(GoodsReceivedNoteDetail detail : goodsReceivedNote.getGoodsReceivedNoteDetails()) {
				if(detail.getReceivedQty() > 0) {
					Purchase purchase = new Purchase();
					purchase.setItem(detail.getItem());
					purchase.setQty(detail.getReceivedQty());
					purchase.setAmount(detail.getReceivedQty() * detail.getPrice());
					purchase.setGoodsReceivedNote(detail.getGoodsReceivedNote());
					
					purchase.setCreatedBy(userService.getUserId(request));
					purchase.setCreatedOn(dayService.getDayId());
					purchase.setCreatedAt(dayService.getTimeStamp());
					
					purchaseRepository.save(purchase);
				}
			}
			localPurchaseOrder.setStatus("RECEIVED");
			
			localPurchaseOrder.setReceivedBy(userService.getUserId(request));
			localPurchaseOrder.setReceivedOn(dayService.getDayId());
			localPurchaseOrder.setReceivedAt(dayService.getTimeStamp());
			
			localPurchaseOrderRepository.save(localPurchaseOrder);
			
		}
		
		goodsReceivedNote.setStatus("APPROVED");
		goodsReceivedNote.setStatusDescription("All/Partial ordered goods received");
		
		goodsReceivedNote.setApprovedBy(userService.getUserId(request));
		goodsReceivedNote.setApprovedOn(dayService.getDayId());
		goodsReceivedNote.setApprovedAt(dayService.getTimeStamp());
		
		goodsReceivedNoteRepository.save(goodsReceivedNote);
		
		return null;
	}
	
	//@Override
	public RecordModel requestRequestGrnNo() {
		Long id = 1L;
		try {
			id = goodsReceivedNoteRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("GRN",id.toString()));
		return model;
	}
}
