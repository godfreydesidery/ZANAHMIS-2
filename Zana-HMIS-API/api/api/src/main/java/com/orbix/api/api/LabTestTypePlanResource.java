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

import com.orbix.api.domain.LabTestTypeInsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.repositories.LabTestTypeInsurancePlanRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
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
public class LabTestTypePlanResource {
	private final InsuranceProviderService insuranceProviderService;
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final LabTestTypeInsurancePlanRepository labTestTypeInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final LabTestTypeRepository labTestTypeRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/lab_test_type_insurance_plans")
	public ResponseEntity<List<LabTestTypeInsurancePlan>>getLabTestTypeInsurancePlans(HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/lab_test_type_insurance_plans/get")
	public ResponseEntity<LabTestTypeInsurancePlan> getLabTestTypeInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/lab_test_type_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<LabTestTypeInsurancePlan>save(
			@RequestBody LabTestTypeInsurancePlan labTestTypePlan,
			HttpServletRequest request){
		LabTestTypeInsurancePlan conPlan = new LabTestTypeInsurancePlan();
		conPlan.setId(labTestTypePlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(labTestTypePlan.getInsurancePlan().getName()).get();
		LabTestType labTestType = labTestTypeRepository.findByName(labTestTypePlan.getLabTestType().getName()).get();
		conPlan.setInsurancePlan(plan);
		conPlan.setLabTestType(labTestType);
		conPlan.setPrice(labTestTypePlan.getPrice());
		
		if(labTestTypePlan.getId() == null) {
			if(labTestTypeInsurancePlanRepository.findByInsurancePlanAndLabTestType(plan, labTestType).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider ediiting the existing plan");
			}
		}
		
		
		
		if(conPlan.getId() == null) {
			conPlan.setCreatedBy(userService.getUser(request).getId());
			conPlan.setCreatedOn(dayService.getDay().getId());
			conPlan.setCreatedAt(dayService.getTimeStamp());
			
			conPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/lab_test_type_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(labTestTypeInsurancePlanRepository.saveAndFlush(conPlan));
	}
	
	@PostMapping("/lab_test_type_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		
		LabTestTypeInsurancePlan plan = labTestTypeInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/labTest_type_insurance_plans/delete").toUriString());
		labTestTypeInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}
