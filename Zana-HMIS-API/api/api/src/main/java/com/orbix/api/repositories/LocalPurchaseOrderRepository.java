/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.LocalPurchaseOrder;

/**
 * @author Godfrey
 *
 */
public interface LocalPurchaseOrderRepository extends JpaRepository<LocalPurchaseOrder, Long> {
	
	@Query("SELECT MAX(lpo.id) FROM LocalPurchaseOrder lpo")
	Long getLastId();

	/**
	 * @param no
	 * @return
	 */
	Optional<LocalPurchaseOrder> findByNo(String no);

	/**
	 * @param statuses
	 * @return
	 */
	List<LocalPurchaseOrder> findAllByStatusIn(List<String> statuses);

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @param statuses
	 * @return
	 */
	List<LocalPurchaseOrder> findAllByApprovedAtBetweenAndStatusIn(LocalDateTime atStartOfDay, LocalDateTime plusDays,
			List<String> statuses);
	
	
}
