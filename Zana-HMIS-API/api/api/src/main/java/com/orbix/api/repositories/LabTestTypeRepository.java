/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeRepository extends JpaRepository<LabTestType, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<LabTestType> findByName(String name);

	@Query("SELECT t.name FROM LabTestType t")
	List<String> getNames();

	/**
	 * @param value
	 * @return
	 */
	List<LabTestType> findAllByNameContaining(String value);
}
