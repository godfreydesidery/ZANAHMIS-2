/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.StoreToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyRNDetail;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyRNDetailRepository extends JpaRepository<StoreToPharmacyRNDetail, Long> {

	/**
	 * @param receiveNote
	 * @return
	 */
	List<StoreToPharmacyRNDetail> findAllByStoreToPharmacyRN(StoreToPharmacyRN receiveNote);

}
