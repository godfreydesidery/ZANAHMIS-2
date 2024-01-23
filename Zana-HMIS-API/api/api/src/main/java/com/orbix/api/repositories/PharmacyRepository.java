/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Pharmacy;

/**
 * @author Godfrey
 *
 */
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Pharmacy> findByName(String name);
	
	@Query("SELECT p.name FROM Pharmacy p")
	List<String> getNames();

	/**
	 * @param value
	 * @param value2
	 * @return
	 */
	List<Pharmacy> findAllByNameContainingOrCodeContaining(String value, String value2);

}
