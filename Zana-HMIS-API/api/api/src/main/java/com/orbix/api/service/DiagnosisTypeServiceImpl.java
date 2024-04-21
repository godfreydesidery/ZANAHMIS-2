/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DiagnosisTypeRepository;
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
public class DiagnosisTypeServiceImpl implements DiagnosisTypeService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final DiagnosisTypeRepository diagnosisTypeRepository;
	
	@Override
	public DiagnosisType save(DiagnosisType diagnosisType, HttpServletRequest request) {
		
		diagnosisType.setName(diagnosisType.getName());
		
		if(diagnosisType.getId() == null) {
			diagnosisType.setCreatedBy(userService.getUser(request).getId());
			diagnosisType.setCreatedOn(dayService.getDay().getId());
			diagnosisType.setCreatedAt(dayService.getTimeStamp());
			
			diagnosisType.setActive(true);
		
		}
		
		log.info("Saving new diagnosisType to the database");
		return diagnosisTypeRepository.save(diagnosisType);
	}

	@Override
	public List<DiagnosisType> getDiagnosisTypes(HttpServletRequest request) {
		log.info("Fetching all diagnosisTypes");
		return diagnosisTypeRepository.findAll();
	}

	@Override
	public DiagnosisType getDiagnosisTypeByName(String name, HttpServletRequest request) {
		return diagnosisTypeRepository.findByName(name).get();
	}

	@Override
	public DiagnosisType getDiagnosisTypeById(Long id, HttpServletRequest request) {
		return diagnosisTypeRepository.findById(id).get();
	}

	@Override
	public boolean deleteDiagnosisType(DiagnosisType diagnosisType, HttpServletRequest request) {
		/**
		 * Delete a diagnosisType if a diagnosisType is deletable
		 */
		if(allowDeleteDiagnosisType(diagnosisType) == false) {
			throw new InvalidOperationException("Deleting this diagnosisType is not allowed");
		}
		diagnosisTypeRepository.delete(diagnosisType);
		return true;
	}
	
	private boolean allowDeleteDiagnosisType(DiagnosisType diagnosisType) {
		/**
		 * Code to check if a diagnosisType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
