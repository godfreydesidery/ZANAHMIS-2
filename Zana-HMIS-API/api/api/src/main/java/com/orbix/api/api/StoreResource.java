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

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Store;
import com.orbix.api.domain.StoreItem;
import com.orbix.api.domain.StoreItemBatch;
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.StoreItemBatchRepository;
import com.orbix.api.repositories.StoreItemRepository;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.repositories.StoreStockCardRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.StoreService;
import com.orbix.api.service.UserService;

import lombok.Data;
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
public class StoreResource {

	private final StoreRepository storeRepository;
	private final ItemRepository itemRepository;
	private final StoreService storeService;
	private final StoreItemRepository storeItemRepository;
	private final StoreStockCardRepository storeStockCardRepository;
	private final StoreItemBatchRepository storeItemBatchRepository;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/stores")
	public ResponseEntity<List<Store>>getPharmacies(HttpServletRequest request){
		return ResponseEntity.ok().body(storeService.getStores(request));
	}
	
	@GetMapping("/stores/load_stores_by_store_person")
	public ResponseEntity<List<Store>>getStoresByStorePerson(HttpServletRequest request){
		return ResponseEntity.ok().body(storeService.getStoresByStorePerson(request));
	}
	
	@GetMapping("/stores/load_stores_like")
	public ResponseEntity<List<Store>> getStoreNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Store> stores = new ArrayList<Store>();
		stores = storeRepository.findAllByNameContainingOrCodeContaining(value, value);
		return ResponseEntity.ok().body(stores);
	}
	
	@GetMapping("/stores/get")
	public ResponseEntity<Store> getStore(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(storeService.getStoreById(id, request));
	}
	
	@GetMapping("/stores/get_by_name")
	public ResponseEntity<Store> getStoreByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(storeService.getStoreByName(name, request));
	}
	
	@GetMapping("/stores/get_names")
	public ResponseEntity<List<String>> getStoreNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = storeService.getNames(request);
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/stores/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Store>save(
			@RequestBody Store store,
			HttpServletRequest request){
		store.setName(store.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/stores/save").toUriString());
		return ResponseEntity.created(uri).body(storeService.save(store, request));
	}
	
	@PostMapping("/stores/update_store_item_register")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Store>updateStoreItemRegister(
			@RequestBody Store store,
			HttpServletRequest request){
		
		store.setName(store.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/stores/update_store_item_register").toUriString());
		return ResponseEntity.created(uri).body(storeService.updateStoreItemRegister(store, request));
	}
	
	@GetMapping("/stores/get_store_item_list")
	public ResponseEntity<List<StoreItem>> getStoreItemList(
			@RequestParam(name = "store_name") String storeName,
			HttpServletRequest request){
		
		Optional<Store> p = storeRepository.findByName(storeName);
		if(p.isEmpty()) {
			throw new NotFoundException("Store not found");
		}
		
		List<StoreItem> storeItems = storeItemRepository.findAllByStore(p.get());
		List<StoreItem> storeItemsToShow = new ArrayList<>();
		for(StoreItem pm : storeItems) {
			if(pm.getItem().isActive() == true) {
				storeItemsToShow.add(pm);
			}
		}
		
		
		return ResponseEntity.ok().body(storeItemsToShow);
	}
	
	@GetMapping("/stores/get_store_item_batches")
	public ResponseEntity<List<StoreItemBatch>> getStoreItemList(
			@RequestParam(name = "store_id") Long storeId,
			@RequestParam(name = "item_id") Long itemId,
			HttpServletRequest request){
		
		Optional<Store> store_ = storeRepository.findById(storeId);
		if(store_.isEmpty()) {
			throw new NotFoundException("Store not found");
		}
		Optional<Item> item_ = itemRepository.findById(itemId);
		if(item_.isEmpty()) {
			throw new NotFoundException("Item not found");
		}
		
		List<StoreItemBatch> storeItemBatches = storeItemBatchRepository.findAllByStoreAndItemAndQtyGreaterThan(store_.get(), item_.get(), 0);
		
		return ResponseEntity.ok().body(storeItemBatches);
	}
	
	@PostMapping("/stores/update_stock")
	@PreAuthorize("hasAnyAuthority('ITEM_STOCK-UPDATE')")
	public void updateStock(
			@RequestBody LStoreItem pm,
			HttpServletRequest request){
		
		StoreItem storeItem = storeItemRepository.findById(pm.getId()).get();
		Store store = storeItem.getStore();
		Item item = storeItem.getItem();
		if(item.isActive() == false) {
			throw new InvalidOperationException("Can not update stock. Item not active");
		}
		if(pm.getStock() < 0) {
			throw new InvalidOperationException("Negative value is not allowed");
		}
		storeItem.setStock(pm.getStock());
		storeItemRepository.save(storeItem);
		
		StoreStockCard storeStockCard = new StoreStockCard();
		storeStockCard.setItem(item);
		storeStockCard.setStore(store);
		storeStockCard.setQtyIn(pm.getStock());
		storeStockCard.setQtyOut(0);
		storeStockCard.setBalance(pm.getStock());
		storeStockCard.setReference("Stock Update");
		
		storeStockCard.setCreatedBy(userService.getUserId(request));
		storeStockCard.setCreatedOn(dayService.getDayId());
		storeStockCard.setCreatedAt(dayService.getTimeStamp());
		
		storeStockCardRepository.save(storeStockCard);
		
	}
	
}

@Data
class LStoreItem {
	Long id;
	double stock;
}

