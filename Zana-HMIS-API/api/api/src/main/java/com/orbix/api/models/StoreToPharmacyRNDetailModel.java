/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.StoreToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyTO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class StoreToPharmacyRNDetailModel {
	public Long id = null;
	
	public double orderedPharmacySKUQty = 0;
	public double receivedPharmacySKUQty = 0;
	public double receivedStoreSKUQty = 0;
		
	public String status = "";
		
    public StoreToPharmacyRN storeToPharmacyRN = null;
	
    public Medicine medicine = null;
	
    public Item item = null;
    
    public String created;
    
    public List<StoreToPharmacyBatch> storeToPharmacyBatches;
}
