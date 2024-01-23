/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.ExternalMedicalProvider;

/**
 * @author Godfrey
 *
 */
public interface ExternalMedicalProviderRepository extends JpaRepository<ExternalMedicalProvider, Long> {

	/**
	 * @param name
	 * @return
	 */
	ExternalMedicalProvider findByName(String name);
	
	@Query("SELECT i.name FROM ExternalMedicalProvider i")
	List<String> getNames();

	/**
	 * @param value
	 * @return
	 */
	List<ExternalMedicalProvider> findAllByNameContaining(String value);

}
