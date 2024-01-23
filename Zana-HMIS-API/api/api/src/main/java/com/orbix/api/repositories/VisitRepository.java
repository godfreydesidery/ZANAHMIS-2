/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Visit;

/**
 * @author Godfrey
 *
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {

	/**
	 * @param p
	 * @return
	 */
	Optional<Visit> findLastByPatient(Patient p);

	/**
	 * @param patient
	 * @return
	 */
	List<Visit> findAllByPatient(Patient patient);

}
