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
@Table(name = "store_to_pharmacy_batches")
public class StoreToPharmacyBatch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String no;
	
	private double pharmacySKUQty;
	private double storeSKUQty;
	
	private LocalDate manufacturedDate;
	private LocalDate expiryDate;
	
	@ManyToOne(targetEntity = StoreToPharmacyTODetail.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "store_to_pharmacy_t_o_detail_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private StoreToPharmacyTODetail storeToPharmacyTODetail;
	
	@ManyToOne(targetEntity = StoreToPharmacyRNDetail.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "store_to_pharmacy_r_n_detail_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private StoreToPharmacyRNDetail storeToPharmacyRNDetail;
	
	@ManyToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "item_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Item item;
}
