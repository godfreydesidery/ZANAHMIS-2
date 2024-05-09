package com.orbix.api.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.ClinicianPerformance;
import com.orbix.api.domain.Consultation;
import com.orbix.api.reports.models.ClinicianPerformanceReport;
import com.orbix.api.reports.models.CollectionReport;

public interface ClinicianPerformanceRepository extends JpaRepository<ClinicianPerformance, Long> {

	Optional<ClinicianPerformance> findByConsultationAndClinicianAndCheckDate(Consultation consultation,
			Clinician clinician, LocalDate checkDate);

	Optional<ClinicianPerformance> findByAdmissionAndClinicianAndCheckDate(Admission admission, Clinician clinician,
			LocalDate checkDate);

	
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
	
	@Query(
			value = "SELECT\r\n" +
					"COUNT(*) AS `total`,\r\n" +
					"`clinicians`.`nickname` AS `name`\r\n" +
					"FROM\r\n" +
					"`clinician_performances`\r\n" +
					"JOIN\r\n" +
					"`clinicians`\r\n" +
					"ON\r\n" +
					"`clinician_performances`.`clinician_id` = `clinicians`.`id`\r\n" +
					"WHERE\r\n" +
					"`clinician_performances`.`consultation_id` IS NOT NULL AND `clinician_performances`.`created_at` BETWEEN :from AND :to\r\n" +
					"GROUP BY\r\n" +
					"`name`",
					nativeQuery = true
			)
	List<ClinicianPerformanceReport> getClinicianPerformanceOutpatientReport(LocalDateTime from, LocalDateTime to);
	
	@Query(
			value = "SELECT\r\n" +
					"COUNT(*) AS `total`,\r\n" +
					"`clinicians`.`nickname` AS `name`\r\n" +
					"FROM\r\n" +
					"`clinician_performances`\r\n" +
					"JOIN\r\n" +
					"`clinicians`\r\n" +
					"ON\r\n" +
					"`clinician_performances`.`clinician_id` = `clinicians`.`id`\r\n" +
					"WHERE\r\n" +
					"`clinician_performances`.`admission_id` IS NOT NULL AND `clinician_performances`.`created_at` BETWEEN :from AND :to\r\n" +
					"GROUP BY\r\n" +
					"`name`",
					nativeQuery = true
			)
	List<ClinicianPerformanceReport> getClinicianPerformanceInpatientReport(LocalDateTime from, LocalDateTime to);
	

}
