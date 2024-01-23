/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientPaymentDetail;

/**
 * @author Godfrey
 *
 */
public interface PatientPaymentDetailRepository extends JpaRepository <PatientPaymentDetail, Long> {

	/**
	 * @param patientBill
	 * @return
	 */
	Optional<PatientPaymentDetail> findByPatientBill(PatientBill patientBill);

}
