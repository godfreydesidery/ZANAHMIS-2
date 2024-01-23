/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
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
import com.orbix.api.domain.Item;
import com.orbix.api.domain.ItemMedicineCoefficient;
import com.orbix.api.domain.Medicine;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ItemMedicineCoefficientRepository;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.MedicineRepository;
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
public class ConversionCoefficientResource {
	
	private final ItemMedicineCoefficientRepository itemMedicineCoefficientRepository;
	private final ItemRepository itemRepository;
	private final MedicineRepository medicineRepository;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/item_medicine_coefficients")
	public ResponseEntity<List<ItemMedicineCoefficient>>getItemMedicineCoefficients(HttpServletRequest request){
		return ResponseEntity.ok().body(itemMedicineCoefficientRepository.findAll());
	}
	
	@GetMapping("/item_medicine_coefficients/get")
	public ResponseEntity<ItemMedicineCoefficient>get(
			@RequestParam (name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(itemMedicineCoefficientRepository.findById(id).get());
	}
	
	@PostMapping("/item_medicine_coefficients/save")
	//@PreAuthorize("hasAnyAuthority('ROLE-CREATE')")
	public ResponseEntity<ItemMedicineCoefficient>save(
			@RequestBody ItemMedicineCoefficient coef,
			HttpServletRequest request){
		
		Optional<Item> i = itemRepository.findByName(coef.getItem().getName());
		if(i.isEmpty()) {
			throw new NotFoundException("Item Not Found");
		}
		Optional<Medicine> med = medicineRepository.findByName(coef.getMedicine().getName());
		if(i.isEmpty()) {
			throw new NotFoundException("Medicine No tFound");
		}
		Optional<ItemMedicineCoefficient> c = itemMedicineCoefficientRepository.findByItemAndMedicine(i.get(), med.get());
		if(c.isPresent() && coef.getId() != c.get().getId()) {
			throw new InvalidOperationException("Coefficient already exist, please consider editing the existing coefficient");
		}
		if(coef.getItemQty() <= 0 || coef.getMedicineQty() <=0) {
			throw new InvalidEntryException("Zero values are not allowed");
		}
		ItemMedicineCoefficient coefficient = new ItemMedicineCoefficient();
		if(c.isPresent()) {
			coefficient = c.get();
			coefficient.setItemQty(coef.getItemQty());
			coefficient.setMedicineQty(coef.getMedicineQty());
			coefficient.setCoefficient(coef.getMedicineQty() / coef.getItemQty());
		}else {
			coefficient.setItem(i.get());
			coefficient.setMedicine(med.get());
			coefficient.setItemQty(coef.getItemQty());
			coefficient.setMedicineQty(coef.getMedicineQty());
			coefficient.setCoefficient(coef.getMedicineQty() / coef.getItemQty());
			
			coefficient.setCreatedBy(userService.getUser(request).getId());
			coefficient.setCreatedOn(dayService.getDay().getId());
			coefficient.setCreatedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/items/save").toUriString());
		return ResponseEntity.created(uri).body(itemMedicineCoefficientRepository.save(coefficient));
	}
}
