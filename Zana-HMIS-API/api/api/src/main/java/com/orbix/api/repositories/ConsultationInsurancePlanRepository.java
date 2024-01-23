/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.ConsultationInsurancePlan;
import com.orbix.api.domain.InsurancePlan;

/**
 * @author Godfrey
 *
 */
public interface ConsultationInsurancePlanRepository extends JpaRepository<ConsultationInsurancePlan, Long> {

	/**
	 * @param plan
	 * @param clinic
	 * @return
	 */
	Optional<ConsultationInsurancePlan> findByInsurancePlanAndClinic(InsurancePlan plan, Clinic clinic);

	/**
	 * @param c
	 * @param insurancePlan
	 * @return
	 */
	Optional<ConsultationInsurancePlan> findByClinicAndInsurancePlan(Clinic c, InsurancePlan insurancePlan);

	/**
	 * @param c
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<ConsultationInsurancePlan> findByClinicAndInsurancePlanAndCovered(Clinic c, InsurancePlan insurancePlan,
			boolean b);

}
