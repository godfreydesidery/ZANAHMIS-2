/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface InsurancePlanService {
	InsurancePlan save(InsurancePlan insurancePlan, HttpServletRequest request);	
	List<InsurancePlan>getInsurancePlans(HttpServletRequest request); // return all the insurancePlans
	InsurancePlan getInsurancePlanByName(String name, HttpServletRequest request);
	InsurancePlan getInsurancePlanById(Long id, HttpServletRequest request);
	boolean deleteInsurancePlan(InsurancePlan insurancePlan, HttpServletRequest request);
}
