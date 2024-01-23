/**
 * 
 */
package com.orbix.api.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class SupplierItemPriceList {
	Supplier supplier = null;
	List<SupplierItemPrice> supplierItemPrices = new ArrayList<>();
}
