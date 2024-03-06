/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.reports.FastMovingDrugs;
import com.orbix.api.reports.models.LabTestCollectionReport;

/**
 * @author Godfrey
 *
 */
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

	/**
	 * @param consultation
	 * @param statuses
	 * @return
	 */
	List<LabTest> findAllByConsultationAndStatusIn(Consultation consultation, List<String> statuses);

	/**
	 * @param nonConsultation
	 * @param statuses
	 * @return
	 */
	List<LabTest> findAllByNonConsultationAndStatusIn(NonConsultation nonConsultation, List<String> statuses);

	/**
	 * @param statusToView
	 * @return
	 */
	List<LabTest> findAllByStatusIn(List<String> statusToView);

	/**
	 * @param cs
	 * @param ncs
	 * @return
	 */
	List<LabTest> findAllByConsultationInOrNonConsultationIn(List<Consultation> cs, List<NonConsultation> ncs);

	/**
	 * @param cs
	 * @return
	 */
	List<LabTest> findAllByConsultationIn(List<Consultation> cs);

	/**
	 * @param admission
	 * @param statuses
	 * @return
	 */
	List<LabTest> findAllByAdmissionAndStatusIn(Admission admission, List<String> statuses);

	
	/**
	 * @param consultation
	 * @return
	 */
	List<LabTest> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<LabTest> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param consultation
	 * @param labTestType
	 * @return
	 */
	boolean existsByConsultationAndLabTestType(Consultation consultation, LabTestType labTestType);

	/**
	 * @param nonConsultation
	 * @param labTestType
	 * @return
	 */
	boolean existsByNonConsultationAndLabTestType(NonConsultation nonConsultation, LabTestType labTestType);

	/**
	 * @param ncs
	 * @return
	 */
	List<LabTest> findAllByNonConsultationIn(List<NonConsultation> ncs);

	/**
	 * @param patient
	 * @return
	 */
	List<LabTest> findAllByPatient(Patient patient);

	/**
	 * @param admission
	 * @return
	 */
	List<LabTest> findAllByAdmission(Admission admission);

	/**
	 * @param a
	 * @return
	 */
	List<LabTest> findAllByAdmissionIn(List<Admission> a);

	/**
	 * @param cs
	 * @param ncs
	 * @param adm
	 * @return
	 */
	List<LabTest> findAllByConsultationInOrNonConsultationInOrAdmissionIn(List<Consultation> cs,
			List<NonConsultation> ncs, List<Admission> adm);

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<LabTest> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

	/**
	 * @param c
	 * @return
	 */
	List<LabTest> findByConsultation(Consultation c);

	/**
	 * @param c
	 * @return
	 */
	List<LabTest> findByNonConsultation(NonConsultation c);

	/**
	 * @param labTestType
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<LabTest> findAllByLabTestTypeAndCreatedAtBetween(LabTestType labTestType, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	/**
	 * @param patient
	 * @param statuses
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<LabTest> findAllByPatientAndStatusInAndCreatedAtBetween(Patient patient, List<String> statuses,
			LocalDateTime atStartOfDay, LocalDateTime plusDays);

	/**
	 * @param clinician
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<LabTest> findAllByClinicianAndCreatedAtBetween(Clinician clinician, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	/**
	 * @param statuses
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<LabTest> findAllByStatusInAndCreatedAtBetween(List<String> statuses, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	List<LabTest> findAllByPatientBillIn(List<PatientBill> bills);

	
	/*
	 * 
	 * this query is cumbersome, use alternative method though cumbersome
	@Query(
			value = "SELECT\r\n" + 
					"`lab_tests`.`id` AS `id`\r\n" + 
					"FROM\r\n" + 
					"`lab_tests`\r\n"+
					"JOIN\r\n" + 
					"`medicines`\r\n" + 
					"ON\r\n" + 
					"`medicines`.`id`=`prescriptions`.`medicine_id`\r\n" + 
					"WHERE\r\n" +
					"`prescriptions`.`approved_at` BETWEEN :atStartOfDay AND :plusDays\r\n" +
					"GROUP BY\r\n" + 
					"`code`\r\n" +
					"ORDER BY\r\n" +
					"`issued` DESC",
					nativeQuery = true					
			)
	List<LabTestCollectionReport> getFastMovingCollectionReport(LocalDateTime atStartOfDay,
			LocalDateTime plusDays);
			*/
}
