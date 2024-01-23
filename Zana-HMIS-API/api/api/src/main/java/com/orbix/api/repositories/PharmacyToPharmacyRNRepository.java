/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.PharmacyToPharmacyRN;
import com.orbix.api.domain.PharmacyToPharmacyTO;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyRNRepository extends JpaRepository <PharmacyToPharmacyRN, Long> {
	
	@Query("SELECT MAX(p.id) FROM PharmacyToPharmacyRN p")
	Long getLastId();

	/**
	 * @param pharmacyToPharmacyTO
	 * @return
	 */
	Optional<PharmacyToPharmacyRN> findByPharmacyToPharmacyTO(PharmacyToPharmacyTO pharmacyToPharmacyTO);

}
