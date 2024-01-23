/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "referral_plans")
public class ReferralPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String referringDiagnosis;	
	private String history;
	private String investigation;
	private String management;
	private String operationNote;
	private String icuAdmissionNote;
	private String generalRecommendation;
	
	private String status = "PENDING";
	
	@ManyToOne(targetEntity = ExternalMedicalProvider.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "external_medical_provider_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private ExternalMedicalProvider externalMedicalProvider;
	
	@ManyToOne(targetEntity = Admission.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "admission_id", nullable = true , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Admission admission;
	
	@ManyToOne(targetEntity = Consultation.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "consultation_id", nullable = true , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Consultation consultation;
	
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Patient patient;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();	
	
	@Column(name = "approved_by_user_id", nullable = true , updatable = true)
    private Long approvedBy;
	@Column(name = "approved_on_day_id", nullable = true , updatable = true)
    private Long approvedOn;
	private LocalDateTime approvedAt;	
}