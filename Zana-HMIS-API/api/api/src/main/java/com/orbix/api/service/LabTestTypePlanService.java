/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypePlanService {
	LabTestTypeInsurancePlan save(InsurancePlan insurancePlan, LabTestType labTestType, double price, HttpServletRequest request);	
	//List<LabTestTypeInsurancePlan>getLabTestTypePlans(); // return all the insuranceProviders	
	boolean deleteLabTestTypeInsurancePlan(InsurancePlan insurancePlan, LabTestType labTestType, HttpServletRequest request);
}
