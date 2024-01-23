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

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.Medicine;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.InsurancePlanService;
import com.orbix.api.service.MedicineService;
import com.orbix.api.service.UserService;
import com.orbix.api.service.MedicineService;

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
public class MedicineResource {
	private final MedicineRepository medicineRepository;
	private final MedicineService medicineService;
	private final UserService userService;
	private final DayService dayService;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	
	
	@GetMapping("/medicines")
	public ResponseEntity<List<Medicine>>getMedicines(
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineService.getMedicines(request));
	}
	
	@GetMapping("/medicines/get")
	public ResponseEntity<Medicine> getMedicine(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineService.getMedicineById(id, request));
	}
	
	@GetMapping("/medicines/get_by_code")
	public ResponseEntity<Medicine> getMedicineByCode(
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineService.getMedicineByCode(code, request));
	}
	
	@GetMapping("/medicines/get_by_name")
	public ResponseEntity<Medicine> getMedicineByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(medicineService.getMedicineByName(name, request));
	}
	
	@GetMapping("/medicines/get_names")
	public ResponseEntity<List<String>> getMedicineNames(
			HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = medicineRepository.getNames();
		return ResponseEntity.ok().body(names);
	}
	
	@GetMapping("/medicines/load_medicines_like")
	public ResponseEntity<List<Medicine>> getMedicineNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Medicine> medicines = new ArrayList<Medicine>();
		medicines = medicineRepository.findAllByNameContainingAndActive(value, true);
		return ResponseEntity.ok().body(medicines);
	}
	
	@PostMapping("/medicines/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Medicine>save(
			@RequestBody Medicine medicine,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/medicines/save").toUriString());
		return ResponseEntity.created(uri).body(medicineService.save(medicine, request));
	}
	
	@PostMapping("/medicines/get_available_units")
	public double getMedicineUnits(
			@RequestBody Medicine med,
			HttpServletRequest request){
		double unit = 0;
		Optional<Medicine> medicine = medicineRepository.findById(med.getId());
		if(medicine.isEmpty()) {
			return 0;
		}
		List<PharmacyMedicine> pms = pharmacyMedicineRepository.findAllByMedicine(medicine.get());
		for(PharmacyMedicine pm : pms) {
			unit = unit + pm.getStock();
		}
		return unit;
	}
	
	@PostMapping("/medicines/activate_medicine")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Medicine>activateBed(
			@RequestBody Medicine medicine,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/medicines/activate_medicine").toUriString());
		
		return ResponseEntity.created(uri).body(medicineService.activateMedicine(medicine, request));
	}
	
	@PostMapping("/medicines/deactivate_medicine")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Medicine>deactivateBed(
			@RequestBody Medicine medicine,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/medicines/deactivate_medicine").toUriString());
		return ResponseEntity.created(uri).body(medicineService.deactivateMedicine(medicine, request));
	}
	
}
