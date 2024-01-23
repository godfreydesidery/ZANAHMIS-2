/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.WardCategory;

/**
 * @author Godfrey
 *
 */
public interface WardCategoryService {
	WardCategory save(WardCategory wardCategory, HttpServletRequest request);	
	List<WardCategory>getWardCategories(HttpServletRequest request); // return all the pharmacies
	WardCategory getWardCategoryById(Long id, HttpServletRequest request);
	WardCategory getWardCategoryByName(String name, HttpServletRequest request);	
	boolean deleteWardCategory(WardCategory wardCategory, HttpServletRequest request);
}
