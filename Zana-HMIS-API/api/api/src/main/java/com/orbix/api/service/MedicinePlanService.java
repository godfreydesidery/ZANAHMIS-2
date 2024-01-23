/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface MedicinePlanService {
	MedicineInsurancePlan save(InsurancePlan insurancePlan, Medicine medicine, double price, HttpServletRequest request);	
	//List<MedicinePlanPrice>getMedicinePlans(); // return all the insuranceProviders	
	boolean deleteMedicineInsurancePlan(InsurancePlan insurancePlan, Medicine medicine, HttpServletRequest request);
}
