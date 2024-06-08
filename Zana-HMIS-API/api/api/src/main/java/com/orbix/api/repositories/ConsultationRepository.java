/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.reports.models.CollectionReport;

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

	List<Consultation> findAllByCreatedAt(LocalDateTime atStartOfDay);

	List<Consultation> findAllByClinicianAndFollowUpAndStatusIn(Clinician clinician, boolean b, List<String> statuses);

	List<Consultation> findAllByStatusOrFollowUp(String string, boolean b);

	Optional<Consultation> findByPatientAndStatusOrFollowUp(Patient patient, String string, boolean b);

	/**
	 * @param p
	 * @param string
	 * @return
	 */
	//Optional<Consultation> findByPatientAndStatus(Patient p, String string);

	
	
	
	/*@Query(
			value = "SELECT\r\n" + 
					"`users`.`id` AS `user_id`,\r\n" +
					"`collections`.`item_name` AS `itemName`,\r\n" +
					"`collections`.`payment_channel` AS `paymentChannel`,\r\n" +
					"SUM(`collections`.`amount`) AS `amount`\r\n" + 
					"FROM\r\n" + 
					"`collections`\r\n" + 
					"JOIN\r\n" + 
					"`users`\r\n" + 
					"ON\r\n" + 
					"`users`.`id`=`collections`.`created_by_user_id`\r\n" + 
					"WHERE\r\n" +
					"`collections`.`created_at` BETWEEN :from AND :to\r\n" +
					"GROUP BY\r\n" + 
					"`itemName`, `paymentChannel`",
					nativeQuery = true					
			)
	List<CollectionReport> getCollectionReportGeneral(LocalDateTime from, LocalDateTime to);*/
	
	//use this query, complete it first
	/*@Query(
			value = "SELECT\r\n" +
					"`clinician`.`nickname` AS `clinicianName`, \r\n" +
					"COUNT(*) AS `patientCount`, \r\n" +
					"FROM\r\n" +
					"`consultations`\r\n" +
					"JOIN\r\n" + 
					"`users`\r\n" +
					"ON\r\n" +
					"`clinician`.`id`=`consultations`.`clinician_id`\r\n" + 
					"JOIN\r\n" +
					"`patient_bills`.`id` = `consultations`.patient_bill_id`\r\n" +
					"WHERE\r\n" +
					"`consultations`.`created_at` BETWEEN :from AND :to AND `patient_bills`.`status` IN()\r\n" +
					"GROUP BY\r\n" + 
					"`clinicianName`",
					nativeQuery = true	
			)*/
}
