/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
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
public class ProcedureTypeServiceImpl implements ProcedureTypeService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ProcedureTypeRepository procedureTypeRepository;
	
	@Override
	public ProcedureType save(ProcedureType procedureType, HttpServletRequest request) {
		
		procedureType.setCode(procedureType.getCode().replace(" ", ""));
		procedureType.setName(procedureType.getName().trim().replaceAll("\\s+", " "));
		
		if(procedureType.getId() == null) {
			procedureType.setCreatedBy(userService.getUser(request).getId());
			procedureType.setCreatedOn(dayService.getDay().getId());
			procedureType.setCreatedAt(dayService.getTimeStamp());
			
			procedureType.setActive(true);
		}
		
		log.info("Saving new procedureType to the database");
		return procedureTypeRepository.save(procedureType);
	}

	@Override
	public List<ProcedureType> getProcedureTypes(HttpServletRequest request) {
		log.info("Fetching all procedureTypes");
		return procedureTypeRepository.findAll();
	}

	@Override
	public ProcedureType getProcedureTypeByName(String name, HttpServletRequest request) {
		return procedureTypeRepository.findByName(name).get();
	}

	@Override
	public ProcedureType getProcedureTypeById(Long id, HttpServletRequest request) {
		return procedureTypeRepository.findById(id).get();
	}

	@Override
	public boolean deleteProcedureType(ProcedureType procedureType, HttpServletRequest request) {
		/**
		 * Delete a procedureType if a procedureType is deletable
		 */
		if(allowDeleteProcedureType(procedureType) == false) {
			throw new InvalidOperationException("Deleting this procedureType is not allowed");
		}
		procedureTypeRepository.delete(procedureType);
		return true;
	}
	
	private boolean allowDeleteProcedureType(ProcedureType procedureType) {
		/**
		 * Code to check if a procedureType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
