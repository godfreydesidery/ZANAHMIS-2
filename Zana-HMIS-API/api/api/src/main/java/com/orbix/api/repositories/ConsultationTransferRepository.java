/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ConsultationTransfer;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface ConsultationTransferRepository extends JpaRepository<ConsultationTransfer, Long> {

	/**
	 * @param string
	 * @return
	 */
	List<ConsultationTransfer> findAllByStatus(String string);

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	List<ConsultationTransfer> findAllByPatientAndStatus(Patient patient, String string);

	/**
	 * @param p
	 * @param string
	 * @return
	 */
	Optional<ConsultationTransfer> findByPatientAndStatus(Patient p, String string);

	/**
	 * @param consultation
	 * @param string
	 * @return
	 */
	Optional<ConsultationTransfer> findByConsultationAndStatus(Consultation consultation, String string);

}
