/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyTO;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyTORepository extends JpaRepository <PharmacyToPharmacyTO, Long> {

	@Query("SELECT MAX(s.id) FROM PharmacyToPharmacyTO s")
	Long getLastId();
	/**
	 * @param pharmacyToPharmacyRO
	 * @return
	 */
	Optional<PharmacyToPharmacyTO> findByPharmacyToPharmacyRO(PharmacyToPharmacyRO pharmacyToPharmacyRO);

}
