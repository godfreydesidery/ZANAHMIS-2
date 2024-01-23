/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.Store;

/**
 * @author Godfrey
 *
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
	/**
	 * @param name
	 * @return
	 */
	Optional<Store> findByName(String name);
	
	@Query("SELECT s.name FROM Store s")
	List<String> getNames();

	/**
	 * @param value
	 * @param value2
	 * @return
	 */
	List<Store> findAllByNameContainingOrCodeContaining(String value, String value2);
}
