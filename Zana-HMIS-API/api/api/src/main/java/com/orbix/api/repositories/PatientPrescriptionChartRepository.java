/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientPrescriptionChart;
import com.orbix.api.domain.Prescription;

/**
 * @author Godfrey
 *
 */
public interface PatientPrescriptionChartRepository extends JpaRepository<PatientPrescriptionChart, Long> {

	/**
	 * @param prescription
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByPrescription(Prescription prescription);

	/**
	 * @param consultation
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param admission
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByAdmission(Admission admission);

	/**
	 * @param consultation
	 * @param prescription
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByConsultationAndPrescription(Consultation consultation,
			Prescription prescription);

	/**
	 * @param nonConsultation
	 * @param prescription
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByNonConsultationAndPrescription(NonConsultation nonConsultation,
			Prescription prescription);

	/**
	 * @param admission
	 * @param prescription
	 * @return
	 */
	List<PatientPrescriptionChart> findAllByAdmissionAndPrescription(Admission admission, Prescription prescription);

}
