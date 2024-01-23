/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Medicine> findByName(String name);
	
	@Query("SELECT m.name FROM Medicine m")
	List<String> getNames();

	/**
	 * @param code
	 * @return
	 */
	Optional<Medicine> findByCode(String code);

	/**
	 * @param value
	 * @return
	 */
	List<Medicine> findAllByNameContaining(String value);

	/**
	 * @param value
	 * @param b
	 * @return
	 */
	List<Medicine> findAllByNameContainingAndActive(String value, boolean b);

	

}
