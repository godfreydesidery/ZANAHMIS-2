package com.orbix.api.models;

import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

import lombok.Data;

@Data
public class LabTestCollectionModel {
	private Long id = null;
	private String description = "";
	private LabTestType labTestType = null;
	private PatientBill patientBill = null;
	private Patient patient = null;	
}
