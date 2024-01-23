/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.WardCategory;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.WardCategoryRepository;
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
public class WardCategoryServiceImpl implements WardCategoryService {
	private final UserService userService;
	private final DayService dayService;
	private final WardCategoryRepository wardCategoryRepository;
	
	@Override
	public WardCategory save(WardCategory wardCategory, HttpServletRequest request) {
		
		if(wardCategory.getName().equals("")) {
			throw new InvalidEntryException("Name can not be empty");
		}
			
		wardCategory.setName(wardCategory.getName());
		
		
		
		if(wardCategory.getId() == null) {
			wardCategory.setCreatedby(userService.getUser(request).getId());
			wardCategory.setCreatedOn(dayService.getDay().getId());
			wardCategory.setCreatedAt(dayService.getTimeStamp());
			
		}
			
		log.info("Saving new wardCategory to the database");
		return wardCategoryRepository.save(wardCategory);
	}

	@Override
	public List<WardCategory> getWardCategories(HttpServletRequest request) {
		log.info("Fetching all wardCategories");
		return wardCategoryRepository.findAll();
	}
	
	@Override
	public WardCategory getWardCategoryById(Long id, HttpServletRequest request) {
		return wardCategoryRepository.findById(id).get();
	}
	
	@Override
	public WardCategory getWardCategoryByName(String name, HttpServletRequest request) {
		return wardCategoryRepository.findByName(name).get();
	}


	@Override
	public boolean deleteWardCategory(WardCategory wardCategory, HttpServletRequest request) {
		/**
		 * Delete a wardCategory if a wardCategory is deletable
		 */
		if(allowDeleteWardCategory(wardCategory) == false) {
			throw new InvalidOperationException("Deleting this wardCategory is not allowed");
		}
		wardCategoryRepository.delete(wardCategory);
		return true;
	}
	
	private boolean allowDeleteWardCategory(WardCategory wardCategory) {
		/**
		 * Code to check if a wardCategory is deletable
		 * Returns false if not
		 */
		return false;
	}
}
