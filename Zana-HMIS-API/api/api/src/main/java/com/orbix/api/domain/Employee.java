package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	
	/**
	 * Demographic information
	 */
	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String nationalIdNo;
	private String phoneNo;
	private String email;
	@NotBlank
	private String gender;
	@NotNull
	private LocalDate dateOfBirth;
	
	private boolean active = true;
	private boolean payable = true;
	
	/**
	 * Address information
	 */
	private String physicalAddress;
	private String postalAddress;
	private String permanentResidence;
	private String temporaryResidence;
	
	/**
	 * Next of kin information
	 */
	private String kinName;
	private String kinPhoneNo;
	
	/**
	 * Employment information
	 */
	@NotBlank
	@Column(unique = true)
	private String tinNo;
	@NotNull
	private double basicSalary;
	@NotNull
	private LocalDate joiningDate;
	private LocalDate employmentDate;
	private LocalDate terminationDate;
	
	/**
	 * Bank information
	 */
	private String bankAccountNo;
	private String bankAccountName;
	private String bankName;
	
	private String socialSecurityNo;
	private String socialSecurityName;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;	
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
