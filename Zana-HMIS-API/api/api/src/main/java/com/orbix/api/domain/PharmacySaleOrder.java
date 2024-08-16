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

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "pharmacy_sale_orders")
public class PharmacySaleOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String paymentType;//CASH,DEBIT CARD, CREDIT CARD, MOBILE, INSURANCE
	//private String membershipNo;
	@NotBlank
	private String status;
	//private boolean followUp = false;
	
	/**
	 * A patient can have one or more consultations
	 */
	@ManyToOne(targetEntity = PharmacyCustomer.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacy_customer_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PharmacyCustomer pharmacyCustomer;
	
	/**
	 * One consultation has one clinic, i.e. a patient is sent to one clinic in a single consultation
	 */
	@ManyToOne(targetEntity = Pharmacy.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacy_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Pharmacy pharmacy;
	/**
	 * One consultation has one clinician, i.e. a patient is sent to one clinician in a single consultation
	 * However, a patient can be reasigned to another clinician
	 */
	@ManyToOne(targetEntity = Pharmacist.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacist_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Pharmacist pharmacist;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
