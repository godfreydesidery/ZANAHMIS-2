/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Supplier;

/**
 * @author Godfrey
 *
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Supplier> findByName(String name);
	
	@Query("SELECT s.name FROM Supplier s")
	List<String> getNames();

	/**
	 * @param code
	 * @return
	 */
	Optional<Supplier> findByCode(String code);

	/**
	 * @param value
	 * @param value2
	 * @return
	 */
	List<Supplier> findAllByNameContainingOrCodeContaining(String value, String value2);

}
