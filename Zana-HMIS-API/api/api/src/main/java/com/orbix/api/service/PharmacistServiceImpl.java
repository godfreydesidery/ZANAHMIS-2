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
import com.orbix.api.domain.Pharmacist;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.PharmacistRepository;
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
public class PharmacistServiceImpl implements PharmacistService{

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacistRepository pharmacistRepository;
	
	@Override
	public Pharmacist save(Pharmacist pharmacist, HttpServletRequest request) {
		
		Optional<User> u = userRepository.findByCode(pharmacist.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		if(!u.get().getFirstName().equals(pharmacist.getFirstName()) || !u.get().getMiddleName().equals(pharmacist.getMiddleName()) || !u.get().getLastName().equals(pharmacist.getLastName())){
			throw new InvalidOperationException("Provided names do not match with user account");
		}
		pharmacist.setNickname(u.get().getNickname());
		pharmacist.setUser(u.get());

		pharmacist.setNickname(pharmacist.getFirstName()+ " "+pharmacist.getMiddleName()+ " "+pharmacist.getLastName()+" "+pharmacist.getCode());
		
		if(pharmacist.getId() == null) {
			pharmacist.setCreatedby(userService.getUser(request).getId());
			pharmacist.setCreatedOn(dayService.getDay().getId());
			pharmacist.setCreatedAt(dayService.getTimeStamp());
			
			pharmacist.setActive(true);
		}
		log.info("Saving new clinic to the database");
		return pharmacistRepository.save(pharmacist);
	}

	@Override
	public List<Pharmacist> getPharmacists(HttpServletRequest request) {
		log.info("Fetching all pharmacists");
		return pharmacistRepository.findAll();
	}
	
	@Override
	public List<Pharmacist> getActivePharmacists(HttpServletRequest request) {
		log.info("Fetching all pharmacists");
		return pharmacistRepository.findAllByActive(true);
	}

	@Override
	public Pharmacist getPharmacistByName(String name, HttpServletRequest request) {
		return pharmacistRepository.findByNickname(name).get();
	}

	@Override
	public Pharmacist getPharmacistById(Long id, HttpServletRequest request) {
		return pharmacistRepository.findById(id).get();
	}

	@Override
	public boolean deletePharmacist(Pharmacist pharmacist, HttpServletRequest request) {
		/**
		 * Delete a pharmacist if a pharmacist is deletable
		 */
		if(allowDeletePharmacist(pharmacist) == false) {
			throw new InvalidOperationException("Deleting this pharmacist is not allowed");
		}
		pharmacistRepository.delete(pharmacist);
		return true;
	}
	
	private boolean allowDeletePharmacist(Pharmacist pharmacist) {
		/**
		 * Code to check if a pharmacist is deletable
		 * Returns false if not
		 */
		return false;
	}
}
