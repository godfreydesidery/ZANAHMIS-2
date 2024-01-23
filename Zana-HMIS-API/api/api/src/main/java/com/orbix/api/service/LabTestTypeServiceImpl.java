/**
 * 
 */
package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.LabTestTypeRepository;
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
public class LabTestTypeServiceImpl implements LabTestTypeService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final LabTestTypeRepository labTestTypeRepository;
	
	@Override
	public LabTestType save(LabTestType labTestType, HttpServletRequest request) {
		LabTestType testType = new LabTestType();
		
		if(labTestType.getId() != null) {
			testType = labTestTypeRepository.findById(labTestType.getId()).get();
			
			testType.setCode(testType.getCode().replace(" ", ""));
			testType.setName(testType.getName().trim().replaceAll("\\s+", " "));
			
			testType.setDescription(labTestType.getDescription());
			testType.setPrice(labTestType.getPrice());
			testType.setUom(labTestType.getUom());
		}else {
			testType = labTestType;
			testType.setCreatedby(userService.getUser(request).getId());
			testType.setCreatedOn(dayService.getDay().getId());
			testType.setCreatedAt(dayService.getTimeStamp());
			
			testType.setActive(true);
		}
		
		testType.setName(labTestType.getName());
		
		
		
		log.info("Saving new labTestType to the database");
		return labTestTypeRepository.save(testType);
	}

	@Override
	public List<LabTestType> getLabTestTypes(HttpServletRequest request) {
		log.info("Fetching all labTestTypes");
		return labTestTypeRepository.findAll();
	}

	@Override
	public LabTestType getLabTestTypeByName(String name, HttpServletRequest request) {
		return labTestTypeRepository.findByName(name).get();
	}

	@Override
	public LabTestType getLabTestTypeById(Long id, HttpServletRequest request) {
		return labTestTypeRepository.findById(id).get();
	}

	@Override
	public boolean deleteLabTestType(LabTestType labTestType, HttpServletRequest request) {
		/**
		 * Delete a labTestType if a labTestType is deletable
		 */
		if(allowDeleteLabTestType(labTestType) == false) {
			throw new InvalidOperationException("Deleting this labTestType is not allowed");
		}
		labTestTypeRepository.delete(labTestType);
		return true;
	}
	
	private boolean allowDeleteLabTestType(LabTestType labTestType) {
		/**
		 * Code to check if a labTestType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
