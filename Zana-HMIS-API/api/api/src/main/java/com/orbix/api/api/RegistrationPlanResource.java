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

import com.orbix.api.domain.RegistrationInsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.repositories.RegistrationInsurancePlanRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsuranceProviderService;
import com.orbix.api.service.RegistrationPlanService;
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
public class RegistrationPlanResource {
	private final RegistrationInsurancePlanRepository registrationInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/registration_insurance_plans")
	public ResponseEntity<List<RegistrationInsurancePlan>>getRegistrationInsurancePlans(
			HttpServletRequest request){
		return ResponseEntity.ok().body(registrationInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/registration_insurance_plans/get")
	public ResponseEntity<RegistrationInsurancePlan> getRegistrationInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(registrationInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/registration_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<RegistrationInsurancePlan>save(
			@RequestBody RegistrationInsurancePlan registrationPlan,
			HttpServletRequest request){
		RegistrationInsurancePlan regPlan = new RegistrationInsurancePlan();
		regPlan.setId(registrationPlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(registrationPlan.getInsurancePlan().getName()).get();
		regPlan.setInsurancePlan(plan);
		regPlan.setRegistrationFee(registrationPlan.getRegistrationFee());
		
		if(registrationPlan.getId() == null) {
			if(registrationInsurancePlanRepository.findByInsurancePlan(plan).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider ediiting the existing plan");
			}
		}
		
		
		
		if(regPlan.getId() == null) {
			regPlan.setCreatedby(userService.getUser(request).getId());
			regPlan.setCreatedOn(dayService.getDay().getId());
			regPlan.setCreatedAt(dayService.getTimeStamp());
			
			regPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/registration_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(registrationInsurancePlanRepository.saveAndFlush(regPlan));
	}
	
	@PostMapping("/registration_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		
		RegistrationInsurancePlan plan = registrationInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/registration_insurance_plans/delete").toUriString());
		registrationInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}
