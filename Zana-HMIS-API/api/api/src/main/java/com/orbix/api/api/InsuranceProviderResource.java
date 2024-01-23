/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.service.ClinicService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsuranceProviderService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.InsuranceProviderService;

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
public class InsuranceProviderResource {
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final InsuranceProviderService insuranceProviderService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/insurance_providers")
	public ResponseEntity<List<InsuranceProvider>>getInsuranceProviders(HttpServletRequest request){
		return ResponseEntity.ok().body(insuranceProviderService.getInsuranceProviders(request));
	}
	
	@GetMapping("/insurance_providers/get")
	public ResponseEntity<InsuranceProvider> getInsuranceProvider(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(insuranceProviderService.getInsuranceProviderById(id, request));
	}
	
	@GetMapping("/insurance_providers/get_names")
	public ResponseEntity<List<String>> getInsuranceProviderNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = insuranceProviderRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/insurance_providers/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<InsuranceProvider>save(
			@RequestBody InsuranceProvider insuranceProvider,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/insurance_providers/save").toUriString());
		return ResponseEntity.created(uri).body(insuranceProviderService.save(insuranceProvider, request));
	}
}

