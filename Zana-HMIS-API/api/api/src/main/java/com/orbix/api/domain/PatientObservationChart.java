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
import javax.validation.constraints.NotNull;

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
@Table(name = "patient_observation_charts")
public class PatientObservationChart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;		
	private String bloodPressure;
	private String meanArterialPressure;
	private String pressure;
	private String temperature;
	private String respiratoryRate;
	private String saturationOxygen;
	
	@ManyToOne(targetEntity = Consultation.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "consultation_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Consultation consultation;
	
	@ManyToOne(targetEntity = NonConsultation.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "non_consultation_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private NonConsultation nonConsultation;
	
	@ManyToOne(targetEntity = Admission.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "admission_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Admission admission;
	
	@ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER,  optional = false)
	@JoinColumn(name = "patient_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Patient patient;
	
	@ManyToOne(targetEntity = Clinician.class, fetch = FetchType.EAGER,  optional = true)
	@JoinColumn(name = "clinician_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Clinician clinician;
	
	@ManyToOne(targetEntity = Nurse.class, fetch = FetchType.EAGER,  optional = true)
	@JoinColumn(name = "nurse_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Nurse nurse;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdby;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
