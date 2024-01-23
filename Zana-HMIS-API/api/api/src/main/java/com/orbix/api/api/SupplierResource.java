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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.SupplierService;
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
public class SupplierResource {

	private final SupplierRepository supplierRepository;
	private final SupplierService supplierService;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/suppliers")
	public ResponseEntity<List<Supplier>>getSuppliers(HttpServletRequest request){
		return ResponseEntity.ok().body(supplierService.getSuppliers(request));
	}
	
	@GetMapping("/suppliers/load_suppliers_like")
	public ResponseEntity<List<Supplier>> getSupplierNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Supplier> suppliers = new ArrayList<Supplier>();
		suppliers = supplierRepository.findAllByNameContainingOrCodeContaining(value, value);
		return ResponseEntity.ok().body(suppliers);
	}
	
	@GetMapping("/suppliers/get")
	public ResponseEntity<Supplier> getSupplier(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(supplierService.getSupplierById(id, request));
	}
	
	@GetMapping("/suppliers/search")
	public ResponseEntity<Supplier> searchSupplier(
			@RequestParam(name = "code") String code,
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		Optional<Supplier> i;
		if(!code.equals("")) {
			i = supplierRepository.findByCode(code);
			if(i.isEmpty()) {
				throw new NotFoundException("Supplier not found");
			}
		}else if(!name.equals("")) {
			i = supplierRepository.findByName(name);
			if(i.isEmpty()) {
				throw new NotFoundException("Supplier not found");
			}
		}else {
			throw new InvalidOperationException("No search key specified");
		}
		return ResponseEntity.ok().body(i.get());
	}
	
	@GetMapping("/suppliers/get_names")
	public ResponseEntity<List<String>> getSupplierNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = supplierService.getNames(request);
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/suppliers/save")
	//@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Supplier>save(
			@RequestBody Supplier supplier,
			HttpServletRequest request){
		supplier.setName(supplier.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/suppliers/save").toUriString());
		return ResponseEntity.created(uri).body(supplierService.save(supplier, request));
	}
}
