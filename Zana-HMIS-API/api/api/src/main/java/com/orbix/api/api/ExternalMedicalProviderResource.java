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

import com.orbix.api.domain.ExternalMedicalProvider;
import com.orbix.api.domain.Patient;
import com.orbix.api.repositories.ExternalMedicalProviderRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.ExternalMedicalProviderService;
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
public class ExternalMedicalProviderResource {
	private final ExternalMedicalProviderRepository externalMedicalProviderRepository;
	private final ExternalMedicalProviderService externalMedicalProviderService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/external_medical_providers")
	public ResponseEntity<List<ExternalMedicalProvider>>getExternalMedicalProviders(HttpServletRequest request){
		return ResponseEntity.ok().body(externalMedicalProviderService.getExternalMedicalProviders(request));
	}
	
	@GetMapping("/external_medical_providers/get")
	public ResponseEntity<ExternalMedicalProvider> getExternalMedicalProvider(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(externalMedicalProviderService.getExternalMedicalProviderById(id, request));
	}
	
	@GetMapping("/external_medical_providers/get_names")
	public ResponseEntity<List<String>> getExternalMedicalProviderNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = externalMedicalProviderRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/external_medical_providers/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<ExternalMedicalProvider>save(
			@RequestBody ExternalMedicalProvider externalMedicalProvider,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/external_medical_providers/save").toUriString());
		return ResponseEntity.created(uri).body(externalMedicalProviderService.save(externalMedicalProvider, request));
	}
	
	@GetMapping("/external_medical_providers/load_external_medical_providers_like")
	public ResponseEntity<List<ExternalMedicalProvider>> getProvidersLike(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<ExternalMedicalProvider> externalMedicalProviders = new ArrayList<ExternalMedicalProvider>();
		
		externalMedicalProviders = externalMedicalProviderRepository.findAllByNameContaining(value);
		return ResponseEntity.ok().body(externalMedicalProviders);
	}
}

