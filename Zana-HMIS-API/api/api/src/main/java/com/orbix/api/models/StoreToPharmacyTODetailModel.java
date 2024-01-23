/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.StoreToPharmacyTO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class StoreToPharmacyTODetailModel {

	public Long id = null;
	
	public double orderedPharmacySKUQty = 0;
	public double transferedPharmacySKUQty = 0;
	public double transferedStoreSKUQty = 0;
	
	public String status = "";
		
    public StoreToPharmacyTO storeToPharmacyTO = null;
	
    public Medicine medicine = null;
	
    public Item item = null;
    
    public String created;
    
    public List<StoreToPharmacyBatch> storeToPharmacyBatches;
    
    
	
}
