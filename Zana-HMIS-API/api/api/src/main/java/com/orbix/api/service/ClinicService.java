/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;

/**
 * @author Godfrey
 *
 */
public interface ClinicService {
	Clinic save(Clinic clinic, HttpServletRequest request);	
	List<Clinic>getClinics(HttpServletRequest request); // return all the clinics
	Clinic getClinicByName(String name, HttpServletRequest request);
	Clinic getClinicById(Long id, HttpServletRequest request);
	boolean deleteClinic(Clinic clinic, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param clinicName
	 * @return
	 */
	Clinic getByName(String clinicName, HttpServletRequest request);
	/**
	 * @return
	 */
	//List<Clinician> getClinicians( HttpServletRequest request);
	
}
