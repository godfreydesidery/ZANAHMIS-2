/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface MedicineInsurancePlanRepository extends JpaRepository<MedicineInsurancePlan, Long> {

	/**
	 * @param insurancePlan
	 * @param medicine
	 * @return
	 */
	Optional<MedicineInsurancePlan> findByInsurancePlanAndMedicine(InsurancePlan insurancePlan, Medicine medicine);

	/**
	 * @param medicine
	 * @param insurancePlan
	 * @return
	 */
	Optional<MedicineInsurancePlan> findByMedicineAndInsurancePlan(Medicine medicine, InsurancePlan insurancePlan);

	/**
	 * @param medicine
	 * @param insurancePlan
	 * @param b
	 * @return
	 */
	Optional<MedicineInsurancePlan> findByMedicineAndInsurancePlanAndCovered(Medicine medicine,
			InsurancePlan insurancePlan, boolean b);

}
