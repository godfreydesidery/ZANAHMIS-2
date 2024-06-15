package com.orbix.api.models;

import java.time.LocalDateTime;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.WardBed;

import lombok.Data;

@Data
public class AdmissionBedModel {

	Long id = null;
	String status = "";
	WardBed wardBed = null;
	PatientBill patientBill = null;
	Admission admission = null;
	Patient patient = null;
	
	LocalDateTime openedAt = null;
	LocalDateTime closedAt = null;
	
	String admitted;
	
	
}
