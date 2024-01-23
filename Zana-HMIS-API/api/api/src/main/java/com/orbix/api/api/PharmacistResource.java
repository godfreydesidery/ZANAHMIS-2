/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Pharmacist;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.PharmacistRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.ClinicService;
import com.orbix.api.service.PharmacistService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

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
public class PharmacistResource {

	private final PharmacistRepository pharmacistRepository;
	private final ClinicRepository clinicRepository;
	private final PharmacistService pharmacistService;
	private final ClinicService clinicService;
	private final UserRepository userRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/pharmacists")
	public ResponseEntity<List<Pharmacist>>getPharmacists(HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacistService.getPharmacists(request));
	}
	
	@GetMapping("/pharmacists/get_all_active")
	public ResponseEntity<List<Pharmacist>>getActivePharmacists(HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacistService.getActivePharmacists(request));
	}
	
	@GetMapping("/pharmacists/get")
	public ResponseEntity<Pharmacist> getPharmacist(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacistService.getPharmacistById(id, request));
	}
	
	@PostMapping("/pharmacists/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Pharmacist>save(
			@RequestBody Pharmacist pharmacist,
			HttpServletRequest request){
		//pharmacist.setFirstName(Sanitizer.sanitizeString(pharmacist.getFirstName()));
		//pharmacist.setMiddleName(Sanitizer.sanitizeString(pharmacist.getMiddleName()));
		//pharmacist.setLastName(Sanitizer.sanitizeString(pharmacist.getLastName()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/pharmacists/save").toUriString());
		return ResponseEntity.created(uri).body(pharmacistService.save(pharmacist, request));
	}
	
	@GetMapping("/pharmacists/load_pharmacist_by_username")    // to do later
	public ResponseEntity<Long> loadPharmacistByUsername(
			@RequestParam(name = "username") String username,
			HttpServletRequest request){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new NotFoundException("Usrr not found");
		}
		Optional<Pharmacist> c = pharmacistRepository.findByUser(user.get());
		if(!c.isPresent()) {
			throw new NotFoundException("User Account not associated with pharmacist");
		}		
		return ResponseEntity.ok().body(c.get().getId());
	}
	
	@PostMapping("/pharmacists/assign_user_profile")    // to do later
	public ResponseEntity<Pharmacist> assignUserProfile(
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		Optional<Pharmacist> c = pharmacistRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Pharmacist not found in database");
		}
		Optional<User> u = userRepository.findByCode(code);
		if(!u.isPresent()) {
			throw new NotFoundException("User not found in database");
		}
		Optional<Pharmacist> cu = pharmacistRepository.findByUser(u.get());
		if(cu.isPresent()) {
			throw new NotFoundException("Can not aasign user account to multiple pharmacists");
		}
		c.get().setUser(u.get());
		return ResponseEntity.ok().body(pharmacistRepository.save(c.get()));
	}
}
