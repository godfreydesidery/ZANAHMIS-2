/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PatientConsumableChartModel {
	private Long id;
	private double qty;
	private String status;
	private String paymentType;
	private String membershipNo;
    private Consultation consultation;	
    private NonConsultation nonConsultation;	
    private Admission admission;
    private Medicine medicine;
    private PatientBill patientBill;
    private Patient patient;
    private Clinician clinician;
    private Nurse nurse;
    private InsurancePlan insurancePlan;
    private String created;
}
