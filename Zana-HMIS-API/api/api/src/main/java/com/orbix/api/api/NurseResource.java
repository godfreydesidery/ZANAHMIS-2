/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.NurseRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.NurseService;
import lombok.RequiredArgsConstructor;

/**
 * @author Godfrey
 *
 */
@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class NurseResource {
	private final NurseRepository nurseRepository;
	private final NurseService nurseService;
	private final UserRepository userRepository;
	
	
	@GetMapping("/nurses")
	public ResponseEntity<List<Nurse>>getNurses(HttpServletRequest request){
		return ResponseEntity.ok().body(nurseService.getNurses(request));
	}
	
	@GetMapping("/nurses/get_all_active")
	public ResponseEntity<List<Nurse>>getActiveNurses(HttpServletRequest request){
		return ResponseEntity.ok().body(nurseService.getActiveNurses(request));
	}
	
	@GetMapping("/nurses/get")
	public ResponseEntity<Nurse> getNurse(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(nurseService.getNurseById(id, request));
	}
	
	@PostMapping("/nurses/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Nurse>save(
			@RequestBody Nurse nurse,
			HttpServletRequest request){
		
		Optional<User> u = userRepository.findByCode(nurse.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		//nurse.setFirstName(Sanitizer.sanitizeString(nurse.getFirstName()));
		//nurse.setMiddleName(Sanitizer.sanitizeString(nurse.getMiddleName()));
		//nurse.setLastName(Sanitizer.sanitizeString(nurse.getLastName()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/nurses/save").toUriString());
		return ResponseEntity.created(uri).body(nurseService.save(nurse, request));
	}
	
	@GetMapping("/nurses/load_nurse_by_username")    // to do later
	public ResponseEntity<Long> loadNurseByUsername(
			@RequestParam(name = "username") String username,
			HttpServletRequest request){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new NotFoundException("User Not found");
		}
		
		Optional<Nurse> c = nurseRepository.findByUser(user.get());
		if(!c.isPresent()) {
			throw new NotFoundException("User Account not associated with nurse");
		}		
		return ResponseEntity.ok().body(c.get().getId());
	}
	
	@PostMapping("/nurses/assign_user_profile")    // to do later
	public ResponseEntity<Nurse> assignUserProfile(
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		Optional<Nurse> c = nurseRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Nurse not found in database");
		}
		Optional<User> u = userRepository.findByCode(code);
		if(!u.isPresent()) {
			throw new NotFoundException("User not found in database");
		}
		Optional<Nurse> cu = nurseRepository.findByUser(u.get());
		if(cu.isPresent()) {
			throw new NotFoundException("Can not aasign user account to multiple nurses");
		}
		c.get().setUser(u.get());
		return ResponseEntity.ok().body(nurseRepository.save(c.get()));
	}
}
