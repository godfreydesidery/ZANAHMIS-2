/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.NurseRepository;
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
public class NurseServiceImpl implements NurseService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final NurseRepository nurseRepository;
	
	@Override
	public Nurse save(Nurse nurse, HttpServletRequest request) {
		
		Optional<User> u = userRepository.findByCode(nurse.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		if(!u.get().getFirstName().equals(nurse.getFirstName()) || !u.get().getMiddleName().equals(nurse.getMiddleName()) || !u.get().getLastName().equals(nurse.getLastName())){
			throw new InvalidOperationException("Provided names do not match with user account");
		}
		nurse.setNickname(u.get().getNickname());
		nurse.setUser(u.get());

		if(nurse.getId() == null) {
			nurse.setCreatedby(userService.getUser(request).getId());
			nurse.setCreatedOn(dayService.getDay().getId());
			nurse.setCreatedAt(dayService.getTimeStamp());
			
			nurse.setActive(true);
		}
		log.info("Saving new clinic to the database");
		return nurseRepository.save(nurse);
	}

	@Override
	public List<Nurse> getNurses(HttpServletRequest request) {
		log.info("Fetching all nurses");
		return nurseRepository.findAll();
	}
	
	@Override
	public List<Nurse> getActiveNurses(HttpServletRequest request) {
		log.info("Fetching all active nurses");
		return nurseRepository.findAllByActive(true);
	}

	@Override
	public Nurse getNurseByName(String name, HttpServletRequest request) {
		return nurseRepository.findByNickname(name).get();
	}

	@Override
	public Nurse getNurseById(Long id, HttpServletRequest request) {
		return nurseRepository.findById(id).get();
	}

	@Override
	public boolean deleteNurse(Nurse nurse, HttpServletRequest request) {
		/**
		 * Delete a nurse if a nurse is deletable
		 */
		if(allowDeleteNurse(nurse) == false) {
			throw new InvalidOperationException("Deleting this nurse is not allowed");
		}
		nurseRepository.delete(nurse);
		return true;
	}
	
	private boolean allowDeleteNurse(Nurse nurse) {
		/**
		 * Code to check if a nurse is deletable
		 * Returns false if not
		 */
		return false;
	}
}
