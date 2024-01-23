/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Day;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.User;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class WorkingDiagnosisModel {
	private Long id = null;
	private String description = "";
	
    private DiagnosisType diagnosisType = null;		
    private Consultation consultation = null;		
    private Patient patient= null;
    
    private String created = "";
	
}
