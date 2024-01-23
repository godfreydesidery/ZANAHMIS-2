/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.PharmacyToStoreRODetail;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToStoreRODetailRepository extends JpaRepository<PharmacyToStoreRODetail, Long> {

	/**
	 * @param pharmacyToStoreRO
	 * @param medicine
	 * @return
	 */
	List<PharmacyToStoreRODetail> findAllByPharmacyToStoreROAndMedicine(PharmacyToStoreRO pharmacyToStoreRO,
			Medicine medicine);

}
