/**
 * 
 */
package com.orbix.api.models;

import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.GoodsReceivedNoteDetailBatch;
import com.orbix.api.domain.Item;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class GoodsReceivedNoteDetailModel {
	public Long id = null;
	public Item item = null;
	public double orderedQty = 0;
	public double receivedQty = 0;
	public double price = 0;
	public String status = "";
	
	public GoodsReceivedNote goodsReceivedNote = null;
	
	public List<GoodsReceivedNoteDetailBatch> goodsReceivedNoteDetailBatches = new ArrayList<>();
	
	public String created;
}
