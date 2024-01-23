/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientNursingProgressNote;

/**
 * @author Godfrey
 *
 */
public interface PatientNursingProgressNoteRepository extends JpaRepository<PatientNursingProgressNote, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	List<PatientNursingProgressNote> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<PatientNursingProgressNote> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param admission
	 * @return
	 */
	List<PatientNursingProgressNote> findAllByAdmission(Admission admission);

}
