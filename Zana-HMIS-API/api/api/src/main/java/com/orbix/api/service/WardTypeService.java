/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.WardType;

/**
 * @author Godfrey
 *
 */
public interface WardTypeService {
	WardType save(WardType wardType, HttpServletRequest request);	
	List<WardType>getWardTypes(HttpServletRequest request); // return all the pharmacies
	WardType getWardTypeById(Long id, HttpServletRequest request);
	WardType getWardTypeByName(String name, HttpServletRequest request);	
	boolean deleteWardType(WardType wardType, HttpServletRequest request);
}
