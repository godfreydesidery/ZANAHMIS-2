/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "pharmacy_to_pharmacy_r_n_details")
public class PharmacyToPharmacyRNDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double orderedQty;
	private double receivedQty;
	private String status;
	
	@ManyToOne(targetEntity = PharmacyToPharmacyRN.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacy_to_pharmacy_r_n_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private PharmacyToPharmacyRN pharmacyToPharmacyRN;
	
	@ManyToOne(targetEntity = Medicine.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "medicine_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Medicine medicine;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
	private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	//@OneToMany(targetEntity = StoreToPharmacyBatch.class, mappedBy = "storeToPharmacyRNDetail", fetch = FetchType.EAGER, orphanRemoval = true)
    //@Valid
    //@JsonIgnoreProperties({"storeToPharmacyRNDetail", "storeToPharmacyTODetail", "store", "pharmacy"})
	//@Fetch(FetchMode.SUBSELECT)
    //private List<StoreToPharmacyBatch> storeToPharmacyBatches;
}
