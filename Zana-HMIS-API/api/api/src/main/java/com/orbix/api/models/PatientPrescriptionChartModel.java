/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Prescription;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PatientPrescriptionChartModel {	
	private Long id;
	private String dosage;
	private String output;
	private String remark;	
    private Consultation consultation;	
    private NonConsultation nonConsultation;	
    private Admission admission;	
    private Prescription prescription;	
    private Patient patient;	
    private Clinician clinician;	
    private Nurse nurse;	
	private String created;
}
