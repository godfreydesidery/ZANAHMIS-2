/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface ProcedureTypePlanService {
	ProcedureTypeInsurancePlan save(InsurancePlan insurancePlan, ProcedureType procedureType, double price, HttpServletRequest request);	
	//List<ProcedureTypeInsurancePlan>getProcedureTypePlans(); // return all the insuranceProviders	
	boolean deleteProcedureTypeInsurancePlan(InsurancePlan insurancePlan, ProcedureType procedureType, HttpServletRequest request);
}
