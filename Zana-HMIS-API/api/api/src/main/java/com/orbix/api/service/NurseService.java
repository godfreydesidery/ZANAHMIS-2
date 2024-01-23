/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Nurse;

/**
 * @author Godfrey
 *
 */
public interface NurseService {
	Nurse save(Nurse nurse, HttpServletRequest request);	
	List<Nurse>getNurses(HttpServletRequest request); // return all the clinics
	List<Nurse>getActiveNurses(HttpServletRequest request);
	Nurse getNurseByName(String name, HttpServletRequest request);
	Nurse getNurseById(Long id, HttpServletRequest request);
	boolean deleteNurse(Nurse nurse, HttpServletRequest request);
}
