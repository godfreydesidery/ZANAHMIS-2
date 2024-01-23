/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
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

import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.repositories.LabTestTypeRangeRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.LabTestTypeRangeService;
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
public class LabTestTypeRangeResource {
	private final LabTestTypeRepository labTestTypeRepository;
	private final LabTestTypeRangeRepository labTestTypeRangeRepository;
	private final LabTestTypeRangeService labTestTypeRangeService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/lab_test_type_ranges")
	public ResponseEntity<List<LabTestTypeRange>>getLabTestTypeRanges(
			@RequestParam(name = "lab_test_type_id") Long labTestTypeId,
			HttpServletRequest request){
		Optional<LabTestType> ltt = labTestTypeRepository.findById(labTestTypeId);
		if(ltt.isEmpty()) {
			throw new NotFoundException("Lab Test Type not found");
		}
		return ResponseEntity.ok().body(labTestTypeRangeService.getLabTestTypeRanges(ltt.get(), request));
	}
	
	@GetMapping("/lab_test_type_ranges/get")
	public ResponseEntity<LabTestTypeRange> getLabTestTypeRange(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeRangeService.getLabTestTypeRangeById(id, request));
	}
	
	@GetMapping("/lab_test_type_ranges/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public boolean delete(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		labTestTypeRangeRepository.deleteById(id);
		return true;
	}
	
	@GetMapping("/lab_test_type_ranges/get_names")
	public ResponseEntity<List<String>> getLabTestTypeRangeNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = labTestTypeRangeRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@GetMapping("/lab_test_type_ranges/get_names_by_insurance_provider")
	public ResponseEntity<List<String>> getLabTestTypeRangeNames(
			@RequestParam(name = "lab_test_type_name") String providerName,
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		LabTestType labTestType = labTestTypeRepository.findByName(providerName).get();
		List<LabTestTypeRange> plans = labTestTypeRangeRepository.findAllByLabTestType(labTestType);
		for(LabTestTypeRange p : plans) {
			names.add(p.getName());
		}
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/lab_test_type_ranges/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<LabTestTypeRange>save(
			@RequestBody LabTestTypeRange labTestTypeRange,
			HttpServletRequest request){
		LabTestType labTestType = labTestTypeRepository.findById(labTestTypeRange.getLabTestType().getId()).get();
		labTestTypeRange.setLabTestType(labTestType);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/lab_test_type_ranges/save").toUriString());
		return ResponseEntity.created(uri).body(labTestTypeRangeService.save(labTestTypeRange, request));
	}
}

