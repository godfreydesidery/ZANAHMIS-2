/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;

/**
 * @author Godfrey
 *
 */
public interface LocalPurchaseOrderDetailRepository extends JpaRepository<LocalPurchaseOrderDetail, Long> {

	/**
	 * @param localPurchaseOrder
	 * @param item
	 * @return
	 */
	List<LocalPurchaseOrderDetail> findAllByLocalPurchaseOrderAndItem(LocalPurchaseOrder localPurchaseOrder, Item item);

}
