/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
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
@Table(name = "store_to_pharmacy_t_o_details")
public class StoreToPharmacyTODetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private double orderedPharmacySKUQty;
	private double transferedPharmacySKUQty;
	@NotNull
	private double transferedStoreSKUQty;
	private String status;
	
	@ManyToOne(targetEntity = StoreToPharmacyTO.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "store_to_pharmacy_t_o_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private StoreToPharmacyTO storeToPharmacyTO;
	
	@ManyToOne(targetEntity = Medicine.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "medicine_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Medicine medicine;
	
	@ManyToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = true)
    @JoinColumn(name = "item_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Item item;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
	private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@OneToMany(targetEntity = StoreToPharmacyBatch.class, mappedBy = "storeToPharmacyTODetail", fetch = FetchType.LAZY, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties({"storeToPharmacyTODetail", "storeToPharmacyRNDetail"})
    private List<StoreToPharmacyBatch> storeToPharmacyBatches;
}
