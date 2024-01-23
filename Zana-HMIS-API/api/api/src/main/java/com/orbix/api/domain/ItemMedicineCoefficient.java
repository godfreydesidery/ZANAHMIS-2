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
@Table(name = "item_medicine_coefficients")
public class ItemMedicineCoefficient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double coefficient = 0;
	
	@OneToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "item_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Item item;
	
	private double itemQty = 0;
	private double medicineQty = 0;
	
	@ManyToOne(targetEntity = Medicine.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "medicine_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Medicine medicine;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
	private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
