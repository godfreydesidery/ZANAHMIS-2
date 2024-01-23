/**
 * 
 */
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

import com.orbix.api.domain.Ward;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.WardCategory;
import com.orbix.api.domain.WardType;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.repositories.WardCategoryRepository;
import com.orbix.api.repositories.WardRepository;
import com.orbix.api.repositories.WardTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.WardService;

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
public class WardResource {
	private final WardRepository wardRepository;
	private final WardService wardService;
	private final WardBedRepository wardBedRepository;
	
	private final WardCategoryRepository wardCategoryRepository;
	private final WardTypeRepository wardTypeRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/wards")
	public ResponseEntity<List<Ward>>getWards(HttpServletRequest request){
		return ResponseEntity.ok().body(wardService.getWards(request));
	}
	
	@GetMapping("/wards/get")
	public ResponseEntity<Ward> getWard(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardService.getWardById(id, request));
	}
	
	@GetMapping("/wards/get_by_name")
	public ResponseEntity<Ward> getWardByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(wardService.getWardByName(name, request));
	}
	
	@GetMapping("/wards/get_wards_by_category_and_type")
	public ResponseEntity<List<Ward>> getWardByCategoryAndType(
			@RequestParam(name = "category_id") Long categoryId,
			@RequestParam(name = "type_id") Long typeId,
			HttpServletRequest request){
		
		Optional<WardCategory> c = wardCategoryRepository.findById(categoryId);		
		if(c.isEmpty()) {
			throw new NotFoundException("Category not found");
		}
		
		Optional<WardType> t = wardTypeRepository.findById(typeId);		
		if(t.isEmpty()) {
			throw new NotFoundException("Type not found");
		}
		
		List<Ward> wards = wardRepository.findAllByWardCategoryAndWardType(c.get(), t.get());
		
		return ResponseEntity.ok().body(wards);
	}
	
	@GetMapping("/wards/get_available_beds_by_ward")
	public ResponseEntity<List<WardBed>> geAvailableBedsByWard(
			@RequestParam(name = "ward_id") Long wardId,
			HttpServletRequest request){
		
		Optional<Ward> w = wardRepository.findById(wardId);		
		if(w.isEmpty()) {
			throw new NotFoundException("Ward not found");
		}
		
		List<WardBed> wardBeds = wardBedRepository.findAllByWardAndStatusAndActive(w.get(), "EMPTY", true);
		
		return ResponseEntity.ok().body(wardBeds);
	}
	
	@PostMapping("/wards/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Ward>save(
			@RequestBody Ward ward,
			HttpServletRequest request){
		ward.setName(ward.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/wards/save").toUriString());
		return ResponseEntity.created(uri).body(wardService.save(ward, request));
	}
	
	@PostMapping("/wards/register_bed")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardBed>registerBed(
			@RequestBody WardBed wardBed,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/wards/register_bed").toUriString());
		return ResponseEntity.created(uri).body(wardService.registerBed(wardBed, request));
	}
	
	@GetMapping("/wards/get_ward_beds")
	public ResponseEntity<List<WardBed>> getWardBeds(
			@RequestParam(name = "ward_id") Long wardId,
			HttpServletRequest request){
		Optional<Ward> ward = wardRepository.findById(wardId);
		if(ward.isEmpty()) {
			throw new NotFoundException("Ward not found");
		}
		List<WardBed> wardBeds = wardBedRepository.findAllByWard(ward.get());
		return ResponseEntity.ok().body(wardBeds);
	}
	
	@GetMapping("/wards/get_ward_bed")
	public ResponseEntity<WardBed> getWardBed(
			@RequestParam(name = "bed_id") Long bedId,
			HttpServletRequest request){
		Optional<WardBed> wardBed = wardBedRepository.findById(bedId);
		if(wardBed.isEmpty()) {
			throw new NotFoundException("Bed/Room not found");
		}
		return ResponseEntity.ok().body(wardBed.get());
	}
	
	@PostMapping("/wards/update_bed")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardBed>updateBed(
			@RequestBody WardBed wardBed,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/wards/activate_bed").toUriString());
		return ResponseEntity.created(uri).body(wardService.updateBed(wardBed, request));
	}
	
	@PostMapping("/wards/activate_bed")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardBed>activateBed(
			@RequestBody WardBed wardBed,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/wards/activate_bed").toUriString());
		return ResponseEntity.created(uri).body(wardService.activateBed(wardBed, request));
	}
	
	@PostMapping("/wards/deactivate_bed")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<WardBed>deactivateBed(
			@RequestBody WardBed wardBed,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/wards/deactivate_bed").toUriString());
		return ResponseEntity.created(uri).body(wardService.deactivateBed(wardBed, request));
	}
	
	
	
}
