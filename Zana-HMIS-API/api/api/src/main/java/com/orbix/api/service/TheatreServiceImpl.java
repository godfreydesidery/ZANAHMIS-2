/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Theatre;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.TheatreRepository;
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
public class TheatreServiceImpl implements TheatreService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final TheatreRepository theatreRepository;
	
	@Override
	public Theatre save(Theatre theatre, HttpServletRequest request) {
		
		theatre.setName(theatre.getName());
		
		if(theatre.getId() == null) {
			theatre.setCreatedBy(userService.getUser(request).getId());
			theatre.setCreatedOn(dayService.getDay().getId());
			theatre.setCreatedAt(dayService.getTimeStamp());
			
			theatre.setActive(true);
		}
		
		
		log.info("Saving new theatre to the database");
		return theatreRepository.save(theatre);
	}

	@Override
	public List<Theatre> getTheatres(HttpServletRequest request) {
		log.info("Fetching all theatres");
		return theatreRepository.findAll();
	}

	@Override
	public Theatre getTheatreByName(String name, HttpServletRequest request) {
		return theatreRepository.findByName(name).get();
	}

	@Override
	public Theatre getTheatreById(Long id, HttpServletRequest request) {
		return theatreRepository.findById(id).get();
	}

	@Override
	public boolean deleteTheatre(Theatre theatre, HttpServletRequest request) {
		/**
		 * Delete a theatre if a theatre is deletable
		 */
		if(allowDeleteTheatre(theatre) == false) {
			throw new InvalidOperationException("Deleting this theatre is not allowed");
		}
		theatreRepository.delete(theatre);
		return true;
	}
	
	private boolean allowDeleteTheatre(Theatre theatre) {
		/**
		 * Code to check if a theatre is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public List<String> getNames(HttpServletRequest request) {
		return theatreRepository.getNames();	
	}

	@Override
	public Theatre getByName(String theatreName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return theatreRepository.findByName(theatreName).get();
	}

	
	
}
