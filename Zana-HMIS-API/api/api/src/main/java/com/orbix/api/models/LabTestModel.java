/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.User;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class LabTestModel {

	private Long id = null;
	private String result = "";
	private String report = "";
	private String description = "";
	private String range = "";
	private String level = "";
	private String unit = "";
	private String status = "";
	private Consultation consultation = null;
	private NonConsultation nonConsultation = null;
	private Admission admission = null;
	private LabTestType labTestType = null;
	private DiagnosisType diagnosisType;
	private PatientBill patientBill = null;
	private Patient patient = null;
	
	String created = "";
	String ordered = "";
	String accepted = "";
	String held = "";
	String rejected = "";
	String rejectComment = "";
	String collected = "";
	String verified = "";	
	
	List<LabTestAttachmentModel> labTestAttachments = new ArrayList<>();
}
