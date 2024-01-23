/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class PatientModel {
	
	private Long id;
	
	private String no;
	
	private String searchKey;
	
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String type;
	
	private String paymentType = "";
	private String membershipNo = "";
	
	private String phoneNo;		
	private String address;
	private String email;
	private String nationality;
	private String nationalId;	
	private String passportNo;
	
	private String kinFullName;
	private String kinRelationship;
	private String kinPhoneNo;
	
	private boolean active = true;
	
    private InsurancePlan insurancePlan;
	
    private Long createdby;
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
