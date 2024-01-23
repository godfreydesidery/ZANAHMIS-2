/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClinicServiceImpl implements ClinicService{
	
	private final UserService userService;
	private final DayService dayService;
	private final ClinicRepository clinicRepository;
	//private final ClinicianRepository clinicianRepository;
	
	@Override
	public Clinic save(Clinic clinic, HttpServletRequest request) {
		
		clinic.setName(clinic.getName());
		
		if(clinic.getId() == null) {
			clinic.setCreatedby(userService.getUser(request).getId());
			clinic.setCreatedOn(dayService.getDay().getId());
			clinic.setCreatedAt(dayService.getTimeStamp());
			
			clinic.setActive(true);
		}
		
		
		log.info("Saving new clinic to the database");
		return clinicRepository.save(clinic);
	}

	@Override
	public List<Clinic> getClinics(HttpServletRequest request) {
		log.info("Fetching all clinics");
		return clinicRepository.findAll();
	}

	@Override
	public Clinic getClinicByName(String name, HttpServletRequest request) {
		return clinicRepository.findByName(name).get();
	}

	@Override
	public Clinic getClinicById(Long id, HttpServletRequest request) {
		return clinicRepository.findById(id).get();
	}

	@Override
	public boolean deleteClinic(Clinic clinic, HttpServletRequest request) {
		/**
		 * Delete a clinic if a clinic is deletable
		 */
		if(allowDeleteClinic(clinic) == false) {
			throw new InvalidOperationException("Deleting this clinic is not allowed");
		}
		clinicRepository.delete(clinic);
		return true;
	}
	
	private boolean allowDeleteClinic(Clinic clinic) {
		/**
		 * Code to check if a clinic is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public List<String> getNames(HttpServletRequest request) {
		return clinicRepository.getNames();	
	}

	@Override
	public Clinic getByName(String clinicName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return clinicRepository.findByName(clinicName).get();
	}

	//@Override
	//public List<Clinician> getClinicians(HttpServletRequest request) {
		//// TODO Auto-generated method stub
		//return clinicianRepository.findAll();
	//}

	
}
