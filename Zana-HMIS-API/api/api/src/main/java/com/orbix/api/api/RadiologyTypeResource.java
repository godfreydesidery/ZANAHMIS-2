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

import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.RadiologyTypeService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.RadiologyTypeService;

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
public class RadiologyTypeResource {
	private final RadiologyTypeRepository radiologyTypeRepository;
	private final RadiologyTypeService radiologyTypeService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/radiology_types")
	public ResponseEntity<List<RadiologyType>>getRadiologyTypes(
			HttpServletRequest request){
		return ResponseEntity.ok().body(radiologyTypeService.getRadiologyTypes(request));
	}
	
	@GetMapping("/radiology_types/get")
	public ResponseEntity<RadiologyType> getRadiologyType(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(radiologyTypeService.getRadiologyTypeById(id, request));
	}
	
	@GetMapping("/radiology_types/get_names")
	public ResponseEntity<List<String>> getRadiologyTypeNames(
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = radiologyTypeRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/radiology_types/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<RadiologyType>save(
			@RequestBody RadiologyType radiologyType,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/radiology_types/save").toUriString());
		return ResponseEntity.created(uri).body(radiologyTypeService.save(radiologyType, request));
	}
	
	@GetMapping("/radiology_types/load_radiology_types_like")
	public ResponseEntity<List<RadiologyType>> getRadiologyTypeNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<RadiologyType> radiologyTypes = new ArrayList<RadiologyType>();
		radiologyTypes = radiologyTypeRepository.findAllByNameContaining(value);
		return ResponseEntity.ok().body(radiologyTypes);
	}
}
