/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientInvoice;

/**
 * @author Godfrey
 *
 */
public interface PatientInvoiceRepository extends JpaRepository<PatientInvoice, Long> {

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	Optional<PatientInvoice> findByPatientAndStatus(Patient patient, String string);

	/**
	 * @param patient
	 * @param insurancePlan
	 * @param string
	 * @return
	 */
	Optional<PatientInvoice> findByPatientAndInsurancePlanAndStatus(Patient patient, InsurancePlan insurancePlan,
			String string);

	/**
	 * @param object
	 * @param string
	 * @return
	 */
	List<PatientInvoice> findAllByInsurancePlanAndStatus(Object object, String string);

	/**
	 * @param plans
	 * @param string
	 * @return
	 */
	List<PatientInvoice> findAllByInsurancePlanInAndStatus(List<InsurancePlan> plans, String string);

	/**
	 * @param adm
	 * @return
	 */
	List<PatientInvoice> findAllByAdmission(Admission adm);

	/**
	 * @param con
	 * @param adm
	 * @return
	 */
	List<PatientInvoice> findAllByConsultationOrAdmission(Consultation con, Admission adm);

	/**
	 * @param adm
	 * @param string
	 * @return
	 */
	List<PatientInvoice> findAllByAdmissionAndStatus(Admission adm, String string);

	/**
	 * @param adm
	 * @param con 
	 * @return
	 */
	List<PatientInvoice> findAllByAdmissionOrConsultation(Admission adm, Consultation con);

	/**
	 * @param con
	 * @return
	 */
	List<PatientInvoice> findAllByConsultation(Consultation con);

	List<PatientInvoice> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

	List<PatientInvoice> findAllByCreatedAtBetweenAndAdmissionNot(LocalDateTime atStartOfDay, LocalDateTime plusDays,
			Object object);

	List<PatientInvoice> findAllByPatientAndStatus(Patient p, String string);

	List<PatientInvoice> findAllByCreatedAtBetweenAndInsurancePlan(LocalDateTime atStartOfDay, LocalDateTime plusDays,
			Object object);

	List<PatientInvoice> findAllByStatus(String string);

	List<PatientInvoice> findAllByStatusAndInsurancePlan(String string, Object object);

}
