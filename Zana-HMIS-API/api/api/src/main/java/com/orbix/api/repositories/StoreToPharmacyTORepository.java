/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.StoreToPharmacyTO;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyTORepository extends JpaRepository<StoreToPharmacyTO, Long> {

	@Query("SELECT MAX(s.id) FROM StoreToPharmacyTO s")
	Long getLastId();
	/**
	 * @param pharmacyToStoreRO
	 * @return
	 */
	Optional<StoreToPharmacyTO> findByPharmacyToStoreRO(PharmacyToStoreRO pharmacyToStoreRO);

}
