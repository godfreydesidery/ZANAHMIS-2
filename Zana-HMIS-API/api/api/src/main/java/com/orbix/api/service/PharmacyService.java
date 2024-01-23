/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Pharmacy;

/**
 * @author Godfrey
 *
 */
public interface PharmacyService {
	Pharmacy save(Pharmacy pharmacy, HttpServletRequest request);	
	List<Pharmacy>getPharmacies(HttpServletRequest request); // return all the pharmacies
	Pharmacy getPharmacyByName(String name, HttpServletRequest request);
	Pharmacy getPharmacyById(Long id, HttpServletRequest request);
	boolean deletePharmacy(Pharmacy pharmacy, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param pharmacyName
	 * @return
	 */
	Pharmacy getByName(String pharmacyName, HttpServletRequest request);
	
}
