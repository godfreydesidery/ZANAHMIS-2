/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Supplier;

/**
 * @author Godfrey
 *
 */
public interface SupplierService {
	Supplier save(Supplier supplier, HttpServletRequest request);	
	List<Supplier>getSuppliers(HttpServletRequest request); // return all the suppliers
	Supplier getSupplierByName(String name, HttpServletRequest request);
	Supplier getSupplierById(Long id, HttpServletRequest request);
	boolean deleteSupplier(Supplier supplier, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param supplierName
	 * @return
	 */
	Supplier getByName(String supplierName, HttpServletRequest request);
}
