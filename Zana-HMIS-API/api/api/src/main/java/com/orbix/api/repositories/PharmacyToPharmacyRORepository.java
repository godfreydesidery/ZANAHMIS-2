/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyToPharmacyRO;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyRORepository extends JpaRepository <PharmacyToPharmacyRO, Long> {
	@Query("SELECT MAX(p.id) FROM PharmacyToPharmacyRO p")
	Long getLastId();

	/**
	 * @param no
	 * @return
	 */
	Optional<PharmacyToPharmacyRO> findByNo(String no);

	/**
	 * @param pharmacy
	 * @param statuses
	 * @return
	 */
	List<PharmacyToPharmacyRO> findByRequestingPharmacyAndStatusIn(Pharmacy pharmacy, List<String> statuses);

	/**
	 * @param pharmacy
	 * @param statuses
	 * @return
	 */
	List<PharmacyToPharmacyRO> findByDeliveringPharmacyAndStatusIn(Pharmacy pharmacy, List<String> statuses);
}
