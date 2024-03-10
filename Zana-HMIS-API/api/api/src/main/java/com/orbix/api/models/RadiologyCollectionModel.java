package com.orbix.api.models;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.RadiologyType;

import lombok.Data;

@Data
public class RadiologyCollectionModel {
	private Long id = null;
	private String description = "";
	private RadiologyType radiologyType = null;
	private PatientBill patientBill = null;
	private Patient patient = null;	
	private String cashier = "";
	private String dateTime = "";
}
