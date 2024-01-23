/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.service.ClinicService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.PatientService;
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
public class ClinicResource {
	
	private final ClinicRepository clinicRepository;
	private final ClinicService clinicService;
	

	@GetMapping("/clinics")
	public ResponseEntity<List<Clinic>>getClinics(HttpServletRequest request){
		return ResponseEntity.ok().body(clinicService.getClinics(request));
	}
	
	@GetMapping("/clinics/get")
	public ResponseEntity<Clinic> getClinic(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(clinicService.getClinicById(id, request));
	}
	
	@GetMapping("/clinics/get_names")
	public ResponseEntity<List<String>> getClinicNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = clinicService.getNames(request);
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/clinics/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Clinic>save(
			@RequestBody Clinic clinic,
			HttpServletRequest request){
		clinic.setName(clinic.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/clinics/save").toUriString());
		return ResponseEntity.created(uri).body(clinicService.save(clinic, request));
	}
	
	@GetMapping("/clinics/get_consultation_fee")
	public ResponseEntity<Double> getConsultationFee(
			@RequestParam(name = "clinic_name") String clinicName,
			HttpServletRequest request){
		Clinic d = clinicRepository.findByName(clinicName).get();
		
		return ResponseEntity.ok().body(d.getConsultationFee());
	}
	
	@GetMapping("/clinics/load_clinics_like")
	public ResponseEntity<List<Clinic>> getClinicNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Clinic> clinics = new ArrayList<Clinic>();
		clinics = clinicRepository.findAllByNameContainingOrCodeContaining(value, value);
		return ResponseEntity.ok().body(clinics);
	}
}
