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
@Table(name = "goods_received_note_details")
public class GoodsReceivedNoteDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private double orderedQty;
	private double receivedQty = 0;
	private double price = 0;
	private String status = "NOT VERIFIED";
	
	@ManyToOne(targetEntity = GoodsReceivedNote.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "goods_received_note_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private GoodsReceivedNote goodsReceivedNote;
	
	@ManyToOne(targetEntity = Item.class, fetch = FetchType.EAGER,  optional = false)
    @JoinColumn(name = "item_id", nullable = false , updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)	
    private Item item;
	
	@OneToMany(targetEntity = GoodsReceivedNoteDetailBatch.class, mappedBy = "goodsReceivedNoteDetail", fetch = FetchType.EAGER, orphanRemoval = true)
    @Valid
    @JsonIgnoreProperties("goodsReceivedNoteDetail")
	@Fetch(FetchMode.SUBSELECT)
    private List<GoodsReceivedNoteDetailBatch> goodsReceivedNoteDetailBatches;

}
