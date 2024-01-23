/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.Store;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyStockCardModel {
	
	Long id = null;
	double qtyIn = 0;
	double qtyOut = 0;
	double balance = 0;
	String reference = "";
	LocalDateTime dateTime = null;
	Medicine medicine = null;
	Pharmacy pharmacy = null;
	
	String created = "";	
}
