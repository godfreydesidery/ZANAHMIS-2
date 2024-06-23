/**
 * 
 */
package com.orbix.api.models;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.RadiologyType;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PrescriptionModel {
	private Long id = null;
	private String dosage = "";
	private String frequency = "";
	private String route = "";
	private String days = "";
	private double price = 0;
	private double qty = 0;
	private double issued = 0;
	private double balance = 0;
	private double stock = 0;
	private String status = "";
	private String reference;
	private String instructions;
	private Consultation consultation = null;
	private NonConsultation nonConsultation = null;
	private Admission admission = null;
	private Medicine medicine = null;
	private PatientBill patientBill = null;
	private Patient patient = null;
	private Pharmacy issuePharmacy = null;
	
    private String created;			
    private String ordered;			
    private String accepted;
    private String held;
    private String rejected;	
	private String rejectComment;	
    private String verified; 
    private String approved;
}
