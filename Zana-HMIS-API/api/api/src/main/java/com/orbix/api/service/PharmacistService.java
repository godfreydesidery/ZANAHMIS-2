/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Pharmacist;

/**
 * @author Godfrey
 *
 */
public interface PharmacistService {
	Pharmacist save(Pharmacist pharmacist, HttpServletRequest request);	
	List<Pharmacist>getPharmacists(HttpServletRequest request); // return all the clinics
	List<Pharmacist>getActivePharmacists(HttpServletRequest request);
	Pharmacist getPharmacistByName(String name, HttpServletRequest request);
	Pharmacist getPharmacistById(Long id, HttpServletRequest request);
	boolean deletePharmacist(Pharmacist pharmacist, HttpServletRequest request);
}
