/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.InsuranceProvider;
import com.orbix.api.domain.Patient;

/**
 * @author Godfrey
 *
 */
public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<InsurancePlan> findByName(String name);
	
	@Query("SELECT i.name FROM InsurancePlan i")
	List<String> getNames();

	/**
	 * @param insuranceProvider
	 * @return
	 */
	List<InsurancePlan> findAllByInsuranceProvider(InsuranceProvider insuranceProvider);

}
