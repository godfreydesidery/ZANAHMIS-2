/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRODetail;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyRODetailRepository extends JpaRepository <PharmacyToPharmacyRODetail, Long> {

	/**
	 * @param pharmacyToPharmacyRO
	 * @param medicine
	 * @return
	 */
	List<PharmacyToPharmacyRODetail> findAllByPharmacyToPharmacyROAndMedicine(PharmacyToPharmacyRO pharmacyToPharmacyRO,
			Medicine medicine);

}
