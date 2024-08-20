package com.orbix.api.models;

import com.orbix.api.domain.Pharmacist;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyCustomer;

import lombok.Data;

@Data
public class PharmacySaleOrderModel {
	
	Long id = null;
	String no = "";
	String paymentType = "";
	String status = "";
	PharmacyCustomer pharmacyCustomer = null;
	Pharmacy pharmacy = null;
	Pharmacist pharmacist = null;	
	String created = "";
}
