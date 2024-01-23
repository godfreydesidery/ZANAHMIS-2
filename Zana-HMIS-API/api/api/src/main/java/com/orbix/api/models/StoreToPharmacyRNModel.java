/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.StoreToPharmacyTO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class StoreToPharmacyRNModel {
	public Long id = null;
	public String no = "";
	public LocalDate receivingDate = null;
	public String status = "";
	public String statusDescription = "";
	
	public Pharmacy pharmacy = null;	
	public StoreToPharmacyTO storeToPharmacyTO = null;
    
    public String created;
    public String verified;
    public String approved;

    private List<StoreToPharmacyRNDetailModel> storeToPharmacyRNDetails;
}
