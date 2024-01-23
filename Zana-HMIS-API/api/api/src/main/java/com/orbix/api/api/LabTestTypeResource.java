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
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.service.ClinicService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.LabTestTypeService;
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
public class LabTestTypeResource {
	private final LabTestTypeRepository labTestTypeRepository;
	private final LabTestTypeService labTestTypeService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/lab_test_types")
	public ResponseEntity<List<LabTestType>>getLabTestTypes(
			HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeService.getLabTestTypes(request));
	}
	
	@GetMapping("/lab_test_types/get")
	public ResponseEntity<LabTestType> getLabTestType(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeService.getLabTestTypeById(id, request));
	}
	
	@GetMapping("/lab_test_types/get_names")
	public ResponseEntity<List<String>> getLabTestTypeNames(
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = labTestTypeRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/lab_test_types/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<LabTestType>save(
			@RequestBody LabTestType labTestType,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/lab_test_types/save").toUriString());
		return ResponseEntity.created(uri).body(labTestTypeService.save(labTestType, request));
	}
	
	@GetMapping("/lab_test_types/load_lab_test_types_like")
	public ResponseEntity<List<LabTestType>> getLabTestTypeNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<LabTestType> labTestTypes = new ArrayList<LabTestType>();
		labTestTypes = labTestTypeRepository.findAllByNameContaining(value);
		return ResponseEntity.ok().body(labTestTypes);
	}
}
