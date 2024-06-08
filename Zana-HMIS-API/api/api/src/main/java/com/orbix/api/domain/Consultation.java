/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Godfrey
 *
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "consultations")
public class Consultation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String paymentType;//CASH,DEBIT CARD, CREDIT CARD, MOBILE, INSURANCE
	private String membershipNo;
	@NotBlank
	private String status;
	private boolean followUp = false;
	
	/**
	 * A patient can have one or more consultations
	 */
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Patient patient;
	/**
	 * One can only have one bill, i.e. A single consultation can only be
	 * billed once
	 */
	@OneToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_bill_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PatientBill patientBill;
	/**
	 * One consultation has one clinic, i.e. a patient is sent to one clinic in a single consultation
	 */
	@ManyToOne(targetEntity = Clinic.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "clinic_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Clinic clinic;
	/**
	 * One consultation has one clinician, i.e. a patient is sent to one clinician in a single consultation
	 * However, a patient can be reasigned to another clinician
	 */
	@ManyToOne(targetEntity = Clinician.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "clinician_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Clinician clinician;
	
	/**
	 * One patient visit can have one or more consultation i.e. 
	 */
	@ManyToOne(targetEntity = Visit.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "visit_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
	@JsonIgnoreProperties({"createdBy", "createdOn"})
    private Visit visit;
	
	@ManyToOne(targetEntity = InsurancePlan.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "insurance_plan_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private InsurancePlan insurancePlan;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
}
