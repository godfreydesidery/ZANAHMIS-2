/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface DiagnosisTypeRepository extends JpaRepository<DiagnosisType, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<DiagnosisType> findByName(String name);
	
	@Query("SELECT d.name FROM DiagnosisType d")
	List<String> getNames();

	/**
	 * @param value
	 * @return
	 */
	List<DiagnosisType> findAllByNameContaining(String value);

	/**
	 * @param value
	 * @param value2
	 * @return
	 */
	List<DiagnosisType> findAllByNameContainingOrCodeContaining(String value, String value2);

}
