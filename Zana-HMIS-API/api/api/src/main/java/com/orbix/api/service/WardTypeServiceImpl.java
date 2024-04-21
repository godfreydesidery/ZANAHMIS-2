/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.WardType;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.TheatreRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.repositories.WardTypeRepository;

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
public class WardTypeServiceImpl implements WardTypeService {
	private final UserService userService;
	private final DayService dayService;
	private final WardTypeRepository wardTypeRepository;
	
	@Override
	public WardType save(WardType wardType, HttpServletRequest request) {
		
		if(wardType.getName().equals("")) {
			throw new InvalidEntryException("Name can not be empty");
		}
		
		wardType.setName(wardType.getName());
		
		if(wardType.getId() == null) {
			wardType.setCreatedBy(userService.getUser(request).getId());
			wardType.setCreatedOn(dayService.getDay().getId());
			wardType.setCreatedAt(dayService.getTimeStamp());
			
		}
			
		log.info("Saving new wardType to the database");
		return wardTypeRepository.save(wardType);
	}

	@Override
	public List<WardType> getWardTypes(HttpServletRequest request) {
		log.info("Fetching all wardCategories");
		return wardTypeRepository.findAll();
	}
	
	@Override
	public WardType getWardTypeById(Long id, HttpServletRequest request) {
		return wardTypeRepository.findById(id).get();
	}
	
	@Override
	public WardType getWardTypeByName(String name, HttpServletRequest request) {
		return wardTypeRepository.findByName(name).get();
	}


	@Override
	public boolean deleteWardType(WardType wardType, HttpServletRequest request) {
		/**
		 * Delete a wardType if a wardType is deletable
		 */
		if(allowDeleteWardType(wardType) == false) {
			throw new InvalidOperationException("Deleting this wardType is not allowed");
		}
		wardTypeRepository.delete(wardType);
		return true;
	}
	
	private boolean allowDeleteWardType(WardType wardType) {
		/**
		 * Code to check if a wardType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
