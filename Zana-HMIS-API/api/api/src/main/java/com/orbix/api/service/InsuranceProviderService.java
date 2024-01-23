/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsuranceProvider;

/**
 * @author Godfrey
 *
 */

public interface InsuranceProviderService {
	InsuranceProvider save(InsuranceProvider insuranceProvider, HttpServletRequest request);	
	List<InsuranceProvider>getInsuranceProviders(HttpServletRequest request); // return all the insuranceProviders
	InsuranceProvider getInsuranceProviderByName(String name, HttpServletRequest request);
	InsuranceProvider getInsuranceProviderById(Long id, HttpServletRequest request);
	boolean deleteInsuranceProvider(InsuranceProvider insuranceProvider, HttpServletRequest request);
}
