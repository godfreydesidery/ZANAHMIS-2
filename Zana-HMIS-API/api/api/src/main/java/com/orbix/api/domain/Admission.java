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
import javax.validation.constraints.NotBlank;

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
@Table(name = "admissions")
public class Admission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	@NotBlank
	private String paymentType;//CASH,DEBIT CARD, CREDIT CARD, MOBILE, INSURANCE
	private String membershipNo;
	private String status;
	
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Patient patient;
	
	@OneToOne(targetEntity = Visit.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "visit_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Visit visit;
	
	@ManyToOne(targetEntity = InsurancePlan.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "insurance_plan_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private InsurancePlan insurancePlan;
	
	@OneToOne(targetEntity = WardBed.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "ward_bed_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private WardBed wardBed;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = "admitted_by_user_id", nullable = false , updatable = false)
    private Long admittedBy;
	@Column(name = "admitted_on_day_id", nullable = false , updatable = false)
    private Long admittedOn;
	private LocalDateTime admittedAt = LocalDateTime.now();
	
	@Column(name = "discharged_by_user_id", nullable = true , updatable = true)
    private Long dischargedBy;
	@Column(name = "discharged_on_day_id", nullable = true , updatable = true)
    private Long dischargedOn;
	private LocalDateTime dischargedAt;
}
