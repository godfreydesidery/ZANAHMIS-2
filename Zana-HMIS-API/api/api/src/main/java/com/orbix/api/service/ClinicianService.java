/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;

/**
 * @author Godfrey
 *
 */
public interface ClinicianService {
	Clinician save(Clinician clinician, HttpServletRequest request);	
	List<Clinician>getClinicians(HttpServletRequest request); // return all the clinics
	List<Clinician>getActiveClinicians(HttpServletRequest request);
	Clinician getClinicianByName(String name, HttpServletRequest request);
	Clinician getClinicianById(Long id, HttpServletRequest request);
	boolean deleteClinician(Clinician clinician, HttpServletRequest request);
}
