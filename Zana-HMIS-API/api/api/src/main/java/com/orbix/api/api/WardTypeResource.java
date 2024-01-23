/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
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

import com.orbix.api.domain.WardType;
import com.orbix.api.repositories.WardTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.WardTypeService;

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
public class WardTypeResource {
	private final WardTypeRepository wardTypeRepository;
	private final WardTypeService wardTypeService;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/ward_types")
	public ResponseEntity<List<WardType>>getWardTypes(HttpServletRequest request){
		return ResponseEntity.ok().body(wardTypeService.getWardTypes(request));
	}
	
	@GetMapping("/ward_types/get")
	public ResponseEntity<WardType> getWardType(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardTypeService.getWardTypeById(id, request));
	}
	
	@GetMapping("/ward_types/get_by_name")
	public ResponseEntity<WardType> getWardTypeByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardTypeService.getWardTypeByName(name, request));
	}
	
	@PostMapping("/ward_types/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardType>save(
			@RequestBody WardType wardType,
			HttpServletRequest request){
		wardType.setName(wardType.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/ward_types/save").toUriString());
		return ResponseEntity.created(uri).body(wardTypeService.save(wardType, request));
	}
	
	
	
	
}
