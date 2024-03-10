package com.orbix.api.models;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

import lombok.Data;

@Data
public class RegistrationCollectionModel {
	private Long id = null;
	private String description = "";
	private PatientBill patientBill = null;
	private Patient patient = null;	
	private String cashier = "";
	private String dateTime = "";
}
