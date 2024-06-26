/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.User;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class FinalDiagnosisModel {
	private Long id = null;
	private String description = "";
	
    private DiagnosisType diagnosisType = null;		
    private Consultation consultation = null;		
    private Patient patient= null;
    
    private User user = null;
    
    private String doctor = "";
    
    private String created = "";
}
