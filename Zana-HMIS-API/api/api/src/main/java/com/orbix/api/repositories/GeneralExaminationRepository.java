/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.ClinicalNote;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.GeneralExamination;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface GeneralExaminationRepository extends JpaRepository<GeneralExamination, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	Optional<GeneralExamination> findByConsultation(Consultation consultation);

	/**
	 * @param cons
	 * @return
	 */
	List<GeneralExamination> findAllByConsultationIn(List<Consultation> cons);

	/**
	 * @param admission
	 * @return
	 */
	List<GeneralExamination> findAllByAdmission(Admission admission);

	/**
	 * @param consultation
	 * @return
	 */
	List<GeneralExamination> findAllByConsultation(Consultation consultation);

	/**
	 * @param cons
	 * @param adms
	 * @return
	 */
	List<GeneralExamination> findAllByConsultationInOrAdmissionIn(List<Consultation> cons, List<Admission> adms);

}
