/**
 * 
 */
package com.orbix.api.domain;

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
@Table(name = "items_suppliers")
public class ItemSupplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "item_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Item item;
	
	@ManyToOne(targetEntity = Supplier.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "supplier_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Supplier supplier;
	
	private double costPriceVatIncl = 0;
	private double costPriceVatExcl = 0;
	
	private boolean active = true;
}
