/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Prescription;
import com.orbix.api.reports.FastMovingDrugs;

/**
 * @author Godfrey
 *
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	/**
	 * @param consultation
	 * @param statuses
	 * @return
	 */
	List<Prescription> findAllByConsultationAndStatusIn(Consultation consultation, List<String> statuses);

	/**
	 * @param nonConsultation
	 * @param statuses
	 * @return
	 */
	List<Prescription> findAllByNonConsultationAndStatusIn(NonConsultation nonConsultation, List<String> statuses);

	/**
	 * @param admission
	 * @param statuses
	 * @return
	 */
	List<Prescription> findAllByAdmissionAndStatusIn(Admission admission, List<String> statuses);

	/**
	 * @param consultation
	 * @return
	 */
	List<Prescription> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<Prescription> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param consultation
	 * @param medicine
	 * @return
	 */
	boolean existsByConsultationAndMedicine(Consultation consultation, Medicine medicine);

	/**
	 * @param cs
	 * @return
	 */
	List<Prescription> findAllByConsultationIn(List<Consultation> cs);

	/**
	 * @param cs
	 * @param ncs
	 * @return
	 */
	List<Prescription> findAllByConsultationInOrNonConsultationIn(List<Consultation> cs, List<NonConsultation> ncs);

	/**
	 * @param ncs
	 * @return
	 */
	List<Prescription> findAllByNonConsultationIn(List<NonConsultation> ncs);

	/**
	 * @param adm
	 * @return
	 */
	List<Prescription> findAllByAdmissionIn(List<Admission> adm);

	/**
	 * @param patient
	 * @return
	 */
	List<Prescription> findAllByPatient(Patient patient);

	/**
	 * @param admission
	 * @return
	 */
	List<Prescription> findAllByAdmission(Admission admission);

	/**
	 * @param cs
	 * @param ncs
	 * @param adm
	 * @return
	 */
	List<Prescription> findAllByConsultationInOrNonConsultationInOrAdmissionIn(List<Consultation> cs,
			List<NonConsultation> ncs, List<Admission> adm);

	/**
	 * @param c
	 * @return
	 */
	List<Prescription> findByConsultation(Consultation c);

	/**
	 * @param c
	 * @return
	 */
	List<Prescription> findByNonConsultation(NonConsultation c);

	/**
	 * @param medicine
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Prescription> findAllByMedicineAndCreatedAtBetween(Medicine medicine, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	/**
	 * @param medicine
	 * @param atStartOfDay
	 * @param plusDays
	 * @param statuses
	 * @return
	 */
	List<Prescription> findAllByMedicineAndCreatedAtBetweenAndStatusIn(Medicine medicine, LocalDateTime atStartOfDay,
			LocalDateTime plusDays, List<String> statuses);
	
	@Query(
			value = "SELECT\r\n" + 
					"SUM(`prescriptions`.`issued`) AS `issued`,\r\n" + 
					"`medicines`.`code` AS `code`,\r\n" + 
					"`medicines`.`name` AS `name`\r\n" + 
					"FROM\r\n" + 
					"`prescriptions`\r\n"+
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
	List<FastMovingDrugs> getFastMovingDrugs(LocalDateTime atStartOfDay,
			LocalDateTime plusDays);
	
	@Query(
			value = "SELECT\r\n" + 
					"SUM(`prescriptions`.`issued`) AS `issued`,\r\n" + 
					"`medicines`.`code` AS `code`,\r\n" + 
					"`medicines`.`name` AS `name`\r\n" + 
					"FROM\r\n" + 
					"`prescriptions`\r\n"+
					"JOIN\r\n" + 
					"`medicines`\r\n" + 
					"ON\r\n" + 
					"`medicines`.`id`=`prescriptions`.`medicine_id`\r\n" + 
					"WHERE\r\n" +
					"`prescriptions`.`approved_at` BETWEEN :atStartOfDay AND :plusDays\r\n" +
					"GROUP BY\r\n" + 
					"`code`\r\n" +
					"ORDER BY\r\n" +
					"`issued` ASC",
					nativeQuery = true					
			)
	List<FastMovingDrugs> getSlowMovingDrugs(LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

}
