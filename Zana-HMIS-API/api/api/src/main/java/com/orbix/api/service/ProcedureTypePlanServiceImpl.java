/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ProcedureTypeInsurancePlanRepository;
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
public class ProcedureTypePlanServiceImpl implements ProcedureTypePlanService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ProcedureTypeInsurancePlanRepository procedureTypeInsurancePlanRepository;
	
	@Override
	public ProcedureTypeInsurancePlan save(InsurancePlan insurancePlan, ProcedureType procedureType, double price, HttpServletRequest request) {
		Optional<ProcedureTypeInsurancePlan> p = procedureTypeInsurancePlanRepository.findByInsurancePlanAndProcedureType(insurancePlan, procedureType);
		ProcedureTypeInsurancePlan plan = new ProcedureTypeInsurancePlan();
		if(p.isPresent()) {
			//save existing
			p.get().setPrice(price);
			plan = p.get();
		}else {
			plan.setInsurancePlan(insurancePlan);
			plan.setProcedureType(procedureType);
			plan.setPrice(price);
		}
		return procedureTypeInsurancePlanRepository.save(plan);
	}

	//@Override
	//public List<ProcedureType> getProcedureTypes() {
		//log.info("Fetching all procedureTypes");
		//return procedureTypeRepository.findAll();
	//}

	

	@Override
	public boolean deleteProcedureTypeInsurancePlan(InsurancePlan insurancePlan, ProcedureType procedureType, HttpServletRequest request) {
		/**
		 * Delete a procedureType if a procedureType is deletable
		 */
		Optional<ProcedureTypeInsurancePlan> p = procedureTypeInsurancePlanRepository.findByInsurancePlanAndProcedureType(insurancePlan, procedureType);
		
		procedureTypeInsurancePlanRepository.delete(p.get());
		return true;
	}
	
	private boolean allowDeleteProcedureType(ProcedureType procedureType) {
		/**
		 * Code to check if a procedureType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
