/**
 * 
 */
package com.orbix.api.models;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.Theatre;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class ProcedureModel {

	private Long id = null;
	private String note = "";
	private String type = "";
	private LocalTime time = null;
	private String diagnosis = "";
	private LocalDate date = null;
	private double hours = 0;
	private double minutes = 0;
	private String status = "";
	
	private Theatre theatre = null;
	private DiagnosisType diagnosisType = null;
	
    private Consultation consultation = null;	
    private NonConsultation nonConsultation = null;	
    private Admission admission = null;	
    private ProcedureType procedureType = null;		
    private PatientBill patientBill = null;
    private Patient patient = null;
    
    private String created;   
    private String ordered;
    private String accepted;
    private String held;
    private String rejected;
    private String rejectComment;
    private String verified;
}
