/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
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
public class InsurancePlanServiceImpl implements InsurancePlanService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final InsurancePlanRepository insurancePlanRepository;
	
	@Override
	public InsurancePlan save(InsurancePlan insurancePlan, HttpServletRequest request) {
		insurancePlan.setName(insurancePlan.getName());
		
		
		
		if(insurancePlan.getId() == null) {
			insurancePlan.setCreatedBy(userService.getUser(request).getId());
			insurancePlan.setCreatedOn(dayService.getDay().getId());
			insurancePlan.setCreatedAt(dayService.getTimeStamp());
			
			insurancePlan.setActive(true);
		}
		
		log.info("Saving new insurancePlan to the database");
		return insurancePlanRepository.save(insurancePlan);
	}

	@Override
	public List<InsurancePlan> getInsurancePlans(HttpServletRequest request) {
		log.info("Fetching all insurancePlans");
		return insurancePlanRepository.findAll();
	}

	@Override
	public InsurancePlan getInsurancePlanByName(String name, HttpServletRequest request) {
		return insurancePlanRepository.findByName(name).get();
	}

	@Override
	public InsurancePlan getInsurancePlanById(Long id, HttpServletRequest request) {
		return insurancePlanRepository.findById(id).get();
	}

	@Override
	public boolean deleteInsurancePlan(InsurancePlan insurancePlan, HttpServletRequest request) {
		/**
		 * Delete a insurancePlan if a insurancePlan is deletable
		 */
		if(allowDeleteInsurancePlan(insurancePlan) == false) {
			throw new InvalidOperationException("Deleting this insurancePlan is not allowed");
		}
		insurancePlanRepository.delete(insurancePlan);
		return true;
	}
	
	private boolean allowDeleteInsurancePlan(InsurancePlan insurancePlan) {
		/**
		 * Code to check if a insurancePlan is deletable
		 * Returns false if not
		 */
		return false;
	}
}
