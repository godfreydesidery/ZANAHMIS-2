/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.SupplierItemPrice;
import com.orbix.api.domain.SupplierItemPriceList;

/**
 * @author Godfrey
 *
 */
public interface SupplierItemPriceService {
	SupplierItemPriceList save(SupplierItemPrice supplierItemPrice, HttpServletRequest request);
	SupplierItemPriceList update(SupplierItemPrice supplierItemPrice, HttpServletRequest request);
	List<SupplierItemPrice>getSupplierItemPrices(Supplier supplier, HttpServletRequest request); // return all the labTestTypeRanges
	SupplierItemPrice getSupplierItemPriceById(Long id, HttpServletRequest request);
	boolean deleteSupplierItemPrice(SupplierItemPrice SupplierItemPrice, HttpServletRequest request);
}
