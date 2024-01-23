/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface AdmissionRepository extends JpaRepository<Admission, Long> {

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	Optional<Admission> findByPatientAndStatus(Patient patient, String string);

	/**
	 * @param string
	 * @return
	 */
	List<Admission> findAllByStatus(String string);

	/**
	 * @param patient
	 * @param statuses
	 * @return
	 */
	List<Admission> findAllByPatientAndStatusIn(Patient patient, List<String> statuses);

	/**
	 * @param patient
	 * @param statuses
	 * @return
	 */
	Optional<Admission> findByPatientAndStatusIn(Patient patient, List<String> statuses);

	/**
	 * @param patient
	 * @param string
	 * @param string2
	 * @return
	 */
	Optional<Admission> findByPatientAndStatusOrStatus(Patient patient, String string, String string2);

	/**
	 * @param patient
	 * @param string
	 * @return
	 */
	List<Admission> findAllByPatientAndStatus(Patient patient, String string);

	/**
	 * @param statuses
	 * @return
	 */
	List<Admission> findAllByStatusIn(List<String> statuses);

	/**
	 * @param patient
	 * @return
	 */
	List<Admission> findAllByPatient(Patient patient);

}
