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

import com.orbix.api.domain.Cashier;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.CashierRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.CashierService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class CashierResource {

	private final CashierRepository cashierRepository;
	private final CashierService cashierService;
	private final UserRepository userRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/cashiers")
	public ResponseEntity<List<Cashier>>getCashiers(HttpServletRequest request){
		return ResponseEntity.ok().body(cashierService.getCashiers(request));
	}
	
	@GetMapping("/cashiers/get_all_active")
	public ResponseEntity<List<Cashier>>getActiveCashiers(HttpServletRequest request){
		return ResponseEntity.ok().body(cashierService.getActiveCashiers(request));
	}
	
	@GetMapping("/cashiers/get")
	public ResponseEntity<Cashier> getCashier(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(cashierService.getCashierById(id, request));
	}
	
	@PostMapping("/cashiers/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Cashier>save(
			@RequestBody Cashier cashier,
			HttpServletRequest request){
		//cashier.setFirstName(Sanitizer.sanitizeString(cashier.getFirstName()));
		//cashier.setMiddleName(Sanitizer.sanitizeString(cashier.getMiddleName()));
		//cashier.setLastName(Sanitizer.sanitizeString(cashier.getLastName()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/cashiers/save").toUriString());
		return ResponseEntity.created(uri).body(cashierService.save(cashier, request));
	}
	
	@GetMapping("/cashiers/load_cashier_by_username")    // to do later
	public ResponseEntity<Long> loadCashierByUsername(
			@RequestParam(name = "username") String username,
			HttpServletRequest request){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new NotFoundException("Usrr not found");
		}
		Optional<Cashier> c = cashierRepository.findByUser(user.get());
		if(!c.isPresent()) {
			throw new NotFoundException("User Account not associated with cashier");
		}		
		return ResponseEntity.ok().body(c.get().getId());
	}
	
	@PostMapping("/cashiers/assign_user_profile")    // to do later
	public ResponseEntity<Cashier> assignUserProfile(
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		Optional<Cashier> c = cashierRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Cashier not found in database");
		}
		Optional<User> u = userRepository.findByCode(code);
		if(!u.isPresent()) {
			throw new NotFoundException("User not found in database");
		}
		Optional<Cashier> cu = cashierRepository.findByUser(u.get());
		if(cu.isPresent()) {
			throw new NotFoundException("Can not asign user account to multiple cashiers");
		}
		c.get().setUser(u.get());
		return ResponseEntity.ok().body(cashierRepository.save(c.get()));
	}
}
