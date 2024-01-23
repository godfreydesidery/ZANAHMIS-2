/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Dressing;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DressingRepository;
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
public class DressingServiceImpl implements DressingService {
	
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	
	private final DressingRepository dressingRepository;
	private final ProcedureTypeRepository procedureTypeRepository;
	
	@Override
	public Dressing add(Dressing d, HttpServletRequest request) {
		Dressing dressing = new Dressing();
		Optional<ProcedureType> pt = procedureTypeRepository.findById(d.getProcedureType().getId());
		
		if(pt.isEmpty()) {
			throw new NotFoundException("Procedure type not found");
		}
		List<Dressing> ds = dressingRepository.findAllByProcedureType(pt.get());
		if(!ds.isEmpty()) {
			throw new InvalidOperationException("Dressing already exists");
		}
		
		dressing.setProcedureType(pt.get());
		
		dressing.setCreatedBy(userService.getUser(request).getId());
		dressing.setCreatedOn(dayService.getDay().getId());
		dressing.setCreatedAt(dayService.getTimeStamp());
		
		return dressingRepository.save(dressing);
		
	}

	@Override
	public boolean deleteById(Long id) {
		
		dressingRepository.deleteById(id);
		
		return true;
	}

}
