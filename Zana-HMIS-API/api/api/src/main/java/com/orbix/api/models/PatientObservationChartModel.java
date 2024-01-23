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

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PatientObservationChartModel {
	private Long id;		
	private String bloodPressure;
	private String meanArterialPressure;
	private String pressure;
	private String temperature;
	private String respiratoryRate;
	private String saturationOxygen;
    private Consultation consultation;
    private NonConsultation nonConsultation;
    private Admission admission;
    private Patient patient;
    private Clinician clinician;
    private Nurse nurse;
    private String created;
}
