/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientInvoiceDetail;

/**
 * @author Godfrey
 *
 */
public interface PatientInvoiceDetailRepository extends JpaRepository<PatientInvoiceDetail, Long> {

	/**
	 * @param patientBill
	 * @return
	 */
	Optional<PatientInvoiceDetail> findByPatientBill(PatientBill patientBill);

	/**
	 * @param patientBill
	 * @return
	 */
	List<PatientInvoiceDetail> findAllByPatientBill(PatientBill patientBill);

}
