/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Store;

/**
 * @author Godfrey
 *
 */
public interface StoreService {
	Store save(Store store, HttpServletRequest request);
	Store updateStoreItemRegister(Store store, HttpServletRequest request);
	List<Store>getStores(HttpServletRequest request); // return all the stores
	List<Store>getStoresByStorePerson(HttpServletRequest request); // return all the stores available for a particular store person
	Store getStoreByName(String name, HttpServletRequest request);
	Store getStoreById(Long id, HttpServletRequest request);
	boolean deleteStore(Store store, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param storeName
	 * @return
	 */
	Store getByName(String storeName, HttpServletRequest request);
}
