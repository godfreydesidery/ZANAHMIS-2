/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Item;

/**
 * @author Godfrey
 *
 */
public interface ItemRepository extends JpaRepository <Item, Long> {

	/**
	 * @param itemName
	 * @return
	 */
	Optional<Item> findByName(String itemName);


	@Query("SELECT i.name FROM Item i")
	List<String> getNames();


	/**
	 * @param code
	 * @return
	 */
	Optional<Item> findByCode(String code);


	/**
	 * @param barcode
	 * @return
	 */
	Optional<Item> findByBarcode(String barcode);


	/**
	 * @param value
	 * @param value2
	 * @param value3
	 * @return
	 */
	List<Item> findAllByBarcodeContainingOrCodeContainingOrNameContaining(String value, String value2, String value3);
}
