/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.DeceasedNote;

/**
 * @author Godfrey
 *
 */
public interface DeceasedNoteRepository extends JpaRepository<DeceasedNote, Long> {

	/**
	 * @param admission
	 * @return
	 */
	Optional<DeceasedNote> findByAdmission(Admission admission);

	/**
	 * @param consultation
	 * @return
	 */
	Optional<DeceasedNote> findByConsultation(Consultation consultation);

	/**
	 * @param statuses
	 * @return
	 */
	List<DeceasedNote> findAllByStatusIn(List<String> statuses);

	/**
	 * @param string
	 * @return
	 */
	List<DeceasedNote> findAllByStatus(String string);

}
