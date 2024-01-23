/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeInsurancePlan;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeInsurancePlanRepository extends JpaRepository<LabTestTypeInsurancePlan, Long> {

	/**
	 * @param insurancePlan
	 * @param labTestType
	 * @return
	 */
	Optional<LabTestTypeInsurancePlan> findByInsurancePlanAndLabTestType(InsurancePlan insurancePlan,
			LabTestType labTestType);

	/**
	 * @param labTestType
	 * @param insurancePlan
	 * @return
	 */
	Optional<LabTestTypeInsurancePlan> findByLabTestTypeAndInsurancePlan(LabTestType labTestType,
			InsurancePlan insurancePlan);

	/**
	 * @param labTestType
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<LabTestTypeInsurancePlan> findByLabTestTypeAndInsurancePlanAndCovered(LabTestType labTestType,
			InsurancePlan insurancePlan, boolean b);

}
