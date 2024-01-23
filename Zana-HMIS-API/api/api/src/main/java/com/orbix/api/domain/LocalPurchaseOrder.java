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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@Table(name = "local_purchase_orders")
public class LocalPurchaseOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String no;
	private LocalDate orderDate = LocalDate.now();
	@NotNull
	private LocalDate validUntil;
	
	private String status;
	private String statusDescription;
	
	@ManyToOne(targetEntity = Supplier.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "supplier_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Supplier supplier;
	
	@ManyToOne(targetEntity = Store.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "store_id", nullable = false , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Store store;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = "verified_by_user_id", nullable = true , updatable = true)
    private Long verifiedBy;
	@Column(name = "verified_on_day_id", nullable = true , updatable = true)
    private Long verifiedOn;
	private LocalDateTime verifiedAt;
	
	@Column(name = "approved_by_user_id", nullable = true , updatable = true)
    private Long approvedBy;
	@Column(name = "approved_on_day_id", nullable = true , updatable = true)
    private Long approvedOn;
	private LocalDateTime approvedAt;
	
	@Column(name = "received_by_user_id", nullable = true , updatable = true)
    private Long receivedBy;
	@Column(name = "received_on_day_id", nullable = true , updatable = true)
    private Long receivedOn;
	private LocalDateTime receivedAt;
	
	@OneToMany(targetEntity = LocalPurchaseOrderDetail.class, mappedBy = "localPurchaseOrder", fetch = FetchType.EAGER, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("localPurchaseOrder")
	@Fetch(FetchMode.SUBSELECT)
    private List<LocalPurchaseOrderDetail> localPurchaseOrderDetails;
}
