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

import com.orbix.api.domain.Item;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.SupplierItemPrice;
import com.orbix.api.domain.SupplierItemPriceList;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ItemRepository;
import com.orbix.api.repositories.LabTestTypeRangeRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.SupplierItemPriceRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.LabTestTypeRangeService;
import com.orbix.api.service.SupplierItemPriceService;
import com.orbix.api.service.UserService;

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
public class SupplierItemPriceResource {
	private final SupplierRepository supplierRepository;
	private final ItemRepository itemRepository;
	private final SupplierItemPriceRepository supplierItemPriceRepository;
	private final SupplierItemPriceService supplierItemPriceService;
	private final UserService userService;
	private final DayService dayService;
	
	
	@GetMapping("/supplier_item_prices")
	public ResponseEntity<List<SupplierItemPrice>>getSupplierItemPrices(
			@RequestParam(name = "supplier_id") Long supplierId,
			HttpServletRequest request){
		Optional<Supplier> supplier_ = supplierRepository.findById(supplierId);
		if(supplier_.isEmpty()) {
			throw new NotFoundException("Supplier not found");
		}
		return ResponseEntity.ok().body(supplierItemPriceService.getSupplierItemPrices(supplier_.get(), request));
	}
	
	@GetMapping("/supplier_item_prices/get_item_price_list_by_supplier")
	public ResponseEntity<SupplierItemPriceList>getSupplierItemPriceListBySupplier(
			@RequestParam(name = "supplier_id") Long supplierId,
			HttpServletRequest request){
		Optional<Supplier> supplier_ = supplierRepository.findById(supplierId);
		if(supplier_.isEmpty()) {
			throw new NotFoundException("Supplier not found");
		}
		SupplierItemPriceList supplierItemPriceList = new SupplierItemPriceList();
		supplierItemPriceList.setSupplier(supplier_.get());
		List<SupplierItemPrice> supplierItemPrices = supplierItemPriceRepository.findAllBySupplier(supplier_.get());
		
		supplierItemPriceList.setSupplierItemPrices(supplierItemPrices);
		
		return ResponseEntity.ok().body(supplierItemPriceList);
	}
	
	@GetMapping("/supplier_item_prices/get")
	public ResponseEntity<SupplierItemPrice> getSupplierItemPrice(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(supplierItemPriceService.getSupplierItemPriceById(id, request));
	}
	
	@PostMapping("/supplier_item_prices/delete")
	@PreAuthorize("hasAnyAuthority('SUPPLIER_PRICE_LIST-ALL')")
	public boolean delete(
			@RequestBody SupplierItemPrice supplierItemPrice,
			HttpServletRequest request){
		Optional<SupplierItemPrice> supplierItemPrice_ = supplierItemPriceRepository.findById(supplierItemPrice.getId());
		if(supplierItemPrice_.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		if(supplierItemPrice_.get().getItem().getId() != supplierItemPrice.getItem().getId()) {
			throw new InvalidOperationException("Record mismatch. Item specified does not match the available records");
		}
		
		supplierItemPriceRepository.delete(supplierItemPrice_.get());
		return true;
	}
	
	@PostMapping("/supplier_item_prices/save")
	@PreAuthorize("hasAnyAuthority('SUPPLIER_PRICE_LIST-ALL')")
	public ResponseEntity<SupplierItemPriceList>save(
			@RequestBody SupplierItemPrice supplierItemPrice,
			HttpServletRequest request){
		Optional<Supplier> supplier_ = supplierRepository.findById(supplierItemPrice.getSupplier().getId());
		if(supplier_.isEmpty()) {
			throw new NotFoundException("Supplier not found");
		}
		Optional<Item> item_ = itemRepository.findById(supplierItemPrice.getItem().getId());
		if(item_.isEmpty()) {
			throw new NotFoundException("Item not found");
		}
		
		if(supplierItemPrice.getId() == null) {
			if(supplierItemPriceRepository.existsBySupplierAndItem(supplier_.get(), item_.get())) {
				throw new InvalidOperationException("Duplicate values are not allowed");
			}
		}
		
		supplierItemPrice.setSupplier(supplier_.get());
		supplierItemPrice.setItem(item_.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/supplier_item_prices/save").toUriString());
		return ResponseEntity.created(uri).body(supplierItemPriceService.save(supplierItemPrice, request));
	}
	
	@PostMapping("/supplier_item_prices/save_or_update")
	@PreAuthorize("hasAnyAuthority('SUPPLIER_PRICE_LIST-ALL')")
	public ResponseEntity<SupplierItemPriceList>saveOrUpdate(
			@RequestBody SupplierItemPrice supplierItemPrice,
			HttpServletRequest request){
		Optional<Supplier> supplier_ = supplierRepository.findById(supplierItemPrice.getSupplier().getId());
		if(supplier_.isEmpty()) {
			throw new NotFoundException("Supplier not found");
		}
		Optional<Item> item_ = itemRepository.findByCode(supplierItemPrice.getItem().getCode());
		if(item_.isEmpty()) {
			throw new NotFoundException("Item not found");
		}
		
		Optional<SupplierItemPrice> supplierItemPrice_ = supplierItemPriceRepository.findBySupplierAndItem(supplier_.get(), item_.get());
		
		if(supplierItemPrice_.isEmpty()) {
			supplierItemPrice.setSupplier(supplier_.get());
			supplierItemPrice.setItem(item_.get());
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/supplier_item_prices/save").toUriString());
			return ResponseEntity.created(uri).body(supplierItemPriceService.save(supplierItemPrice, request));
		}else {
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/supplier_item_prices/save_or_update").toUriString());
			supplierItemPrice_.get().setPrice(supplierItemPrice.getPrice());
			supplierItemPrice_.get().setTerms(supplierItemPrice.getTerms());
			return ResponseEntity.created(uri).body(supplierItemPriceService.update(supplierItemPrice_.get(), request));
		}
	}
}
