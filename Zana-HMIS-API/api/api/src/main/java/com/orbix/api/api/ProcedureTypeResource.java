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

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.ProcedureTypeService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.ProcedureTypeService;

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
public class ProcedureTypeResource {
	private final ProcedureTypeRepository procedureTypeRepository;
	private final ProcedureTypeService procedureTypeService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/procedure_types")
	public ResponseEntity<List<ProcedureType>>getProcedureTypes(
			HttpServletRequest request){
		return ResponseEntity.ok().body(procedureTypeService.getProcedureTypes(request));
	}
	
	@GetMapping("/procedure_types/get")
	public ResponseEntity<ProcedureType> getProcedureType(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(procedureTypeService.getProcedureTypeById(id, request));
	}
	
	@GetMapping("/procedure_types/get_names")
	public ResponseEntity<List<String>> getProcedureTypeNames(
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = procedureTypeRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/procedure_types/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<ProcedureType>save(
			@RequestBody ProcedureType procedureType,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/procedure_types/save").toUriString());
		return ResponseEntity.created(uri).body(procedureTypeService.save(procedureType, request));
	}
	
	@GetMapping("/procedure_types/load_procedure_types_like")
	public ResponseEntity<List<ProcedureType>> getProcedureTypeNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<ProcedureType> procedureTypes = new ArrayList<ProcedureType>();
		procedureTypes = procedureTypeRepository.findAllByNameContaining(value);
		return ResponseEntity.ok().body(procedureTypes);
	}
}
