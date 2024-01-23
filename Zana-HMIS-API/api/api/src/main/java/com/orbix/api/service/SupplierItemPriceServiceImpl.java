/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.SupplierItemPrice;
import com.orbix.api.domain.SupplierItemPriceList;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.LabTestTypeRangeRepository;
import com.orbix.api.repositories.SupplierItemPriceRepository;
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
public class SupplierItemPriceServiceImpl implements SupplierItemPriceService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final SupplierItemPriceRepository supplierItemPriceRepository;
	
	@Override
	public SupplierItemPriceList save(SupplierItemPrice supplierItemPrice, HttpServletRequest request) {
		
		supplierItemPrice.setCreatedBy(userService.getUser(request).getId());
		supplierItemPrice.setCreatedOn(dayService.getDay().getId());
		supplierItemPrice.setCreatedAt(dayService.getTimeStamp());
		
		supplierItemPrice = supplierItemPriceRepository.save(supplierItemPrice);
		
		SupplierItemPriceList supplierItemPriceList = new SupplierItemPriceList();
		supplierItemPriceList.setSupplier(supplierItemPrice.getSupplier());
		supplierItemPriceList.setSupplierItemPrices(supplierItemPriceRepository.findAllBySupplier(supplierItemPrice.getSupplier()));
		
		return supplierItemPriceList;
	}
	
	@Override
	public SupplierItemPriceList update(SupplierItemPrice supplierItemPrice, HttpServletRequest request) {
		
		supplierItemPrice = supplierItemPriceRepository.save(supplierItemPrice);
		
		SupplierItemPriceList supplierItemPriceList = new SupplierItemPriceList();
		supplierItemPriceList.setSupplier(supplierItemPrice.getSupplier());
		supplierItemPriceList.setSupplierItemPrices(supplierItemPriceRepository.findAllBySupplier(supplierItemPrice.getSupplier()));
		
		return supplierItemPriceList;
	}

	@Override
	public List<SupplierItemPrice> getSupplierItemPrices(Supplier supplier, HttpServletRequest request) {
		return supplierItemPriceRepository.findAllBySupplier(supplier);
	}

	@Override
	public SupplierItemPrice getSupplierItemPriceById(Long id, HttpServletRequest request) {
		return supplierItemPriceRepository.findById(id).get();
	}

	@Override
	public boolean deleteSupplierItemPrice(SupplierItemPrice supplierItemPrice, HttpServletRequest request) {
		/**
		 * Delete a labTestTypeRange if a labTestTypeRange is deletable
		 */
		if(allowDeleteSupplierItemPrice(supplierItemPrice) == false) {
			throw new InvalidOperationException("Deleting this Record is not allowed");
		}
		supplierItemPriceRepository.delete(supplierItemPrice);
		return true;
	}
	
	private boolean allowDeleteSupplierItemPrice(SupplierItemPrice supplierItemPrice) {
		/**
		 * Code to check if a labTestTypeRange is deletable
		 * Returns false if not
		 */
		return true;
	}
}
