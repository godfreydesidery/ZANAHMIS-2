/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.WardType;
import com.orbix.api.domain.WardTypeInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface WardTypeInsurancePlanRepository extends JpaRepository <WardTypeInsurancePlan, Long> {

	/**
	 * @param t
	 * @param insurancePlan
	 * @return
	 */
	Optional<WardTypeInsurancePlan> findByWardTypeAndInsurancePlan(WardType t, InsurancePlan insurancePlan);

	/**
	 * @param insurancePlan
	 * @param wardType
	 * @return
	 */
	Optional<WardTypeInsurancePlan> findByInsurancePlanAndWardType(InsurancePlan insurancePlan, WardType wardType);

	/**
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	List<WardTypeInsurancePlan> findByInsurancePlanAndCovered(InsurancePlan insurancePlan, boolean b);
}
