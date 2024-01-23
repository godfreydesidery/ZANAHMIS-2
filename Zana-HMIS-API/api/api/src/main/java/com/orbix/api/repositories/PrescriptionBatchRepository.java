/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.PrescriptionBatch;

/**
 * @author Godfrey
 *
 */
public interface PrescriptionBatchRepository extends JpaRepository<PrescriptionBatch, Long> {

	/**
	 * @param prescription
	 * @return
	 */
	List<PrescriptionBatch> findAllByPrescription(Prescription prescription);

}
