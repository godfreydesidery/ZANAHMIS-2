package com.orbix.api.models;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.ProcedureType;

import lombok.Data;

@Data
public class ProcedureCollectionModel {
	private Long id = null;
	private String description = "";
	private ProcedureType procedureType = null;
	private PatientBill patientBill = null;
	private Patient patient = null;	
	private String cashier = "";
	private String dateTime = "";
}
