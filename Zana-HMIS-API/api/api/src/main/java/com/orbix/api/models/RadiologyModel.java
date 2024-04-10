/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Day;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.User;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class RadiologyModel {
	private Long id = null;
	private String result = "";
	private String report = "";
	//private String diagnosis = "";
	private String description = "";
	private Byte[] attachment = null;
	private String status = "";	
	
    private RadiologyType radiologyType;
    private DiagnosisType diagnosisType;
    private PatientBill patientBill;
    private Patient patient;
		
    private String created;			
    private String ordered;			
    private String accepted;	
    private String rejected;
    private String held;
	private String rejectComment;	
    private String verified;
    
    List<RadiologyAttachmentModel> radiologyAttachments = new ArrayList<>();
	
}
