/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Store;
import com.orbix.api.domain.StoreItem;
import com.orbix.api.domain.StoreItem;

/**
 * @author Godfrey
 *
 */
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {
	/**
	 * @param store
	 * @param item
	 * @return
	 */
	Optional<StoreItem> findByStoreAndItem(Store store, Item item);

	/**
	 * @param store
	 * @return
	 */
	List<StoreItem> findAllByStore(Store store);

	/**
	 * @param item
	 * @return
	 */
	List<StoreItem> findAllByItem(Item item);

	/**
	 * @param item
	 * @return
	 */
	Optional<StoreItem> findByItem(Item item);

	/**
	 * @param store
	 * @return
	 */

}
