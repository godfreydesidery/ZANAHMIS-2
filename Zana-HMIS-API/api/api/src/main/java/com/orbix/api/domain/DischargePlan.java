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
@Table(name = "discharge_plans")
public class DischargePlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String history;
	private String investigation;
	private String management;
	private String operationNote;
	private String icuAdmissionNote;
	private String generalRecommendation;
	
	private String status = "PENDING";
	
	@OneToOne(targetEntity = Admission.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "admission_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Admission admission;
	
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
