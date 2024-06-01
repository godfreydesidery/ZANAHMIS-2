/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

/**
 * @author Godfrey
 *
 */
public interface PatientBillRepository extends JpaRepository<PatientBill, Long> {

	/**
	 * @param p
	 * @return
	 */
	PatientBill findByPatient(Patient p);

	/**
	 * @param patient
	 * @return
	 */
	List<PatientBill> findAllByPatient(Patient patient);

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	List<PatientBill> findAllByPatientAndStatus(Patient patient, String string);

	/**
	 * @param patient
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<PatientBill> findAllByPatientAndCreatedAtBetween(Patient patient, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	/**
	 * @param patient
	 * @param atStartOfDay
	 * @param plusDays
	 * @param statuses
	 * @return
	 */
	List<PatientBill> findAllByPatientAndCreatedAtBetweenAndStatusIn(Patient patient, LocalDateTime atStartOfDay,
			LocalDateTime plusDays, List<String> statuses);

	List<PatientBill> findAllByCreatedAtBetweenAndStatusIn(LocalDateTime atStartOfDay, LocalDateTime plusDays,
			List<String> statuses);

	List<PatientBill> findAllByCreatedByAndCreatedAtBetweenAndStatusIn(Long id, LocalDateTime atStartOfDay,
			LocalDateTime plusDays, List<String> statuses);

	List<PatientBill> findAllByCreatedAtBetweenAndStatusInAndInsurancePlan(LocalDateTime atStartOfDay,
			LocalDateTime plusDays, List<String> statuses, InsurancePlan insurancePlan);

}
