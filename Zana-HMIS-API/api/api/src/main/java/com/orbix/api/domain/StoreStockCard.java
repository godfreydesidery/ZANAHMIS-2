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
@Table(name = "store_stock_cards")
public class StoreStockCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double qtyIn = 0;
	private double qtyOut = 0;
	private double balance = 0;
	private String reference;
    private LocalDateTime dateTime = LocalDateTime.now();
	
	@ManyToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "item_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Item item;
	
	@ManyToOne(targetEntity = Store.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "store_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Store store;
	
	@Column(name = "created_by_user_id", nullable = true , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	//@ManyToOne(targetEntity = Day.class, fetch = FetchType.EAGER,  optional = true)
    //@JoinColumn(name = "day_id", nullable = true , updatable = true)
    //@OnDelete(action = OnDeleteAction.NO_ACTION)	
    //private Day day;
}

