/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyToPharmacyTO;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToPharmacyRNModel {
	public Long id = null;
	public String no = "";
	public LocalDate receivingDate = null;
	public String status = "";
	public String statusDescription = "";
	
	public Pharmacy requestingPharmacy = null;
	public Pharmacy deliveringPharmacy = null;
	public PharmacyToPharmacyTO pharmacyToPharmacyTO = null;
    
    public String created;
    public String verified;
    public String approved;

    private List<PharmacyToPharmacyRNDetailModel> pharmacyToPharmacyRNDetails;
}
