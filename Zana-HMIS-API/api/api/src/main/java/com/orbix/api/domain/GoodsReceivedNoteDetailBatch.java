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
import javax.validation.constraints.NotBlank;

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
@Table(name = "goods_received_note_detail_batches")
public class GoodsReceivedNoteDetailBatch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String no;
	private LocalDate manufacturedDate;
	private LocalDate expiryDate;
	private double qty = 0;
	
	@ManyToOne(targetEntity = GoodsReceivedNoteDetail.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "goods_received_note_detail_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private GoodsReceivedNoteDetail goodsReceivedNoteDetail;
}
