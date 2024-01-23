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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.DiagnosisTypeRepository;
import com.orbix.api.service.InsurancePlanService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.DiagnosisTypeService;

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
public class DiagnosisTypeResource {
	private final DiagnosisTypeRepository diagnosisTypeRepository;
	private final DiagnosisTypeService diagnosisTypeService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/diagnosis_types")
	public ResponseEntity<List<DiagnosisType>>getDiagnosisTypes(HttpServletRequest request){
		return ResponseEntity.ok().body(diagnosisTypeService.getDiagnosisTypes(request));
	}
	
	@GetMapping("/diagnosis_types/get")
	public ResponseEntity<DiagnosisType> getDiagnosisType(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(diagnosisTypeService.getDiagnosisTypeById(id, request));
	}
	
	@GetMapping("/diagnosis_types/get_names")
	public ResponseEntity<List<String>> getDiagnosisTypeNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = diagnosisTypeRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	
	
	@PostMapping("/diagnosis_types/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<DiagnosisType>save(
			@RequestBody DiagnosisType diagnosisType,
			HttpServletRequest request){
		diagnosisType.setName(diagnosisType.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/diagnosis_types/save").toUriString());
		return ResponseEntity.created(uri).body(diagnosisTypeService.save(diagnosisType, request));
	}
	
	@GetMapping("/diagnosis_types/load_diagnosis_types_like")
	public ResponseEntity<List<DiagnosisType>> getDiagnosisTypeNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<DiagnosisType> diagnosisTypes = new ArrayList<DiagnosisType>();
		diagnosisTypes = diagnosisTypeRepository.findAllByNameContainingOrCodeContaining(value, value);
		return ResponseEntity.ok().body(diagnosisTypes);
	}
}


