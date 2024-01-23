/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.LabTestTypeRangeRepository;
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
public class LabTestTypeRangeServiceImpl implements LabTestTypeRangeService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final LabTestTypeRangeRepository labTestTypeRangeRepository;
	
	@Override
	public LabTestTypeRange save(LabTestTypeRange labTestTypeRange, HttpServletRequest request) {
		
		labTestTypeRange.setName(labTestTypeRange.getName());
		
		labTestTypeRange.setCreatedby(userService.getUser(request).getId());
		labTestTypeRange.setCreatedOn(dayService.getDay().getId());
		labTestTypeRange.setCreatedAt(dayService.getTimeStamp());
		
		log.info("Saving new labTestTypeRange to the database");
		return labTestTypeRangeRepository.save(labTestTypeRange);
	}

	@Override
	public List<LabTestTypeRange> getLabTestTypeRanges(LabTestType labTestType, HttpServletRequest request) {
		log.info("Fetching all labTestTypeRanges");
		return labTestTypeRangeRepository.findAllByLabTestType(labTestType);
	}

	@Override
	public LabTestTypeRange getLabTestTypeRangeByName(String name, HttpServletRequest request) {
		return labTestTypeRangeRepository.findByName(name).get();
	}

	@Override
	public LabTestTypeRange getLabTestTypeRangeById(Long id, HttpServletRequest request) {
		return labTestTypeRangeRepository.findById(id).get();
	}

	@Override
	public boolean deleteLabTestTypeRange(LabTestTypeRange labTestTypeRange, HttpServletRequest request) {
		/**
		 * Delete a labTestTypeRange if a labTestTypeRange is deletable
		 */
		if(allowDeleteLabTestTypeRange(labTestTypeRange) == false) {
			throw new InvalidOperationException("Deleting this labTestTypeRange is not allowed");
		}
		labTestTypeRangeRepository.delete(labTestTypeRange);
		return true;
	}
	
	private boolean allowDeleteLabTestTypeRange(LabTestTypeRange labTestTypeRange) {
		/**
		 * Code to check if a labTestTypeRange is deletable
		 * Returns false if not
		 */
		return false;
	}
}
