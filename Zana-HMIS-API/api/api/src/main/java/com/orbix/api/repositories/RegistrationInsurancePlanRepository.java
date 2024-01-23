/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RegistrationInsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface RegistrationInsurancePlanRepository extends JpaRepository<RegistrationInsurancePlan, Long> {

	/**
	 * @param insurancePlan
	 * @return
	 */
	Optional<RegistrationInsurancePlan> findByInsurancePlan(InsurancePlan insurancePlan);

	/**
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<RegistrationInsurancePlan> findByInsurancePlanAndCovered(InsurancePlan insurancePlan, boolean b);

}
