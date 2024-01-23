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

import com.orbix.api.domain.Consumable;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ConsumableRepository;
import com.orbix.api.repositories.DressingRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.service.ConsumableService;
import com.orbix.api.service.DayService;
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
public class ConsumableResource {
	private final ConsumableRepository consumableRepository;
	private final UserService userService;
	private final DayService dayService;
	private final MedicineRepository medicineRepository;
	private final ConsumableService consumableService;
	
	
	@GetMapping("/consumables")
	public ResponseEntity<List<Consumable>>getConsumables(
			HttpServletRequest request){
		return ResponseEntity.ok().body(consumableRepository.findAll());
	}
	
	@GetMapping("/consumables/get")
	public ResponseEntity<Consumable> getConsumable(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consumable> d = consumableRepository.findById(id);
		if(d.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		return ResponseEntity.ok().body(d.get());
	}
	
	@GetMapping("/consumables/get_by_procedure_type")
	public ResponseEntity<Consumable> getConsumableByProcedureType(
			@RequestParam(name = "procedure_type_id") Long procedureTypeId,
			HttpServletRequest request){
		Optional<Medicine> pt = medicineRepository.findById(procedureTypeId);
		if(pt.isEmpty()) {
			throw new NotFoundException("Procedure type not found");
		}
		
		return ResponseEntity.ok().body(consumableRepository.findByMedicine(pt.get()).get());
	}
	
	@GetMapping("/consumables/load_consumables_like")
	public ResponseEntity<List<Consumable>> getConsumableNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Medicine> pts = medicineRepository.findAllByNameContaining(value);
		List<Consumable> ds = consumableRepository.findAll();
		List<Consumable> dsToshow = new ArrayList<>();
		for(Medicine p : pts) {
			for(Consumable d : ds) {
				if(d.getMedicine().getId() == p.getId()) {
					dsToshow.add(d);
				}
			}
		}
		return ResponseEntity.ok().body(dsToshow);
	}
	
	@PostMapping("/consumables/add")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Consumable>save(
			@RequestBody Consumable consumable,
			HttpServletRequest request){		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/consumables/add").toUriString());
		return ResponseEntity.created(uri).body(consumableService.add(consumable, request));
	}	
	
	@PostMapping("/consumables/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public boolean deleteConsumable(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){		
		consumableService.deleteById(id);
		return true;
	}
}
