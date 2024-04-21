/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.ExternalMedicalProvider;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ExternalMedicalProviderRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
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
public class ExternalMedicalProviderServiceImpl implements ExternalMedicalProviderService {
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ExternalMedicalProviderRepository externalMedicalProviderRepository;
	
	@Override
	public ExternalMedicalProvider save(ExternalMedicalProvider externalMedicalProvider, HttpServletRequest request) {
		
		externalMedicalProvider.setName(externalMedicalProvider.getName());
		
		if(externalMedicalProvider.getId() == null) {
			externalMedicalProvider.setCreatedBy(userService.getUser(request).getId());
			externalMedicalProvider.setCreatedOn(dayService.getDay().getId());
			externalMedicalProvider.setCreatedAt(dayService.getTimeStamp());
			
			externalMedicalProvider.setActive(true);
		}
		
		
		return externalMedicalProviderRepository.save(externalMedicalProvider);
	}

	@Override
	public List<ExternalMedicalProvider> getExternalMedicalProviders(HttpServletRequest request) {
		log.info("Fetching all externalMedicalProviders");
		return externalMedicalProviderRepository.findAll();
	}

	@Override
	public ExternalMedicalProvider getExternalMedicalProviderByName(String name, HttpServletRequest request) {
		return externalMedicalProviderRepository.findByName(name);
	}

	@Override
	public ExternalMedicalProvider getExternalMedicalProviderById(Long id, HttpServletRequest request) {
		return externalMedicalProviderRepository.findById(id).get();
	}

	@Override
	public boolean deleteExternalMedicalProvider(ExternalMedicalProvider externalMedicalProvider, HttpServletRequest request) {
		/**
		 * Delete a externalMedicalProvider if a externalMedicalProvider is deletable
		 */
		if(allowDeleteExternalMedicalProvider(externalMedicalProvider) == false) {
			throw new InvalidOperationException("Deleting this externalMedicalProvider is not allowed");
		}
		externalMedicalProviderRepository.delete(externalMedicalProvider);
		return true;
	}
	
	private boolean allowDeleteExternalMedicalProvider(ExternalMedicalProvider externalMedicalProvider) {
		/**
		 * Code to check if a externalMedicalProvider is deletable
		 * Returns false if not
		 */
		return false;
	}
}