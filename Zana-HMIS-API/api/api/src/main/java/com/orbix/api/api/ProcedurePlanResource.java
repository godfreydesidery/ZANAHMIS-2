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
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.ProcedureTypeInsurancePlanRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsuranceProviderService;
import com.orbix.api.service.ProcedureTypePlanService;
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
public class ProcedurePlanResource {
	private final InsuranceProviderService insuranceProviderService;
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final ProcedureTypeInsurancePlanRepository procedureTypeInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final ProcedureTypeRepository procedureTypeRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/procedure_type_insurance_plans")
	public ResponseEntity<List<ProcedureTypeInsurancePlan>>getProcedureTypeInsurancePlans(
			HttpServletRequest request){
		return ResponseEntity.ok().body(procedureTypeInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/procedure_type_insurance_plans/get")
	public ResponseEntity<ProcedureTypeInsurancePlan> getProcedureTypeInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(procedureTypeInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/procedure_type_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<ProcedureTypeInsurancePlan>save(
			@RequestBody ProcedureTypeInsurancePlan procedureTypePlan,
			HttpServletRequest request){
		ProcedureTypeInsurancePlan conPlan = new ProcedureTypeInsurancePlan();
		conPlan.setId(procedureTypePlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(procedureTypePlan.getInsurancePlan().getName()).get();
		ProcedureType procedureType = procedureTypeRepository.findByName(procedureTypePlan.getProcedureType().getName()).get();
		conPlan.setInsurancePlan(plan);
		conPlan.setProcedureType(procedureType);
		conPlan.setPrice(procedureTypePlan.getPrice());
		
		if(procedureType.getId() == null) {
			if(procedureTypeInsurancePlanRepository.findByInsurancePlanAndProcedureType(plan, procedureType).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider ediiting the existing plan");
			}
		}
		
		if(conPlan.getId() == null) {
			conPlan.setCreatedBy(userService.getUser(request).getId());
			conPlan.setCreatedOn(dayService.getDay().getId());
			conPlan.setCreatedAt(dayService.getTimeStamp());
			
			conPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/procedure_type_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(procedureTypeInsurancePlanRepository.saveAndFlush(conPlan));
	}
	
	@PostMapping("/procedure_type_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		
		ProcedureTypeInsurancePlan plan = procedureTypeInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/procedureType_insurance_plans/delete").toUriString());
		procedureTypeInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}