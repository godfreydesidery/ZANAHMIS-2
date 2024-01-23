/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.InsuranceProviderRepository;
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
public class InsuranceProviderServiceImpl implements InsuranceProviderService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final InsuranceProviderRepository insuranceProviderRepository;
	
	@Override
	public InsuranceProvider save(InsuranceProvider insuranceProvider, HttpServletRequest request) {
		
		insuranceProvider.setName(insuranceProvider.getName());
		
		if(insuranceProvider.getId() == null) {
			insuranceProvider.setCreatedby(userService.getUser(request).getId());
			insuranceProvider.setCreatedOn(dayService.getDay().getId());
			insuranceProvider.setCreatedAt(dayService.getTimeStamp());
			
			insuranceProvider.setActive(true);
		}
		
		
		return insuranceProviderRepository.save(insuranceProvider);
	}

	@Override
	public List<InsuranceProvider> getInsuranceProviders(HttpServletRequest request) {
		log.info("Fetching all insuranceProviders");
		return insuranceProviderRepository.findAll();
	}

	@Override
	public InsuranceProvider getInsuranceProviderByName(String name, HttpServletRequest request) {
		return insuranceProviderRepository.findByName(name);
	}

	@Override
	public InsuranceProvider getInsuranceProviderById(Long id, HttpServletRequest request) {
		return insuranceProviderRepository.findById(id).get();
	}

	@Override
	public boolean deleteInsuranceProvider(InsuranceProvider insuranceProvider, HttpServletRequest request) {
		/**
		 * Delete a insuranceProvider if a insuranceProvider is deletable
		 */
		if(allowDeleteInsuranceProvider(insuranceProvider) == false) {
			throw new InvalidOperationException("Deleting this insuranceProvider is not allowed");
		}
		insuranceProviderRepository.delete(insuranceProvider);
		return true;
	}
	
	private boolean allowDeleteInsuranceProvider(InsuranceProvider insuranceProvider) {
		/**
		 * Code to check if a insuranceProvider is deletable
		 * Returns false if not
		 */
		return false;
	}
}


