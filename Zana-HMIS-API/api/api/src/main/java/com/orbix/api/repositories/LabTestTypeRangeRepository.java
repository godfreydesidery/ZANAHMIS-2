/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeRangeRepository extends JpaRepository<LabTestTypeRange, Long> {
	@Query("SELECT l.name FROM LabTestTypeRange l")
	List<String> getNames();

	/**
	 * @param labTestType
	 * @return
	 */
	List<LabTestTypeRange> findAllByLabTestType(LabTestType labTestType);

	/**
	 * @param name
	 * @return
	 */
	Optional<LabTestTypeRange> findByName(String name);
}
