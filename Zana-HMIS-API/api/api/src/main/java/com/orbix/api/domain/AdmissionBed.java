/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDateTime;
import java.util.Set;

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
@Table(name = "admission_beds")
public class AdmissionBed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String status;
	
	@ManyToOne(targetEntity = WardBed.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "ward_bed_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private WardBed wardBed;
	
	@OneToOne(targetEntity = PatientBill.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_bill_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PatientBill patientBill;
	
	@ManyToOne(targetEntity = Admission.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "admission_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Admission admission;
	
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Patient patient;
	
	private LocalDateTime openedAt = LocalDateTime.now();
	private LocalDateTime closedAt;
}
