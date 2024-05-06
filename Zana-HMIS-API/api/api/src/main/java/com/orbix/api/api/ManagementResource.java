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

import com.orbix.api.domain.Management;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ManagementRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.ManagementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class ManagementResource {
	private final ManagementRepository managementRepository;
	private final ManagementService managementService;
	private final UserRepository userRepository;
	
	
	@GetMapping("/managements")
	public ResponseEntity<List<Management>>getManagements(HttpServletRequest request){
		return ResponseEntity.ok().body(managementService.getManagements(request));
	}
	
	@GetMapping("/managements/get_all_active")
	public ResponseEntity<List<Management>>getActiveManagements(HttpServletRequest request){
		return ResponseEntity.ok().body(managementService.getActiveManagements(request));
	}
	
	@GetMapping("/managements/get")
	public ResponseEntity<Management> getManagement(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(managementService.getManagementById(id, request));
	}
	
	@PostMapping("/managements/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Management>save(
			@RequestBody Management management,
			HttpServletRequest request){
		
		Optional<User> u = userRepository.findByCode(management.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		//management.setFirstName(Sanitizer.sanitizeString(management.getFirstName()));
		//management.setMiddleName(Sanitizer.sanitizeString(management.getMiddleName()));
		//management.setLastName(Sanitizer.sanitizeString(management.getLastName()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/managements/save").toUriString());
		return ResponseEntity.created(uri).body(managementService.save(management, request));
	}
	
	@GetMapping("/managements/load_management_by_username")    // to do later
	public ResponseEntity<Long> loadManagementByUsername(
			@RequestParam(name = "username") String username,
			HttpServletRequest request){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new NotFoundException("User Not found");
		}
		
		Optional<Management> c = managementRepository.findByUser(user.get());
		if(!c.isPresent()) {
			throw new NotFoundException("User Account not associated with management");
		}		
		return ResponseEntity.ok().body(c.get().getId());
	}
	
	@PostMapping("/managements/assign_user_profile")    // to do later
	public ResponseEntity<Management> assignUserProfile(
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		Optional<Management> c = managementRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Management not found in database");
		}
		Optional<User> u = userRepository.findByCode(code);
		if(!u.isPresent()) {
			throw new NotFoundException("User not found in database");
		}
		Optional<Management> cu = managementRepository.findByUser(u.get());
		if(cu.isPresent()) {
			throw new NotFoundException("Can not aasign user account to multiple managements");
		}
		c.get().setUser(u.get());
		return ResponseEntity.ok().body(managementRepository.save(c.get()));
	}
}
