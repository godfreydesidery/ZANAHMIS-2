/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Item;

/**
 * @author Godfrey
 *
 */
public interface ItemService {
	Item save(Item item, HttpServletRequest request);	
	List<Item>getItems(HttpServletRequest request); // return all the items
	Item getItemByName(String name, HttpServletRequest request);
	Item getItemById(Long id, HttpServletRequest request);
	boolean deleteItem(Item item, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param itemName
	 * @return
	 */
	Item getByName(String itemName, HttpServletRequest request);
}
