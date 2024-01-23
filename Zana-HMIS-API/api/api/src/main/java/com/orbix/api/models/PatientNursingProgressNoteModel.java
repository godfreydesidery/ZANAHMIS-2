/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Admission;
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
public class PatientNursingProgressNoteModel {	
	private Long id;
	private String note;	
    private Consultation consultation;	
    private NonConsultation nonConsultation;	
    private Admission admission;	
    private Patient patient;	
    private Nurse nurse;	
	private String created;
}
