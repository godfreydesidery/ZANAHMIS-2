/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface RadiologyTypeInsurancePlanRepository extends JpaRepository<RadiologyTypeInsurancePlan, Long> {

	/**
	 * @param insurancePlan
	 * @param radiologyType
	 * @return
	 */
	Optional<RadiologyTypeInsurancePlan> findByInsurancePlanAndRadiologyType(InsurancePlan insurancePlan,
			RadiologyType radiologyType);

	/**
	 * @param radiologyType
	 * @param insurancePlan
	 * @return
	 */
	Optional<RadiologyTypeInsurancePlan> findByRadiologyTypeAndInsurancePlan(RadiologyType radiologyType,
			InsurancePlan insurancePlan);

	/**
	 * @param radiologyType
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<RadiologyTypeInsurancePlan> findByRadiologyTypeAndInsurancePlanAndCovered(RadiologyType radiologyType,
			InsurancePlan insurancePlan, boolean b);

	
}
