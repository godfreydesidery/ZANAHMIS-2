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
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.ConsultationInsurancePlan;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.ConsultationInsurancePlanRepository;
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
public class ConsultationInsurancePlanResource {
	private final InsuranceProviderService insuranceProviderService;
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final ConsultationInsurancePlanRepository consultationInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final ClinicRepository clinicRepository;

	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/consultation_insurance_plans")
	public ResponseEntity<List<ConsultationInsurancePlan>>getConsultationInsurancePlans(HttpServletRequest request){
		return ResponseEntity.ok().body(consultationInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/consultation_insurance_plans/get")
	public ResponseEntity<ConsultationInsurancePlan> getConsultationInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(consultationInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/consultation_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<ConsultationInsurancePlan>save(
			@RequestBody ConsultationInsurancePlan consultationPlan,
			HttpServletRequest request){
		ConsultationInsurancePlan conPlan = new ConsultationInsurancePlan();
		conPlan.setId(consultationPlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(consultationPlan.getInsurancePlan().getName()).get();
		Clinic clinic = clinicRepository.findByName(consultationPlan.getClinic().getName()).get();
		conPlan.setInsurancePlan(plan);
		conPlan.setClinic(clinic);
		conPlan.setConsultationFee(consultationPlan.getConsultationFee());
		
		if(consultationPlan.getId() == null) {
			if(consultationInsurancePlanRepository.findByInsurancePlanAndClinic(plan, clinic).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider editing the existing plan");
			}
		}
		
		if(conPlan.getId() == null) {
			conPlan.setCreatedBy(userService.getUser(request).getId());
			conPlan.setCreatedOn(dayService.getDay().getId());
			conPlan.setCreatedAt(dayService.getTimeStamp());
			
			conPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/consultation_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(consultationInsurancePlanRepository.saveAndFlush(conPlan));
	}
	
	@PostMapping("/consultation_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		ConsultationInsurancePlan plan = consultationInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/consultation_insurance_plans/delete").toUriString());
		consultationInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}
