/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacyToPharmacyRN;
import com.orbix.api.domain.PharmacyToPharmacyRNDetail;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyRNDetailRepository extends JpaRepository <PharmacyToPharmacyRNDetail, Long> {

	/**
	 * @param receiveNote
	 * @return
	 */
	List<PharmacyToPharmacyRNDetail> findAllByPharmacyToPharmacyRN(PharmacyToPharmacyRN receiveNote);

}
