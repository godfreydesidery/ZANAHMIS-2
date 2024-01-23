/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacyToPharmacyBatch;
import com.orbix.api.domain.PharmacyToPharmacyRNDetail;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyBatchRepository extends JpaRepository<PharmacyToPharmacyBatch, Long> {

	/**
	 * @param d
	 * @return
	 */
	List<PharmacyToPharmacyBatch> findAllByPharmacyToPharmacyRNDetail(PharmacyToPharmacyRNDetail d);

}
