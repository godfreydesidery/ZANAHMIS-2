/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

/**
 * @author Godfrey
 *
 */
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

	/**
	 * @param patient
	 * @return
	 */
	List<Consultation> findAllByPatient(Patient patient);

	/**
	 * @param patient
	 * @param statuses
	 * @return
	 */
	Optional<Consultation> findByPatientAndStatusIn(Patient patient, List<String> statuses);

	/**
	 * @param patient
	 * @param statuses
	 * @return
	 */
	List<Consultation> findAllByPatientAndStatusIn(Patient patient, List<String> statuses);

	/**
	 * @param clinician
	 * @return
	 */
	List<Consultation> findAllByClinician(Clinician clinician);

	/**
	 * @param clinician
	 * @param statuses
	 * @return
	 */
	List<Consultation> findAllByClinicianAndStatusIn(Clinician clinician, List<String> statuses);

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	Optional<Consultation> findByPatientAndStatus(Patient patient, String string);

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	List<Consultation> findAllByPatientAndStatus(Patient patient, String string);

	/**
	 * @param string
	 * @return
	 */
	List<Consultation> findAllByStatus(String string);

	/**
	 * @param conStatuses
	 * @return
	 */
	List<Consultation> findAllByStatusIn(List<String> conStatuses);

	/**
	 * @param atStartOfDay
	 * @param atStartOfDay2
	 * @return
	 */
	List<Consultation> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2);

	/**
	 * @param clinician
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Consultation> findAllByClinicianAndCreatedAtBetween(Clinician clinician, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	List<Consultation> findAllByPatientBillIn(List<PatientBill> bills);

	List<Consultation> findAllByStatusInAndCreatedAtBetween(List<String> statuses, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	/**
	 * @param p
	 * @param string
	 * @return
	 */
	//Optional<Consultation> findByPatientAndStatus(Patient p, String string);

	
}
