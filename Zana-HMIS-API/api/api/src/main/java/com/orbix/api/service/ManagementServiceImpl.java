package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Management;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ManagementRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManagementServiceImpl implements ManagementService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ManagementRepository managementRepository;
	
	@Override
	public Management save(Management management, HttpServletRequest request) {
		
		Optional<User> u = userRepository.findByCode(management.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		if(!u.get().getFirstName().equals(management.getFirstName()) || !u.get().getMiddleName().equals(management.getMiddleName()) || !u.get().getLastName().equals(management.getLastName())){
			throw new InvalidOperationException("Provided names do not match with user account");
		}
		management.setNickname(u.get().getNickname());
		management.setUser(u.get());

		if(management.getId() == null) {
			management.setCreatedBy(userService.getUser(request).getId());
			management.setCreatedOn(dayService.getDay().getId());
			management.setCreatedAt(dayService.getTimeStamp());
			
			management.setActive(true);
		}
		log.info("Saving new clinic to the database");
		return managementRepository.save(management);
	}

	@Override
	public List<Management> getManagements(HttpServletRequest request) {
		log.info("Fetching all managements");
		return managementRepository.findAll();
	}
	
	@Override
	public List<Management> getActiveManagements(HttpServletRequest request) {
		log.info("Fetching all active managements");
		return managementRepository.findAllByActive(true);
	}

	@Override
	public Management getManagementByName(String name, HttpServletRequest request) {
		return managementRepository.findByNickname(name).get();
	}

	@Override
	public Management getManagementById(Long id, HttpServletRequest request) {
		return managementRepository.findById(id).get();
	}

	@Override
	public boolean deleteManagement(Management management, HttpServletRequest request) {
		/**
		 * Delete a management if a management is deletable
		 */
		if(allowDeleteManagement(management) == false) {
			throw new InvalidOperationException("Deleting this management is not allowed");
		}
		managementRepository.delete(management);
		return true;
	}
	
	private boolean allowDeleteManagement(Management management) {
		/**
		 * Code to check if a management is deletable
		 * Returns false if not
		 */
		return false;
	}
}
