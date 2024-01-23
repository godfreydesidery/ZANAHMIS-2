/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.LocalPurchaseOrder;
import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class LocalPurchaseOrderDetailModel {
	public Long id = null;
	public Item item = null;
	public double qty = 0;
	public double price = 0;
	
	public LocalPurchaseOrder localPurchaseOrder = null;
	
	public String created;
}
