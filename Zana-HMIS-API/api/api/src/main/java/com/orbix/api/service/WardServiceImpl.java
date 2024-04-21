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
import com.orbix.api.domain.Ward;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.WardCategory;
import com.orbix.api.domain.WardType;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.repositories.WardCategoryRepository;
import com.orbix.api.repositories.WardRepository;
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
public class WardServiceImpl implements WardService {
	private final UserService userService;
	private final DayService dayService;
	private final WardRepository wardRepository;
	private final WardCategoryRepository wardCategoryRepository;
	private final WardTypeRepository wardTypeRepository;
	private final WardBedRepository wardBedRepository;
	
	@Override
	public Ward save(Ward ward, HttpServletRequest request) {
		
		ward.setName(ward.getName());
		if(ward.getNoOfBeds() < 0) {
			throw new InvalidOperationException("Invalid no of beds. No of beds can not be negative");
		}
		
		Optional<WardCategory> wc = wardCategoryRepository.findById(ward.getWardCategory().getId());
		if(wc.isEmpty()) {
			throw new NotFoundException("Category not found");
		}
		
		Optional<WardType> wt = wardTypeRepository.findById(ward.getWardType().getId());
		if(wc.isEmpty()) {
			throw new NotFoundException("Category not found");
		}
		
		if(ward.getId() == null) {
			ward.setCreatedBy(userService.getUser(request).getId());
			ward.setCreatedOn(dayService.getDay().getId());
			ward.setCreatedAt(dayService.getTimeStamp());
			
		}
			
		log.info("Saving new ward to the database");
		return wardRepository.save(ward);
	}

	@Override
	public List<Ward> getWards(HttpServletRequest request) {
		log.info("Fetching all wardCategories");
		return wardRepository.findAll();
	}
	
	@Override
	public Ward getWardById(Long id, HttpServletRequest request) {
		return wardRepository.findById(id).get();
	}
	
	@Override
	public Ward getWardByName(String name, HttpServletRequest request) {
		return wardRepository.findByName(name).get();
	}


	@Override
	public boolean deleteWard(Ward ward, HttpServletRequest request) {
		/**
		 * Delete a ward if a ward is deletable
		 */
		if(allowDeleteWard(ward) == false) {
			throw new InvalidOperationException("Deleting this ward is not allowed");
		}
		wardRepository.delete(ward);
		return true;
	}
	
	private boolean allowDeleteWard(Ward ward) {
		/**
		 * Code to check if a ward is deletable
		 * Returns false if not
		 */
		return false;
	}

	@Override
	public WardBed registerBed(WardBed wb, HttpServletRequest request) {
		
		String no = Sanitizer.sanitizeString(wb.getNo());
		WardBed wardBed = new WardBed();
		
		Optional<Ward> w = wardRepository.findById(wb.getWard().getId());
		if(w.isEmpty()) {
			throw new NotFoundException("Ward not found");
		}
		
		if(wardBedRepository.existsByNoAndWard(no, w.get())) {
			throw new InvalidOperationException("Could not register. Ward with similar number already exist");
		}
		
		int count = 0; // use count later
		List<WardBed> wardBeds = wardBedRepository.findAllByWard(w.get());
		for(WardBed b : wardBeds) {
			count = count + 1;
		}
		if(count >= w.get().getNoOfBeds()) {
			throw new InvalidOperationException("Bed/Room Limit reached");
		}
		
		if(no.equals("")) {
			throw new InvalidEntryException("Bed/Room no can not be empty");
		}
		
		wardBed.setNo(no);
		wardBed.setWard(w.get());
		wardBed.setActive(true);
		wardBed.setStatus("EMPTY");
		
		wardBed.setCreatedBy(userService.getUser(request).getId());
		wardBed.setCreatedOn(dayService.getDay().getId());
		wardBed.setCreatedAt(dayService.getTimeStamp());
		
		return wardBedRepository.save(wardBed);
	}
	
	@Override
	public WardBed updateBed(WardBed wb, HttpServletRequest request) {
		
		String no = Sanitizer.sanitizeString(wb.getNo());
		
		if(wb.getNo().equals("")) {
			throw new InvalidEntryException("Bed/Room no can not be empty");
		}
		
		Optional<WardBed> wardBed = wardBedRepository.findById(wb.getId());
		if(wardBed.isEmpty()) {
			throw new NotFoundException("Bed/Room not found");
		}
		
		Optional<WardBed> wardBed2 = wardBedRepository.findByNo(no);
		
		if(wardBed2.isPresent()) {
			if(wardBed.get().getId() == wardBed2.get().getId()) {
				throw new InvalidOperationException("could not update, a bed/room with similar number already exist");
			}
		}
		
		wardBed.get().setNo(no);
		
		return wardBedRepository.save(wardBed.get());
	}
	
	
	@Override
	public WardBed activateBed(WardBed wb, HttpServletRequest request) {
		
		Optional<WardBed> wardBed = wardBedRepository.findById(wb.getId());
		
		wardBed.get().setActive(true);
		
		return wardBedRepository.save(wardBed.get());
	}
	
	@Override
	public WardBed deactivateBed(WardBed wb, HttpServletRequest request) {
		
		Optional<WardBed> wardBed = wardBedRepository.findById(wb.getId());
		
		if(!wardBed.get().getStatus().equals("EMPTY")) {
			throw new InvalidOperationException("Could not deactivate, bed not empty");
		}
		
		wardBed.get().setActive(false);
		
		return wardBedRepository.save(wardBed.get());
	}
}
