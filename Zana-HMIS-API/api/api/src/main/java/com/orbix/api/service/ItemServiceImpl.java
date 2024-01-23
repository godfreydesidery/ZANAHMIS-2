/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Item;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.Store;
import com.orbix.api.domain.StoreItem;
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.StoreItemRepository;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.repositories.StoreStockCardRepository;
import com.orbix.api.repositories.DayRepository;
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
public class ItemServiceImpl implements ItemService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ItemRepository itemRepository;
	private final StoreRepository storeRepository;
	private final StoreItemRepository storeItemRepository;
	private final StoreStockCardRepository storeStockCardRepository;
	
	@Override
	public Item save(Item item, HttpServletRequest request) {
		
		item.setCode(item.getCode().replace(" ", ""));
		item.setName(item.getName().trim().replaceAll("\\s+", " "));
		
		if(item.getId() == null) {
			item.setCreatedBy(userService.getUser(request).getId());
			item.setCreatedOn(dayService.getDay().getId());
			item.setCreatedAt(dayService.getTimeStamp());
			
			item.setActive(true);
		}
		
		if(item.getId() == null) {
			item = itemRepository.save(item);
			List<Store> stores = storeRepository.findAll();
			for(Store store : stores) {
				StoreItem storeItem = new StoreItem();
				storeItem.setItem(item);
				storeItem.setStore(store);
				storeItem.setStock(0);
				storeItem = storeItemRepository.save(storeItem);
				
				StoreStockCard storeStockCard = new StoreStockCard();
				storeStockCard.setItem(item);
				storeStockCard.setStore(store);
				storeStockCard.setQtyIn(storeItem.getStock());
				storeStockCard.setQtyOut(0);
				storeStockCard.setBalance(0);
				storeStockCard.setReference("Opening stock, item creation");
				
				storeStockCard.setCreatedBy(userService.getUserId(request));
				storeStockCard.setCreatedOn(dayService.getDayId());
				storeStockCard.setCreatedAt(dayService.getTimeStamp());
				
				storeStockCardRepository.save(storeStockCard);
			}			
		}
		
		
		log.info("Saving new item to the database");
		return itemRepository.save(item);
	}

	@Override
	public List<Item> getItems(HttpServletRequest request) {
		log.info("Fetching all items");
		return itemRepository.findAll();
	}

	@Override
	public Item getItemByName(String name, HttpServletRequest request) {
		return itemRepository.findByName(name).get();
	}

	@Override
	public Item getItemById(Long id, HttpServletRequest request) {
		return itemRepository.findById(id).get();
	}

	@Override
	public boolean deleteItem(Item item, HttpServletRequest request) {
		/**
		 * Delete a item if a item is deletable
		 */
		if(allowDeleteItem(item) == false) {
			throw new InvalidOperationException("Deleting this item is not allowed");
		}
		itemRepository.delete(item);
		return true;
	}
	
	private boolean allowDeleteItem(Item item) {
		/**
		 * Code to check if a item is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public List<String> getNames(HttpServletRequest request) {
		return itemRepository.getNames();	
	}

	@Override
	public Item getByName(String itemName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return itemRepository.findByName(itemName).get();
	}
}
