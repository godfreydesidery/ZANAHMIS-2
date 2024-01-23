/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.StoreStockCard;

/**
 * @author Godfrey
 *
 */
public interface StoreStockCardRepository extends JpaRepository<StoreStockCard, Long> {

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<StoreStockCard> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

}
