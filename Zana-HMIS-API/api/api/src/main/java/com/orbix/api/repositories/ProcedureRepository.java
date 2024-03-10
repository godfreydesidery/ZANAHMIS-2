/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.ProcedureType;

/**
 * @author Godfrey
 *
 */
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

	/**
	 * @param consultation
	 * @param statuses
	 * @return
	 */
	List<Procedure> findAllByConsultationAndStatusIn(Consultation consultation, List<String> statuses);

	/**
	 * @param nonConsultation
	 * @param statuses
	 * @return
	 */
	List<Procedure> findAllByNonConsultationAndStatusIn(NonConsultation nonConsultation, List<String> statuses);

	/**
	 * @param admission
	 * @param statuses
	 * @return
	 */
	List<Procedure> findAllByAdmissionAndStatusIn(Admission admission, List<String> statuses);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<Procedure> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param consultation
	 * @return
	 */
	List<Procedure> findAllByConsultation(Consultation consultation);

	/**
	 * @param consultation
	 * @param procedureType
	 * @return
	 */
	boolean existsByConsultationAndProcedureType(Consultation consultation, ProcedureType procedureType);

	/**
	 * @param nonConsultation
	 * @param procedureType
	 * @return
	 */
	boolean existsByNonConsultationAndProcedureType(NonConsultation nonConsultation, ProcedureType procedureType);

	/**
	 * @param patient
	 * @return
	 */
	List<Procedure> findAllByPatient(Patient patient);

	/**
	 * @param cs
	 * @return
	 */
	List<Procedure> findAllByConsultationIn(List<Consultation> cs);

	/**
	 * @param ncs
	 * @return
	 */
	List<Procedure> findAllByNonConsultationIn(List<NonConsultation> ncs);

	/**
	 * @param cs
	 * @param ncs
	 * @return
	 */
	List<Procedure> findAllByConsultationInOrNonConsultationIn(List<Consultation> cs, List<NonConsultation> ncs);

	/**
	 * @param admission
	 * @return
	 */
	List<Procedure> findAllByAdmission(Admission admission);

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Procedure> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

	/**
	 * @param c
	 * @return
	 */
	List<Procedure> findByConsultation(Consultation c);

	/**
	 * @param c
	 * @return
	 */
	List<Procedure> findByNonConsultation(NonConsultation c);

	/**
	 * @param clinician
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Procedure> findAllByClinicianAndCreatedAtBetween(Clinician clinician, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	List<Procedure> findAllByPatientBillIn(List<PatientBill> bills);



}
