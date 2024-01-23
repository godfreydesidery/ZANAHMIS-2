/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RegistrationInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface RegistrationPlanService {
	RegistrationInsurancePlan save(InsurancePlan insurancePlan, double price, HttpServletRequest request);	
	//List<RegistrationInsurancePlan>getRegistrationPlans(); // return all the insuranceProviders	
	boolean deleteRegistrationInsurancePlan(InsurancePlan insurancePlan, HttpServletRequest request);
}
