/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.ProcedureType;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PatientDressingChartModel {	
	private Long id;
	private double qty;	
	private String paymentType;
	private String membershipNo;	
    private Consultation consultation;
    private NonConsultation nonConsultation;	
    private Admission admission;		
    private ProcedureType procedureType;	
    private PatientBill patientBill;	
    private Patient patient;	
    private Clinician clinician;
    private Nurse nurse;	
    private InsurancePlan insurancePlan;
    private String created;	
}
