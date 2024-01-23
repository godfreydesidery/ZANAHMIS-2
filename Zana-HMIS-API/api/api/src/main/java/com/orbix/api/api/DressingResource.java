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
import com.orbix.api.domain.Dressing;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DressingRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.DressingService;
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
public class DressingResource {
	private final DressingRepository dressingRepository;
	private final UserService userService;
	private final DayService dayService;
	private final ProcedureTypeRepository procedureTypeRepository;
	private final DressingService dressingService;
	
	
	@GetMapping("/dressings")
	public ResponseEntity<List<Dressing>>getDressings(
			HttpServletRequest request){
		return ResponseEntity.ok().body(dressingRepository.findAll());
	}
	
	@GetMapping("/dressings/get")
	public ResponseEntity<Dressing> getDressing(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Dressing> d = dressingRepository.findById(id);
		if(d.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		return ResponseEntity.ok().body(d.get());
	}
	
	@GetMapping("/dressings/get_by_procedure_type")
	public ResponseEntity<Dressing> getDressingByProcedureType(
			@RequestParam(name = "procedure_type_id") Long procedureTypeId,
			HttpServletRequest request){
		Optional<ProcedureType> pt = procedureTypeRepository.findById(procedureTypeId);
		if(pt.isEmpty()) {
			throw new NotFoundException("Procedure type not found");
		}
		
		return ResponseEntity.ok().body(dressingRepository.findByProcedureType(pt.get()));
	}
	
	@GetMapping("/dressings/load_dressings_like")
	public ResponseEntity<List<Dressing>> getDressingNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<ProcedureType> pts = procedureTypeRepository.findAllByNameContaining(value);
		List<Dressing> ds = dressingRepository.findAll();
		List<Dressing> dsToshow = new ArrayList<>();
		for(ProcedureType p : pts) {
			for(Dressing d : ds) {
				if(d.getProcedureType().getId() == p.getId()) {
					dsToshow.add(d);
				}
			}
		}
		return ResponseEntity.ok().body(dsToshow);
	}
	
	@PostMapping("/dressings/add")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Dressing>add(
			@RequestBody Dressing dressing,
			HttpServletRequest request){		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/dressings/add").toUriString());
		return ResponseEntity.created(uri).body(dressingService.add(dressing, request));
	}	
	
	@PostMapping("/dressings/delete")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public boolean deleteDressing(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){		
		dressingService.deleteById(id);
		return true;
	}
}
