/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pharmacy_to_pharmacy_batches")
public class PharmacyToPharmacyBatch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String no;
	
	private double qty;
	
	private LocalDate manufacturedDate;
	private LocalDate expiryDate;
	
	@ManyToOne(targetEntity = PharmacyToPharmacyTODetail.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "pharmacy_to_pharmacy_t_o_detail_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PharmacyToPharmacyTODetail pharmacyToPharmacyTODetail;
	
	@ManyToOne(targetEntity = PharmacyToPharmacyRNDetail.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "pharmacy_to_pharmacy_r_n_detail_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private PharmacyToPharmacyRNDetail pharmacyToPharmacyRNDetail;
	
	@ManyToOne(targetEntity = Medicine.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "medicine_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Medicine medicine;
}
