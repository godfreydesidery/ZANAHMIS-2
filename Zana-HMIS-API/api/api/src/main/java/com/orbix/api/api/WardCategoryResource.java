/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.WardCategory;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.WardCategoryRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.WardCategoryService;
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
public class WardCategoryResource {
	private final WardCategoryRepository wardCategoryRepository;
	private final WardCategoryService wardCategoryService;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/ward_categories")
	public ResponseEntity<List<WardCategory>>getWardCategories(HttpServletRequest request){
		return ResponseEntity.ok().body(wardCategoryService.getWardCategories(request));
	}
	
	@GetMapping("/ward_categories/get")
	public ResponseEntity<WardCategory> getWardCategory(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardCategoryService.getWardCategoryById(id, request));
	}
	
	@GetMapping("/ward_categories/get_by_name")
	public ResponseEntity<WardCategory> getWardCategoryByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardCategoryService.getWardCategoryByName(name, request));
	}
	
	@PostMapping("/ward_categories/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardCategory>save(
			@RequestBody WardCategory wardCategory,
			HttpServletRequest request){
		wardCategory.setName(wardCategory.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/ward_categories/save").toUriString());
		return ResponseEntity.created(uri).body(wardCategoryService.save(wardCategory, request));
	}
	
	
	
	
}
