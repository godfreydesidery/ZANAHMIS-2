/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface ProcedureTypeInsurancePlanRepository extends JpaRepository<ProcedureTypeInsurancePlan, Long> {

	/**
	 * @param insurancePlan
	 * @param procedureType
	 * @return
	 */
	Optional<ProcedureTypeInsurancePlan> findByInsurancePlanAndProcedureType(InsurancePlan insurancePlan,
			ProcedureType procedureType);

	/**
	 * @param procedureType
	 * @param insurancePlan
	 * @return
	 */
	Optional<ProcedureTypeInsurancePlan> findByProcedureTypeAndInsurancePlan(ProcedureType procedureType,
			InsurancePlan insurancePlan);

	/**
	 * @param procedureType
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<ProcedureTypeInsurancePlan> findByProcedureTypeAndInsurancePlanAndCovered(ProcedureType procedureType,
			InsurancePlan insurancePlan, boolean b);


}
