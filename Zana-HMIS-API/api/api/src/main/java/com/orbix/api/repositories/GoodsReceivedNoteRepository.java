/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.Store;

/**
 * @author Godfrey
 *
 */
public interface GoodsReceivedNoteRepository extends JpaRepository<GoodsReceivedNote, Long> {
	
	@Query("SELECT MAX(grn.id) FROM GoodsReceivedNote grn")
	Long getLastId();

	/**
	 * @param statuses
	 * @return
	 */
	List<GoodsReceivedNote> findAllByStatusIn(List<String> statuses);

	/**
	 * @param localPurchaseOrder
	 * @return
	 */
	boolean existsByLocalPurchaseOrder(LocalPurchaseOrder localPurchaseOrder);

	/**
	 * @param localPurchaseOrder
	 * @return
	 */
	Optional<GoodsReceivedNote> findByLocalPurchaseOrder(LocalPurchaseOrder localPurchaseOrder);

	/**
	 * @param store
	 * @param statuses
	 * @return
	 */
	List<GoodsReceivedNote> findAllByStoreAndStatusIn(Store store, List<String> statuses);

	/**
	 * @param localDateTime2 
	 * @param localDateTime 
	 * @param statuses
	 * @return
	 */
	List<GoodsReceivedNote> findAllByApprovedAtBetweenAndStatusIn(LocalDateTime localDateTime, LocalDateTime localDateTime2, List<String> statuses);

	
}
