/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.Store;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PharmacyToStoreROModel {

	public Long id = null;	
	public String no = "";
	public LocalDate orderDate;
	public LocalDate validUntil;
	public String status = "";	
	public String statusDescription = "";
    public Pharmacy pharmacy = null;
    public String created = "";
    public String verified = "";
    public String approved = "";
    
    public Store store = null;
    
    public List<PharmacyToStoreRODetailModel> pharmacyToStoreRODetails = new ArrayList<>();
}
