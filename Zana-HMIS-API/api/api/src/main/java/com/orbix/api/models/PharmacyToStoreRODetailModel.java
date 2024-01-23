/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToStoreRO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToStoreRODetailModel {
	public Long id = null;
	public Medicine medicine = null;
	public double orderedQty = 0;
	public double receivedQty = 0;
	
	public PharmacyToStoreRO pharmacyToStoreRO = null;
	
	public String created;
}
