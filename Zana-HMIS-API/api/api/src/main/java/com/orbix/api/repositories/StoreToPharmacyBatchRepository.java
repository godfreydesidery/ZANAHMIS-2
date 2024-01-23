/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.StoreToPharmacyRNDetail;
import com.orbix.api.domain.StoreToPharmacyTODetail;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyBatchRepository extends JpaRepository<StoreToPharmacyBatch, Long> {

	/**
	 * @param storeToPharmacyTODetail
	 * @return
	 */
	List<StoreToPharmacyBatch> findAllByStoreToPharmacyTODetail(StoreToPharmacyTODetail storeToPharmacyTODetail);

	/**
	 * @param d
	 * @return
	 */
	List<StoreToPharmacyBatch> findAllByStoreToPharmacyRNDetail(StoreToPharmacyRNDetail d);

}
