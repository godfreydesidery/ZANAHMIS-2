/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacyStockCard;

/**
 * @author Godfrey
 *
 */
public interface PharmacyStockCardRepository extends JpaRepository<PharmacyStockCard, Long> {

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<PharmacyStockCard> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

}
