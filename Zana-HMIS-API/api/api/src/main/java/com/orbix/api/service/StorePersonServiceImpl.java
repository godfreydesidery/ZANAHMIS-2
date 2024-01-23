/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.StorePerson;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.StorePersonRepository;
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
public class StorePersonServiceImpl implements StorePersonService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final StorePersonRepository storePersonRepository;
	
	@Override
	public StorePerson save(StorePerson storePerson, HttpServletRequest request) {
		
		Optional<User> user_ = userRepository.findByCode(storePerson.getCode());
		if(user_.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		if(!user_.get().getFirstName().equals(storePerson.getFirstName()) || !user_.get().getMiddleName().equals(storePerson.getMiddleName()) || !user_.get().getLastName().equals(storePerson.getLastName())){
			throw new InvalidOperationException("Provided names do not match with user account");
		}
		storePerson.setNickname(user_.get().getNickname());
		storePerson.setUser(user_.get());

		if(storePerson.getId() == null) {
			storePerson.setCreatedBy(userService.getUser(request).getId());
			storePerson.setCreatedOn(dayService.getDay().getId());
			storePerson.setCreatedAt(dayService.getTimeStamp());
			
			storePerson.setActive(true);
		}
		log.info("Saving new clinic to the database");
		return storePersonRepository.save(storePerson);
	}

	@Override
	public List<StorePerson> getStorePersons(HttpServletRequest request) {
		log.info("Fetching all storePersons");
		return storePersonRepository.findAll();
	}
	
	@Override
	public List<StorePerson> getActiveStorePersons(HttpServletRequest request) {
		log.info("Fetching all storePersons");
		return storePersonRepository.findAllByActive(true);
	}

	@Override
	public StorePerson getStorePersonByName(String name, HttpServletRequest request) {
		return storePersonRepository.findByNickname(name).get();
	}

	@Override
	public StorePerson getStorePersonById(Long id, HttpServletRequest request) {
		return storePersonRepository.findById(id).get();
	}

	@Override
	public boolean deleteStorePerson(StorePerson storePerson, HttpServletRequest request) {
		/**
		 * Delete a storePerson if a storePerson is deletable
		 */
		if(allowDeleteStorePerson(storePerson) == false) {
			throw new InvalidOperationException("Deleting this storePerson is not allowed");
		}
		storePersonRepository.delete(storePerson);
		return true;
	}
	
	private boolean allowDeleteStorePerson(StorePerson storePerson) {
		/**
		 * Code to check if a storePerson is deletable
		 * Returns false if not
		 */
		return false;
	}
}
