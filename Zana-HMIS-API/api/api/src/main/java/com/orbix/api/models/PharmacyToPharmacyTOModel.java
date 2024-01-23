/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyToPharmacyRO;

import lombok.Data;

import com.orbix.api.domain.Pharmacy;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToPharmacyTOModel {
	public Long id = null;
	public String no = "";
	public LocalDate orderDate = null;		
	public String status = "";
	public String statusDescription = "";
	
	public Pharmacy requestingPharmacy = null;
	public Pharmacy deliveringPharmacy = null;
	public PharmacyToPharmacyRO pharmacyToPharmacyRO = null;
	
    
    public String created;
    public String verified;
    public String approved;

    public List<PharmacyToPharmacyTODetailModel> pharmacyToPharmacyTODetails;
}
