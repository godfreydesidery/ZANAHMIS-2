/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.ProcedureType;

/**
 * @author Godfrey
 *
 */
public interface ProcedureTypeRepository extends JpaRepository<ProcedureType, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<ProcedureType> findByName(String name);
	
	@Query("SELECT p.name FROM ProcedureType p")
	List<String> getNames();

	/**
	 * @param value
	 * @return
	 */
	List<ProcedureType> findAllByNameContaining(String value);

}
