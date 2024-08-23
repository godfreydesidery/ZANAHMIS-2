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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "pharmacy_sale_order_details")
public class PharmacySaleOrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String dosage;
	private String frequency;
	private String route;
	private String days;
	@NotNull
	private double qty;
	private double issued = 0;
	private double balance;
	private String status = "PENDING";
	private String payStatus = "UNPAID";
	private String reference;
	private String instructions;
	
	private String paymentType = "CASH";//CASH,DEBIT CARD, CREDIT CARD, MOBILE, INSURANCE
	
	@ManyToOne(targetEntity = PharmacySaleOrder.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacy_sale_order_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private PharmacySaleOrder pharmacySaleOrder;
	
	@ManyToOne(targetEntity = Medicine.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "medicine_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Medicine medicine;
	
	@ManyToOne(targetEntity = Pharmacy.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "issue_pharmacy_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Pharmacy issuePharmacy;
	
	@OneToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_bill_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private PatientBill patientBill;
	
	@ManyToOne(targetEntity = Pharmacist.class, fetch = FetchType.EAGER,  optional = true)
	@JoinColumn(name = "pharmacist_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Pharmacist pharmacist;
	
	@ManyToOne(targetEntity = Pharmacy.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "sales_pharmacy_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Pharmacy salesPharmacy;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "ordered_by_user_id", nullable = false , updatable = false)
    private Long orderedby;
	@Column(name = "ordered_on_day_id", nullable = false , updatable = false)
    private Long orderedOn;
	private LocalDateTime orderedAt = LocalDateTime.now();

	@Column(name = "accepted_by_user_id", nullable = true , updatable = true)
    private Long acceptedby;
	@Column(name = "accepted_on_day_id", nullable = true , updatable = true)
    private Long acceptedOn;
	private LocalDateTime acceptedAt;
	
	@Column(name = "held_by_user_id", nullable = true , updatable = true)
    private Long heldby;
	@Column(name = "held_on_day_id", nullable = true , updatable = true)
    private Long heldOn;
	private LocalDateTime heldAt;

	@Column(name = "rejected_by_user_id", nullable = true , updatable = true)
    private Long rejectedby;
	@Column(name = "rejected_on_day_id", nullable = true , updatable = true)
    private Long rejectedOn;
	private LocalDateTime rejectedAt;
	private String rejectComment;
	
	@Column(name = "verified_by_user_id", nullable = true , updatable = true)
    private Long verifiedby;
	@Column(name = "verified_on_day_id", nullable = true , updatable = true)
    private Long verifiedOn;
	private LocalDateTime verifiedAt;
	
	@Column(name = "approved_by_user_id", nullable = true , updatable = true)
    private Long approvedBy;
	@Column(name = "approved_on_day_id", nullable = true , updatable = true)
    private Long approvedOn;
	private LocalDateTime approvedAt;
	
	@Column(name = "sold_by_user_id", nullable = true , updatable = true)
    private Long soldBy;
	@Column(name = "sold_on_day_id", nullable = true , updatable = true)
    private Long soldOn;
	private LocalDateTime soldAt;
}
