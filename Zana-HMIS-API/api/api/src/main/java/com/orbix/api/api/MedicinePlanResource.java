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

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.repositories.MedicineInsurancePlanRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsuranceProviderService;
import com.orbix.api.service.MedicinePlanService;
import com.orbix.api.service.MedicineService;
import com.orbix.api.service.ProcedureTypeService;
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
public class MedicinePlanResource {
	private final InsuranceProviderService insuranceProviderService;
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final MedicineInsurancePlanRepository medicineInsurancePlanRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final MedicineRepository medicineRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/medicine_insurance_plans")
	public ResponseEntity<List<MedicineInsurancePlan>>getMedicineInsurancePlans(
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineInsurancePlanRepository.findAll());
	}
	
	@GetMapping("/medicine_insurance_plans/get")
	public ResponseEntity<MedicineInsurancePlan> getMedicineInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineInsurancePlanRepository.findById(id).get());
	}
	
	@PostMapping("/medicine_insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<MedicineInsurancePlan>save(
			@RequestBody MedicineInsurancePlan medicinePlan,
			HttpServletRequest request){
		MedicineInsurancePlan conPlan = new MedicineInsurancePlan();
		conPlan.setId(medicinePlan.getId());
		
		InsurancePlan plan = insurancePlanRepository.findByName(medicinePlan.getInsurancePlan().getName()).get();
		Medicine medicine = medicineRepository.findByName(medicinePlan.getMedicine().getName()).get();
		conPlan.setInsurancePlan(plan);
		conPlan.setMedicine(medicine);
		conPlan.setPrice(medicinePlan.getPrice());
		
		if(medicinePlan.getId() == null) {
			if(medicineInsurancePlanRepository.findByInsurancePlanAndMedicine(plan, medicine).isPresent()) {
				throw new InvalidOperationException("Could not create plan, a similar plan already exist. Please consider ediiting the existing plan");
			}
		}
		
		if(conPlan.getId() == null) {
			conPlan.setCreatedBy(userService.getUser(request).getId());
			conPlan.setCreatedOn(dayService.getDay().getId());
			conPlan.setCreatedAt(dayService.getTimeStamp());
			
			conPlan.setActive(true);
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/medicine_insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(medicineInsurancePlanRepository.saveAndFlush(conPlan));
	}
	
	@PostMapping("/medicine_insurance_plans/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Boolean>delete(
			@RequestParam Long id,
			HttpServletRequest request){
		
		
		MedicineInsurancePlan plan = medicineInsurancePlanRepository.findById(id).get();
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/medicine_insurance_plans/delete").toUriString());
		medicineInsurancePlanRepository.delete(plan);
		return ResponseEntity.created(uri).body(true);
		
	}
}
