/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Supplier;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SupplierServiceImpl implements SupplierService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final SupplierRepository supplierRepository;
	
	@Override
	public Supplier save(Supplier supplier, HttpServletRequest request) {
		
		supplier.setCode(Sanitizer.sanitizeString(supplier.getCode().replace(" ", "")));
		supplier.setName(supplier.getName());
		
		if(supplier.getId() == null) {
			supplier.setCreatedBy(userService.getUser(request).getId());
			supplier.setCreatedOn(dayService.getDay().getId());
			supplier.setCreatedAt(dayService.getTimeStamp());
			
			supplier.setActive(true);
		}
		
		
		log.info("Saving new supplier to the database");
		return supplierRepository.save(supplier);
	}

	@Override
	public List<Supplier> getSuppliers(HttpServletRequest request) {
		log.info("Fetching all suppliers");
		return supplierRepository.findAll();
	}

	@Override
	public Supplier getSupplierByName(String name, HttpServletRequest request) {
		return supplierRepository.findByName(name).get();
	}

	@Override
	public Supplier getSupplierById(Long id, HttpServletRequest request) {
		return supplierRepository.findById(id).get();
	}

	@Override
	public boolean deleteSupplier(Supplier supplier, HttpServletRequest request) {
		/**
		 * Delete a supplier if a supplier is deletable
		 */
		if(allowDeleteSupplier(supplier) == false) {
			throw new InvalidOperationException("Deleting this supplier is not allowed");
		}
		supplierRepository.delete(supplier);
		return true;
	}
	
	private boolean allowDeleteSupplier(Supplier supplier) {
		/**
		 * Code to check if a supplier is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public List<String> getNames(HttpServletRequest request) {
		return supplierRepository.getNames();	
	}

	@Override
	public Supplier getByName(String supplierName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return supplierRepository.findByName(supplierName).get();
	}
}
