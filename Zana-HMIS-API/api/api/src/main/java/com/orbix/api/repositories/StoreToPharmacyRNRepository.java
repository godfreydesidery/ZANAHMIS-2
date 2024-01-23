/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.StoreToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyTO;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyRNRepository extends JpaRepository<StoreToPharmacyRN, Long> {
	@Query("SELECT MAX(s.id) FROM StoreToPharmacyRN s")
	Long getLastId();

	/**
	 * @param storeToPharmacyTO
	 * @return
	 */
	Optional<StoreToPharmacyRN> findByStoreToPharmacyTO(StoreToPharmacyTO storeToPharmacyTO);
}
