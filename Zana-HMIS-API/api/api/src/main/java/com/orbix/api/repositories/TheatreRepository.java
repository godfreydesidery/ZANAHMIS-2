/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Theatre;

/**
 * @author Godfrey
 *
 */
public interface TheatreRepository extends JpaRepository<Theatre, Long>{

	/**
	 * @param name
	 * @return
	 */
	Optional<Theatre> findByName(String name);
	
	@Query("SELECT t.name FROM Theatre t")
	List<String> getNames();

}
