/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Store;
import com.orbix.api.domain.StoreItemBatch;

/**
 * @author Godfrey
 *
 */
public interface StoreItemBatchRepository extends JpaRepository<StoreItemBatch, Long> {

	/**
	 * @param store
	 * @param item
	 * @param minQty
	 * @return
	 */
	List<StoreItemBatch> findAllByStoreAndItemAndQtyGreaterThan(Store store, Item item, double minQty);

}
