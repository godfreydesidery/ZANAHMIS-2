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

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.RadiologyTypeInsurancePlanRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsuranceProviderService;
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
public class RadiologyPlanResource {
	private final RadiologyTypeInsurancePlanRepository radiologyTypeInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final RadiologyTypeRepository radiologyTypeRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/radiology_type_insurance_plans")
	public ResponseEntity<List<RadiologyTypeInsurancePlan>>getRadiologyTypeInsurancePlans(
			HttpServletRequest request){
		return ResponseEntity.ok().body(radiologyTypeInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/radiology_type_insurance_plans/get")
	public ResponseEntity<RadiologyTypeInsurancePlan> getRadiologyTypeInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(radiologyTypeInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/radiology_type_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<RadiologyTypeInsurancePlan>save(
			@RequestBody RadiologyTypeInsurancePlan radiologyTypePlan,
			HttpServletRequest request){
		RadiologyTypeInsurancePlan conPlan = new RadiologyTypeInsurancePlan();
		conPlan.setId(radiologyTypePlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(radiologyTypePlan.getInsurancePlan().getName()).get();
		RadiologyType radiologyType = radiologyTypeRepository.findByName(radiologyTypePlan.getRadiologyType().getName()).get();
		conPlan.setInsurancePlan(plan);
		conPlan.setRadiologyType(radiologyType);
		conPlan.setPrice(radiologyTypePlan.getPrice());
		
		if(radiologyTypePlan.getId() == null) {
			if(radiologyTypeInsurancePlanRepository.findByInsurancePlanAndRadiologyType(plan, radiologyType).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider ediiting the existing plan");
			}
		}
		
		if(conPlan.getId() == null) {
			conPlan.setCreatedBy(userService.getUser(request).getId());
			conPlan.setCreatedOn(dayService.getDay().getId());
			conPlan.setCreatedAt(dayService.getTimeStamp());
			
			conPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/radiology_type_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(radiologyTypeInsurancePlanRepository.saveAndFlush(conPlan));
	}
	
	@PostMapping("/radiology_type_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		
		RadiologyTypeInsurancePlan plan = radiologyTypeInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/radiologyType_insurance_plans/delete").toUriString());
		radiologyTypeInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}
