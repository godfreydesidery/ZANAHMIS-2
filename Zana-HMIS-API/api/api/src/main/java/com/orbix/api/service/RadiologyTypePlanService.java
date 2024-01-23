/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface RadiologyTypePlanService {
	RadiologyTypeInsurancePlan save(InsurancePlan insurancePlan, RadiologyType radiologyType, double price, HttpServletRequest request);	
	//List<RadiologyTypeInsurancePlan>getRadiologyTypePlans(); // return all the insuranceProviders	
	boolean deleteRadiologyTypeInsurancePlan(InsurancePlan insurancePlan, RadiologyType radiologyType, HttpServletRequest request);
}
