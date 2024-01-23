/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, Long> {

	/**
	 * @param name
	 * @return
	 */
	InsuranceProvider findByName(String name);
	
	@Query("SELECT i.name FROM InsuranceProvider i")
	List<String> getNames();

}
