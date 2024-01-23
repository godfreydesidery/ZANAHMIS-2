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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.domain.ConsultationInsurancePlan;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeInsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;
import com.orbix.api.domain.RegistrationInsurancePlan;
import com.orbix.api.domain.WardType;
import com.orbix.api.domain.WardTypeInsurancePlan;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.CompanyProfileRepository;
import com.orbix.api.repositories.ConsultationInsurancePlanRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.InsuranceProviderRepository;
import com.orbix.api.repositories.LabTestTypeInsurancePlanRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.MedicineInsurancePlanRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.ProcedureTypeInsurancePlanRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.repositories.RadiologyTypeInsurancePlanRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.RegistrationInsurancePlanRepository;
import com.orbix.api.repositories.WardTypeInsurancePlanRepository;
import com.orbix.api.repositories.WardTypeRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsurancePlanService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.InsurancePlanService;

import lombok.Data;
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
public class InsurancePlanResource {
	private final InsuranceProviderRepository insuranceProviderRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final InsurancePlanService insurancePlanService;
	private final UserService userService;
	private final DayService dayService;
	
	private final LabTestTypeRepository labTestTypeRepository;
	private final LabTestTypeInsurancePlanRepository labTestTypeInsurancePlanRepository;
	
	private final ProcedureTypeRepository procedureTypeRepository;
	private final ProcedureTypeInsurancePlanRepository procedureTypeInsurancePlanRepository;
	
	private final RadiologyTypeRepository radiologyTypeRepository;
	private final RadiologyTypeInsurancePlanRepository radiologyTypeInsurancePlanRepository;
	
	private final MedicineRepository medicineRepository;
	private final MedicineInsurancePlanRepository medicineInsurancePlanRepository;
	
	private final ClinicRepository clinicRepository;
	private final ConsultationInsurancePlanRepository consultationInsurancePlanRepository;
	
	private final RegistrationInsurancePlanRepository registrationInsurancePlanRepository;
	
	private final CompanyProfileRepository companyProfileRepository;
	
	private final WardTypeRepository wardTypeRepository;
	private final WardTypeInsurancePlanRepository wardTypeInsurancePlanRepository;
	
	
	@GetMapping("/insurance_plans")
	public ResponseEntity<List<InsurancePlan>>getInsurancePlans(HttpServletRequest request){
		return ResponseEntity.ok().body(insurancePlanService.getInsurancePlans(request));
	}
	
	@GetMapping("/insurance_plans/get")
	public ResponseEntity<InsurancePlan> getInsurancePlan(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(insurancePlanService.getInsurancePlanById(id, request));
	}
	
	@GetMapping("/insurance_plans/get_names")
	public ResponseEntity<List<String>> getInsurancePlanNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = insurancePlanRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@GetMapping("/insurance_plans/get_names_by_insurance_provider")
	public ResponseEntity<List<String>> getInsurancePlanNames(
			@RequestParam(name = "provider_name") String providerName,
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		InsuranceProvider insuranceProvider = insuranceProviderRepository.findByName(providerName);
		List<InsurancePlan> plans = insurancePlanRepository.findAllByInsuranceProvider(insuranceProvider);
		for(InsurancePlan p : plans) {
			names.add(p.getName());
		}
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/insurance_plans/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<InsurancePlan>save(
			@RequestBody InsurancePlan insurancePlan,
			HttpServletRequest request){
		InsuranceProvider insuranceProvider = insuranceProviderRepository.findByName(insurancePlan.getInsuranceProvider().getName());
		insurancePlan.setInsuranceProvider(insuranceProvider);
		insurancePlan.setName(insurancePlan.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/insurance_plans/save").toUriString());
		return ResponseEntity.created(uri).body(insurancePlanService.save(insurancePlan, request));
	}
	
	@GetMapping("/insurance_plans/get_lab_test_type_cash_prices")
	public ResponseEntity<List<LabTestType>> getLabTestTypeCashPrices(
			HttpServletRequest request){
		return ResponseEntity.ok().body(labTestTypeRepository.findAll());
	}
	
	@GetMapping("/insurance_plans/get_lab_test_type_prices")
	public ResponseEntity<List<LLabTestTypePrice>> getLabTestTypePrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LLabTestTypePrice> labTestTypePrices = new ArrayList<>();
		List<LabTestType> labTestTypes = labTestTypeRepository.findAll();
		for(LabTestType t : labTestTypes) {
			LLabTestTypePrice labTestTypePrice = new LLabTestTypePrice();
			labTestTypePrice.setLabTestType(t);
			if(insurancePlanId == 0) {
				labTestTypePrice.setPrice(t.getPrice());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<LabTestTypeInsurancePlan> p = labTestTypeInsurancePlanRepository.findByLabTestTypeAndInsurancePlan(t, i.get());
				LabTestTypeInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new LabTestTypeInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setPrice(0);
					plan.setInsurancePlan(i.get());
					plan.setLabTestType(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = labTestTypeInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				labTestTypePrice.setLabTestTypeInsurancePlan(plan);
				labTestTypePrice.setPrice(plan.getPrice());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			labTestTypePrices.add(labTestTypePrice);
		}
		return ResponseEntity.ok().body(labTestTypePrices);
	}
	
	@PostMapping("/insurance_plans/change_lab_test_type_coverage")
	public ResponseEntity<LLabTestTypePrice> changeLabTestTypeCoverage(
			@RequestBody LLabTestTypePrice labTestTypePrice,
			HttpServletRequest request){
		Optional<LabTestType> tt = labTestTypeRepository.findById(labTestTypePrice.getLabTestType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected lab test type not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(labTestTypePrice.getLabTestTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = labTestTypePrice.getLabTestTypeInsurancePlan().isCovered();
		Optional<LabTestTypeInsurancePlan> lttip = labTestTypeInsurancePlanRepository.findByInsurancePlanAndLabTestType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getPrice() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		LabTestTypeInsurancePlan plan = labTestTypeInsurancePlanRepository.save(lttip.get());
		
		LLabTestTypePrice coverage = new LLabTestTypePrice();
		coverage.setLabTestType(tt.get());
		coverage.setLabTestTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_lab_test_type_price_by_insurance")
	public ResponseEntity<LLabTestTypePrice> updateLabTestTypePriceByInsurance(
			@RequestBody LLabTestTypePrice labTestTypePrice,
			HttpServletRequest request){
		Optional<LabTestType> tt = labTestTypeRepository.findById(labTestTypePrice.getLabTestType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected test type not found");
		}
		
		LLabTestTypePrice coverage = new LLabTestTypePrice();
		if(labTestTypePrice.getLabTestTypeInsurancePlan().getInsurancePlan().getId() == 0) {
			if(labTestTypePrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setPrice(labTestTypePrice.getPrice());
			LabTestType labTestType = labTestTypeRepository.save(tt.get());
			
			coverage.setLabTestType(labTestType);
			coverage.setLabTestTypeInsurancePlan(null);
			coverage.setPrice(labTestTypePrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(labTestTypePrice.getLabTestTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = labTestTypePrice.getPrice();
		Optional<LabTestTypeInsurancePlan> lttip = labTestTypeInsurancePlanRepository.findByInsurancePlanAndLabTestType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setPrice(price);
		
		LabTestTypeInsurancePlan plan = labTestTypeInsurancePlanRepository.save(lttip.get());
		
		
		coverage.setLabTestType(tt.get());
		coverage.setLabTestTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		coverage.setCovered(plan.isCovered());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	
	@GetMapping("/insurance_plans/get_procedure_type_prices")
	public ResponseEntity<List<LProcedureTypePrice>> getProcedureTypePrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LProcedureTypePrice> procedureTypePrices = new ArrayList<>();
		List<ProcedureType> procedureTypes = procedureTypeRepository.findAll();
		for(ProcedureType t : procedureTypes) {
			LProcedureTypePrice procedureTypePrice = new LProcedureTypePrice();
			procedureTypePrice.setProcedureType(t);
			if(insurancePlanId == 0) {
				procedureTypePrice.setPrice(t.getPrice());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<ProcedureTypeInsurancePlan> p = procedureTypeInsurancePlanRepository.findByProcedureTypeAndInsurancePlan(t, i.get());
				ProcedureTypeInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new ProcedureTypeInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setPrice(0);
					plan.setInsurancePlan(i.get());
					plan.setProcedureType(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = procedureTypeInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				procedureTypePrice.setProcedureTypeInsurancePlan(plan);
				procedureTypePrice.setPrice(plan.getPrice());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			procedureTypePrices.add(procedureTypePrice);
		}
		return ResponseEntity.ok().body(procedureTypePrices);
	}
	
	@PostMapping("/insurance_plans/change_procedure_type_coverage")
	public ResponseEntity<LProcedureTypePrice> changeProcedureTypeCoverage(
			@RequestBody LProcedureTypePrice procedureTypePrice,
			HttpServletRequest request){
		Optional<ProcedureType> tt = procedureTypeRepository.findById(procedureTypePrice.getProcedureType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected procedure type not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(procedureTypePrice.getProcedureTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = procedureTypePrice.getProcedureTypeInsurancePlan().isCovered();
		Optional<ProcedureTypeInsurancePlan> lttip = procedureTypeInsurancePlanRepository.findByInsurancePlanAndProcedureType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getPrice() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		ProcedureTypeInsurancePlan plan = procedureTypeInsurancePlanRepository.save(lttip.get());
		
		LProcedureTypePrice coverage = new LProcedureTypePrice();
		coverage.setProcedureType(tt.get());
		coverage.setProcedureTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_procedure_type_price_by_insurance")
	public ResponseEntity<LProcedureTypePrice> updateProcedureTypePriceByInsurance(
			@RequestBody LProcedureTypePrice procedureTypePrice,
			HttpServletRequest request){
		Optional<ProcedureType> tt = procedureTypeRepository.findById(procedureTypePrice.getProcedureType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected procedure not found");
		}
		
		LProcedureTypePrice coverage = new LProcedureTypePrice();
		if(procedureTypePrice.getProcedureTypeInsurancePlan().getInsurancePlan().getId() == 0) {
			if(procedureTypePrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setPrice(procedureTypePrice.getPrice());
			ProcedureType procedureType = procedureTypeRepository.save(tt.get());
			
			coverage.setProcedureType(procedureType);
			coverage.setProcedureTypeInsurancePlan(null);
			coverage.setPrice(procedureTypePrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(procedureTypePrice.getProcedureTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = procedureTypePrice.getPrice();
		Optional<ProcedureTypeInsurancePlan> lttip = procedureTypeInsurancePlanRepository.findByInsurancePlanAndProcedureType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setPrice(price);
		
		ProcedureTypeInsurancePlan plan = procedureTypeInsurancePlanRepository.save(lttip.get());
		
		coverage = new LProcedureTypePrice();
		coverage.setProcedureType(tt.get());
		coverage.setProcedureTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	
	@GetMapping("/insurance_plans/get_radiology_type_prices")
	public ResponseEntity<List<LRadiologyTypePrice>> getRadiologyTypePrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LRadiologyTypePrice> radiologyTypePrices = new ArrayList<>();
		List<RadiologyType> radiologyTypes = radiologyTypeRepository.findAll();
		for(RadiologyType t : radiologyTypes) {
			LRadiologyTypePrice radiologyTypePrice = new LRadiologyTypePrice();
			radiologyTypePrice.setRadiologyType(t);
			if(insurancePlanId == 0) {
				radiologyTypePrice.setPrice(t.getPrice());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<RadiologyTypeInsurancePlan> p = radiologyTypeInsurancePlanRepository.findByRadiologyTypeAndInsurancePlan(t, i.get());
				RadiologyTypeInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new RadiologyTypeInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setPrice(0);
					plan.setInsurancePlan(i.get());
					plan.setRadiologyType(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = radiologyTypeInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				radiologyTypePrice.setRadiologyTypeInsurancePlan(plan);
				radiologyTypePrice.setPrice(plan.getPrice());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			radiologyTypePrices.add(radiologyTypePrice);
		}
		return ResponseEntity.ok().body(radiologyTypePrices);
	}
	
	@PostMapping("/insurance_plans/change_radiology_type_coverage")
	public ResponseEntity<LRadiologyTypePrice> changeRadiologyTypeCoverage(
			@RequestBody LRadiologyTypePrice radiologyTypePrice,
			HttpServletRequest request){
		Optional<RadiologyType> tt = radiologyTypeRepository.findById(radiologyTypePrice.getRadiologyType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected radiology type not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(radiologyTypePrice.getRadiologyTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = radiologyTypePrice.getRadiologyTypeInsurancePlan().isCovered();
		Optional<RadiologyTypeInsurancePlan> lttip = radiologyTypeInsurancePlanRepository.findByInsurancePlanAndRadiologyType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getPrice() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		RadiologyTypeInsurancePlan plan = radiologyTypeInsurancePlanRepository.save(lttip.get());
		
		LRadiologyTypePrice coverage = new LRadiologyTypePrice();
		coverage.setRadiologyType(tt.get());
		coverage.setRadiologyTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_radiology_type_price_by_insurance")
	public ResponseEntity<LRadiologyTypePrice> updateRadiologyTypePriceByInsurance(
			@RequestBody LRadiologyTypePrice radiologyTypePrice,
			HttpServletRequest request){
		Optional<RadiologyType> tt = radiologyTypeRepository.findById(radiologyTypePrice.getRadiologyType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected radiology not found");
		}
		
		LRadiologyTypePrice coverage = new LRadiologyTypePrice();
		if(radiologyTypePrice.getRadiologyTypeInsurancePlan().getInsurancePlan().getId() == 0) {
			if(radiologyTypePrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setPrice(radiologyTypePrice.getPrice());
			RadiologyType radiologyType = radiologyTypeRepository.save(tt.get());
			
			coverage.setRadiologyType(radiologyType);
			coverage.setRadiologyTypeInsurancePlan(null);
			coverage.setPrice(radiologyTypePrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(radiologyTypePrice.getRadiologyTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = radiologyTypePrice.getPrice();
		Optional<RadiologyTypeInsurancePlan> lttip = radiologyTypeInsurancePlanRepository.findByInsurancePlanAndRadiologyType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setPrice(price);
		
		RadiologyTypeInsurancePlan plan = radiologyTypeInsurancePlanRepository.save(lttip.get());
		
		coverage = new LRadiologyTypePrice();
		coverage.setRadiologyType(tt.get());
		coverage.setRadiologyTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	
	@GetMapping("/insurance_plans/get_medicine_prices")
	public ResponseEntity<List<LMedicinePrice>> getMedicinePrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LMedicinePrice> medicinePrices = new ArrayList<>();
		List<Medicine> medicines = medicineRepository.findAll();
		for(Medicine t : medicines) {
			LMedicinePrice medicinePrice = new LMedicinePrice();
			medicinePrice.setMedicine(t);
			if(insurancePlanId == 0) {
				medicinePrice.setPrice(t.getPrice());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<MedicineInsurancePlan> p = medicineInsurancePlanRepository.findByMedicineAndInsurancePlan(t, i.get());
				MedicineInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new MedicineInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setPrice(0);
					plan.setInsurancePlan(i.get());
					plan.setMedicine(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = medicineInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				medicinePrice.setMedicineInsurancePlan(plan);
				medicinePrice.setPrice(plan.getPrice());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			medicinePrices.add(medicinePrice);
		}
		return ResponseEntity.ok().body(medicinePrices);
	}
	
	@PostMapping("/insurance_plans/change_medicine_coverage")
	public ResponseEntity<LMedicinePrice> changeMedicineCoverage(
			@RequestBody LMedicinePrice medicinePrice,
			HttpServletRequest request){
		Optional<Medicine> tt = medicineRepository.findById(medicinePrice.getMedicine().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected medicine type not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(medicinePrice.getMedicineInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = medicinePrice.getMedicineInsurancePlan().isCovered();
		Optional<MedicineInsurancePlan> lttip = medicineInsurancePlanRepository.findByInsurancePlanAndMedicine(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getPrice() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		MedicineInsurancePlan plan = medicineInsurancePlanRepository.save(lttip.get());
		
		LMedicinePrice coverage = new LMedicinePrice();
		coverage.setMedicine(tt.get());
		coverage.setMedicineInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_medicine_price_by_insurance")
	public ResponseEntity<LMedicinePrice> updateMedicinePriceByInsurance(
			@RequestBody LMedicinePrice medicinePrice,
			HttpServletRequest request){
		Optional<Medicine> tt = medicineRepository.findById(medicinePrice.getMedicine().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected medicine not found");
		}
		
		LMedicinePrice coverage = new LMedicinePrice();
		if(medicinePrice.getMedicineInsurancePlan().getInsurancePlan().getId() == 0) {
			if(medicinePrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setPrice(medicinePrice.getPrice());
			Medicine medicine = medicineRepository.save(tt.get());
			
			coverage.setMedicine(medicine);
			coverage.setMedicineInsurancePlan(null);
			coverage.setPrice(medicinePrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(medicinePrice.getMedicineInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = medicinePrice.getPrice();
		Optional<MedicineInsurancePlan> lttip = medicineInsurancePlanRepository.findByInsurancePlanAndMedicine(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setPrice(price);
		
		MedicineInsurancePlan plan = medicineInsurancePlanRepository.save(lttip.get());
		
		coverage = new LMedicinePrice();
		coverage.setMedicine(tt.get());
		coverage.setMedicineInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@GetMapping("/insurance_plans/get_consultation_prices")
	public ResponseEntity<List<LConsultationPrice>> getConsultationPrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LConsultationPrice> consultationPrices = new ArrayList<>();
		List<Clinic> clinics = clinicRepository.findAll();
		for(Clinic t : clinics) {
			LConsultationPrice consultationPrice = new LConsultationPrice();
			consultationPrice.setClinic(t);
			if(insurancePlanId == 0) {
				consultationPrice.setPrice(t.getConsultationFee());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<ConsultationInsurancePlan> p = consultationInsurancePlanRepository.findByClinicAndInsurancePlan(t, i.get());
				ConsultationInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new ConsultationInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setConsultationFee(0);
					plan.setInsurancePlan(i.get());
					plan.setClinic(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = consultationInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				consultationPrice.setConsultationInsurancePlan(plan);
				consultationPrice.setPrice(plan.getConsultationFee());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			consultationPrices.add(consultationPrice);
		}
		return ResponseEntity.ok().body(consultationPrices);
	}
	
	@PostMapping("/insurance_plans/change_consultation_coverage")
	public ResponseEntity<LConsultationPrice> changeConsultationCoverage(
			@RequestBody LConsultationPrice consultationPrice,
			HttpServletRequest request){
		Optional<Clinic> tt = clinicRepository.findById(consultationPrice.getClinic().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected clinic not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(consultationPrice.getConsultationInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = consultationPrice.getConsultationInsurancePlan().isCovered();
		Optional<ConsultationInsurancePlan> lttip = consultationInsurancePlanRepository.findByInsurancePlanAndClinic(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getConsultationFee() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		ConsultationInsurancePlan plan = consultationInsurancePlanRepository.save(lttip.get());
		
		LConsultationPrice coverage = new LConsultationPrice();
		coverage.setClinic(tt.get());
		coverage.setConsultationInsurancePlan(plan);
		coverage.setPrice(plan.getConsultationFee());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_consultation_price_by_insurance")
	public ResponseEntity<LConsultationPrice> updateConsultationPriceByInsurance(
			@RequestBody LConsultationPrice consultationPrice,
			HttpServletRequest request){
		Optional<Clinic> tt = clinicRepository.findById(consultationPrice.getClinic().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected clinic not found");
		}
		
		LConsultationPrice coverage = new LConsultationPrice();
		if(consultationPrice.getConsultationInsurancePlan().getInsurancePlan().getId() == 0) {
			if(consultationPrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setConsultationFee(consultationPrice.getPrice());
			Clinic clinic = clinicRepository.save(tt.get());
			
			coverage.setClinic(clinic);
			coverage.setConsultationInsurancePlan(null);
			coverage.setPrice(consultationPrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(consultationPrice.getConsultationInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = consultationPrice.getPrice();
		Optional<ConsultationInsurancePlan> lttip = consultationInsurancePlanRepository.findByInsurancePlanAndClinic(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setConsultationFee(price);
		
		ConsultationInsurancePlan plan = consultationInsurancePlanRepository.save(lttip.get());
		
		coverage = new LConsultationPrice();
		coverage.setClinic(tt.get());
		coverage.setConsultationInsurancePlan(plan);
		coverage.setPrice(plan.getConsultationFee());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	
	@GetMapping("/insurance_plans/get_registration_prices")
	public ResponseEntity<List<LRegistrationPrice>> getRegistrationPrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LRegistrationPrice> registrationPrices = new ArrayList<>();
		//List<Clinic> clinics = clinicRepository.findAll();
		//for(Clinic t : clinics) {
			LRegistrationPrice registrationPrice = new LRegistrationPrice();
			//registrationPrice.setClinic(t);
			if(insurancePlanId == 0) {
				List<CompanyProfile> cps = companyProfileRepository.findAll();
				for(CompanyProfile cp : cps) {
					registrationPrice.setPrice(cp.getRegistrationFee());
				}
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<RegistrationInsurancePlan> p = registrationInsurancePlanRepository.findByInsurancePlan(i.get());
				RegistrationInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new RegistrationInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setRegistrationFee(0);
					plan.setInsurancePlan(i.get());
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = registrationInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				registrationPrice.setRegistrationInsurancePlan(plan);
				registrationPrice.setPrice(plan.getRegistrationFee());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			registrationPrices.add(registrationPrice);
		//}
		return ResponseEntity.ok().body(registrationPrices);
	}
	
	@PostMapping("/insurance_plans/change_registration_coverage")
	public ResponseEntity<LRegistrationPrice> changeRegistrationCoverage(
			@RequestBody LRegistrationPrice registrationPrice,
			HttpServletRequest request){
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(registrationPrice.getRegistrationInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = registrationPrice.getRegistrationInsurancePlan().isCovered();
		Optional<RegistrationInsurancePlan> lttip = registrationInsurancePlanRepository.findByInsurancePlan(ip.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getRegistrationFee() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		RegistrationInsurancePlan plan = registrationInsurancePlanRepository.save(lttip.get());
		
		LRegistrationPrice coverage = new LRegistrationPrice();
		coverage.setRegistrationInsurancePlan(plan);
		coverage.setPrice(plan.getRegistrationFee());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_registration_price_by_insurance")
	public ResponseEntity<LRegistrationPrice> updateRegistrationPriceByInsurance(
			@RequestBody LRegistrationPrice registrationPrice,
			HttpServletRequest request){
		
		LRegistrationPrice coverage = new LRegistrationPrice();
		if(registrationPrice.getRegistrationInsurancePlan().getInsurancePlan().getId() == 0) {
			if(registrationPrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			CompanyProfile companyProfile = null;
			List<CompanyProfile> profiles = companyProfileRepository.findAll();
			for(CompanyProfile profile : profiles) {
				companyProfile = profile;
			}
			companyProfile.setRegistrationFee(registrationPrice.getPrice());
			companyProfile = companyProfileRepository.save(companyProfile);
			
			coverage.setRegistrationInsurancePlan(null);
			coverage.setPrice(registrationPrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(registrationPrice.getRegistrationInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = registrationPrice.getPrice();
		Optional<RegistrationInsurancePlan> lttip = registrationInsurancePlanRepository.findByInsurancePlan(ip.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setRegistrationFee(price);
		
		RegistrationInsurancePlan plan = registrationInsurancePlanRepository.save(lttip.get());
		
		coverage = new LRegistrationPrice();
		coverage.setRegistrationInsurancePlan(plan);
		coverage.setPrice(plan.getRegistrationFee());
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@GetMapping("/insurance_plans/get_ward_type_prices")
	public ResponseEntity<List<LWardTypePrice>> getWardTypePrices(
			@RequestParam(name = "insurance_plan_id") Long insurancePlanId,
			HttpServletRequest request){
		List<LWardTypePrice> wardTypePrices = new ArrayList<>();
		List<WardType> wardTypes = wardTypeRepository.findAll();
		for(WardType t : wardTypes) {
			LWardTypePrice wardTypePrice = new LWardTypePrice();
			wardTypePrice.setWardType(t);
			if(insurancePlanId == 0) {
				wardTypePrice.setPrice(t.getPrice());
			}else if(insurancePlanId > 0) {
				Optional<InsurancePlan> i = insurancePlanRepository.findById(insurancePlanId);
				if(i.isEmpty()) {
					throw new NotFoundException("Insurance package not found");
				}
				Optional<WardTypeInsurancePlan> p = wardTypeInsurancePlanRepository.findByWardTypeAndInsurancePlan(t, i.get());
				WardTypeInsurancePlan plan;
				if(p.isEmpty()) {
					//create
					plan = new WardTypeInsurancePlan();
					plan.setActive(true);
					plan.setCovered(false);
					plan.setPrice(0);
					plan.setInsurancePlan(i.get());
					plan.setWardType(t);
					
					plan.setCreatedby(userService.getUserId(request));
					plan.setCreatedOn(dayService.getDayId());
					plan.setCreatedAt(dayService.getTimeStamp());
					
					plan = wardTypeInsurancePlanRepository.save(plan);
				}else {
					plan = p.get();
				}
				wardTypePrice.setWardTypeInsurancePlan(plan);
				wardTypePrice.setPrice(plan.getPrice());
			}else {
				throw new InvalidOperationException("Invalid package type selected");
			}
			wardTypePrices.add(wardTypePrice);
		}
		return ResponseEntity.ok().body(wardTypePrices);
	}
	
	@PostMapping("/insurance_plans/change_ward_type_coverage")
	public ResponseEntity<LWardTypePrice> changeWardTypeCoverage(
			@RequestBody LWardTypePrice wardTypePrice,
			HttpServletRequest request){
		Optional<WardType> tt = wardTypeRepository.findById(wardTypePrice.getWardType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected ward type not found");
		}
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(wardTypePrice.getWardTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		boolean covered = wardTypePrice.getWardTypeInsurancePlan().isCovered();
		Optional<WardTypeInsurancePlan> lttip = wardTypeInsurancePlanRepository.findByInsurancePlanAndWardType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(covered == true) {
			if(lttip.get().getPrice() <= 0) {
				throw new InvalidOperationException("Could not change coverage. Invalid price value. Should not be equal or less than zero");
			}
		}
		lttip.get().setCovered(covered);
		WardTypeInsurancePlan plan = wardTypeInsurancePlanRepository.save(lttip.get());
		
		LWardTypePrice coverage = new LWardTypePrice();
		coverage.setWardType(tt.get());
		coverage.setWardTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		
		return ResponseEntity.ok().body(coverage);
	}
	
	@PostMapping("/insurance_plans/update_ward_type_price_by_insurance")
	public ResponseEntity<LWardTypePrice> updateWardTypePriceByInsurance(
			@RequestBody LWardTypePrice wardTypePrice,
			HttpServletRequest request){
		Optional<WardType> tt = wardTypeRepository.findById(wardTypePrice.getWardType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Selected ward type not found");
		}
		
		LWardTypePrice coverage = new LWardTypePrice();
		if(wardTypePrice.getWardTypeInsurancePlan().getInsurancePlan().getId() == 0) {
			if(wardTypePrice.getPrice() < 0) {
				throw new InvalidOperationException("Invalid price value. Price should not be less than zero");
			}
			tt.get().setPrice(wardTypePrice.getPrice());
			WardType wardType = wardTypeRepository.save(tt.get());
			
			coverage.setWardType(wardType);
			coverage.setWardTypeInsurancePlan(null);
			coverage.setPrice(wardTypePrice.getPrice());
			coverage.setCovered(true);
			
			return ResponseEntity.ok().body(coverage);
		}
		
		Optional<InsurancePlan> ip = insurancePlanRepository.findById(wardTypePrice.getWardTypeInsurancePlan().getInsurancePlan().getId());
		if(ip.isEmpty()) {
			throw new NotFoundException("Selected plan not found");
		}
		double price = wardTypePrice.getPrice();
		Optional<WardTypeInsurancePlan> lttip = wardTypeInsurancePlanRepository.findByInsurancePlanAndWardType(ip.get(), tt.get());
		if(lttip.isEmpty()) {
			throw new NotFoundException("Selected package not found");
		}
		if(price == 0) {
			lttip.get().setCovered(false);
		}else if(price < 0) {
			throw new InvalidOperationException("Invalid Price value. Price should not be less than zero");
		}
		lttip.get().setPrice(price);
		
		WardTypeInsurancePlan plan = wardTypeInsurancePlanRepository.save(lttip.get());
		
		coverage = new LWardTypePrice();
		coverage.setWardType(tt.get());
		coverage.setWardTypeInsurancePlan(plan);
		coverage.setPrice(plan.getPrice());
		
		return ResponseEntity.ok().body(coverage);
	}
	
}

@Data
class LLabTestTypePrice{
	LabTestType labTestType = null;
	LabTestTypeInsurancePlan labTestTypeInsurancePlan = null;	
	double price;
	boolean covered;
}

@Data
class LProcedureTypePrice{
	ProcedureType procedureType = null;
	ProcedureTypeInsurancePlan procedureTypeInsurancePlan = null;
	double price;
	boolean covered;
}

@Data
class LRadiologyTypePrice{
	RadiologyType radiologyType = null;
	RadiologyTypeInsurancePlan radiologyTypeInsurancePlan = null;
	double price;
	boolean covered;
}

@Data
class LMedicinePrice{
	Medicine medicine = null;
	MedicineInsurancePlan medicineInsurancePlan = null;
	double price;
	boolean covered;
}

@Data
class LConsultationPrice{
	Clinic clinic = null;
	ConsultationInsurancePlan consultationInsurancePlan = null;
	double price;
	boolean covered;
}

@Data
class LRegistrationPrice{
	//Clinic clinic = null;
	RegistrationInsurancePlan registrationInsurancePlan = null;
	double price;
	boolean covered;
}

@Data
class LWardTypePrice{
	WardType wardType = null;
	WardTypeInsurancePlan wardTypeInsurancePlan = null;
	double price;
	boolean covered;
}

