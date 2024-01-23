/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToPharmacyBatch;
//import com.orbix.api.domain.PharmacyToPharmacyBatch;
import com.orbix.api.domain.PharmacyToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.StoreToPharmacyRN;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToPharmacyRNDetailModel {
	public Long id = null;
	
	public double orderedQty = 0;
	public double receivedQty = 0;
		
	public String status = "";
		
    public PharmacyToPharmacyRN pharmacyToPharmacyRN = null;
	
    public Medicine medicine = null;
	
    public String created;
    
    public List<PharmacyToPharmacyBatch> pharmacyToPharmacyBatches;
}
