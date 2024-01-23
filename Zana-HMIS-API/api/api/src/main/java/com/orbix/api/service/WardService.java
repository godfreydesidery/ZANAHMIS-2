/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Ward;
import com.orbix.api.domain.WardBed;

/**
 * @author Godfrey
 *
 */
public interface WardService {
	Ward save(Ward ward, HttpServletRequest request);	
	List<Ward>getWards(HttpServletRequest request); // return all the wards
	Ward getWardById(Long id, HttpServletRequest request);
	Ward getWardByName(String name, HttpServletRequest request);	
	boolean deleteWard(Ward ward, HttpServletRequest request);
	
	WardBed registerBed(WardBed wardBed, HttpServletRequest request);
	
	WardBed activateBed(WardBed wardBed, HttpServletRequest request);
	WardBed deactivateBed(WardBed wardBed, HttpServletRequest request);
	
	WardBed updateBed(WardBed wardBed, HttpServletRequest request);
}
