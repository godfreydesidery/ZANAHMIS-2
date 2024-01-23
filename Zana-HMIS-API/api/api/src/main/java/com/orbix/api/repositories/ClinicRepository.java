/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Clinic> findByName(String name);
	
	@Query("SELECT c.name FROM Clinic c")
	List<String> getNames();

	/**
	 * @param value
	 * @param value2
	 * @return
	 */
	List<Clinic> findAllByNameContainingOrCodeContaining(String value, String value2);

}
