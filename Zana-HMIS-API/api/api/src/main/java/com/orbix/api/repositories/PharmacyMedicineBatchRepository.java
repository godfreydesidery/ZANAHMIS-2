/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyMedicineBatch;

/**
 * @author Godfrey
 *
 */
public interface PharmacyMedicineBatchRepository extends JpaRepository<PharmacyMedicineBatch, Long> {

	/**
	 * @param pharmacy
	 * @param medicine
	 * @param i
	 * @return
	 */
	List<PharmacyMedicineBatch> findAllByPharmacyAndMedicineAndQtyGreaterThan(Pharmacy pharmacy, Medicine medicine,
			double i);

}
