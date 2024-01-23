/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.WorkingDiagnosis;

/**
 * @author Godfrey
 *
 */
public interface WorkingDiagnosisRepository extends JpaRepository<WorkingDiagnosis, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	List<WorkingDiagnosis> findAllByConsultation(Consultation consultation);

	/**
	 * @param consultation
	 * @param diagnosisType
	 * @return
	 */
	boolean existsByConsultationAndDiagnosisType(Consultation consultation, DiagnosisType diagnosisType);

	/**
	 * @param consultation
	 * @return
	 */
	List<WorkingDiagnosis> findByConsultation(Consultation consultation);

	/**
	 * @param cons
	 * @return
	 */
	List<WorkingDiagnosis> findAllByConsultationIn(List<Consultation> cons);

	/**
	 * @param admission
	 * @return
	 */
	List<WorkingDiagnosis> findAllByAdmission(Admission admission);

}
