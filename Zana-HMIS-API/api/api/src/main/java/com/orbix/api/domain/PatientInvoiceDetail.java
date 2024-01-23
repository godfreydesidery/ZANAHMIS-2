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
@Table(name = "patient_invoice_details")
public class PatientInvoiceDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String description;
	@NotNull
	private double qty;
	@NotNull
	private double amount;
	private String status;
	
	@ManyToOne(targetEntity = PatientInvoice.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_invoice_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private PatientInvoice patientInvoice;
	
	@OneToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "patient_bill_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private PatientBill patientBill;

	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdby;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
