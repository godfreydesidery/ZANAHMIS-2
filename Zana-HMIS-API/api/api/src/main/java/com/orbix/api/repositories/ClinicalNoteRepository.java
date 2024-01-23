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
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	Optional<ClinicalNote> findByConsultation(Consultation consultation);

	/**
	 * @param cons
	 * @return
	 */
	List<ClinicalNote> findAllByConsultationIn(List<Consultation> cons);

	/**
	 * @param admission
	 * @return
	 */
	List<ClinicalNote> findAllByAdmission(Admission admission);

	/**
	 * @param consultation
	 * @return
	 */
	List<ClinicalNote> findAllByConsultation(Consultation consultation);

	/**
	 * @param cons
	 * @param adms
	 * @return
	 */
	List<ClinicalNote> findAllByConsultationInOrAdmissionIn(List<Consultation> cons, List<Admission> adms);

}
