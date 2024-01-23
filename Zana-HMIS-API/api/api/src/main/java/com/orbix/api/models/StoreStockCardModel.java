/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Store;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class StoreStockCardModel {

	Long id = null;
	double qtyIn = 0;
	double qtyOut = 0;
	double balance = 0;
	String reference = "";
	LocalDateTime dateTime = null;
	Item item = null;
	Store store = null;
	
	String created = "";	
}
