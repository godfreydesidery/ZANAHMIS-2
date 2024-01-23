/**
 * 
 */
package com.orbix.api.models;

import java.util.List;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.PharmacyToPharmacyBatch;
//import com.orbix.api.domain.PharmacyToPharmacyBatch;
import com.orbix.api.domain.PharmacyToPharmacyTO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToPharmacyTODetailModel {
public Long id = null;
	
	public double orderedQty = 0;
	public double transferedQty = 0;
	
	public String status = "";
		
    public PharmacyToPharmacyTO pharmacyToPharmacyTO = null;
	
    public Medicine medicine = null;
	
    public String created;
    
    public List<PharmacyToPharmacyBatch> pharmacyToPharmacyBatches;
}
