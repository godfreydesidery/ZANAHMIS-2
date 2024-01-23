/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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
@Table(name = "patients")
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	@NotBlank
	@Column(unique = true)
	private String searchKey;
	/**
	 * Patients basic details
	 */
	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	@NotNull
	private LocalDate dateOfBirth;
	@NotBlank
	private String gender;
	@NotBlank
	private String type;
	/**
	 * Payment method
	 */
	@NotBlank
	private String paymentType = "";
	private String membershipNo = "";
	/**
	 * Contact details
	 */
	private String phoneNo;		
	private String address;
	private String email;
	private String nationality;
	private String nationalId;	
	private String passportNo;
	/**
	 * Next of kin details
	 */
	private String kinFullName;
	private String kinRelationship;
	private String kinPhoneNo;
	/**
	 * To specify whether patient is active or not
	 */
	private boolean active = true;
	/**
	 * 
	 */
	//private Long createdBy;
	//private Long createdOn;
	
	
	@ManyToOne(targetEntity = InsurancePlan.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "insurance_plan_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private InsurancePlan insurancePlan;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdby;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
