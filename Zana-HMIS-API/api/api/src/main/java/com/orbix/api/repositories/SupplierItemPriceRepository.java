/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.SupplierItemPrice;

/**
 * @author Godfrey
 *
 */
public interface SupplierItemPriceRepository extends JpaRepository<SupplierItemPrice, Long> {

	/**
	 * @param supplier
	 * @return
	 */
	List<SupplierItemPrice> findAllBySupplier(Supplier supplier);

	/**
	 * @param supplier
	 * @param item
	 * @return
	 */
	boolean existsBySupplierAndItem(Supplier supplier, Item item);

	/**
	 * @param supplier
	 * @param item
	 * @return
	 */
	Optional<SupplierItemPrice> findBySupplierAndItem(Supplier supplier, Item item);

	

}
