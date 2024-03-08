/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@Table(name = "patient_bills")
public class PatientBill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String billItem = "NA";
	@NotBlank
	private String description;
	private double qty = 1;
	@NotNull
	private double amount;
	@NotNull
	private double paid;
	@NotNull
	private double balance;
	@NotBlank
	private String status;
	
	private String paymentType = "CASH";//CASH,DEBIT CARD, CREDIT CARD, MOBILE, INSURANCE
	private String membershipNo;
		
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Patient patient;
	
	@ManyToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "principal_patient_bill_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PatientBill principalPatientBill;
	
	@ManyToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "supplementary_patient_bill_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PatientBill supplementaryPatientBill;
	
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
