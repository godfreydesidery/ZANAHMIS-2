/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.Pharmacy;

import lombok.Data;

import com.orbix.api.domain.Pharmacy;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToPharmacyROModel {
	public Long id = null;	
	public String no = "";
	public LocalDate orderDate;
	public LocalDate validUntil;
	public String status = "";	
	public String statusDescription = "";
    public Pharmacy requestingPharmacy = null;
    public Pharmacy deliveringPharmacy = null;
    public String created = "";
    public String verified = "";
    public String approved = "";
    
    public Pharmacy store = null;
    
    public List<PharmacyToPharmacyRODetailModel> pharmacyToPharmacyRODetails = new ArrayList<>();
}
