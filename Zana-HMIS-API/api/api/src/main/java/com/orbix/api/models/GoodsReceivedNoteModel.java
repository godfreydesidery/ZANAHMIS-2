/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.Supplier;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class GoodsReceivedNoteModel {
	public Long id = null;	
	public String no = "";
	public String status = "";	
	public String statusDescription = "";
    public String created = "";
    public String verified = "";
    public String approved = "";
    
    public LocalPurchaseOrder localPurchaseOrder = null;
    
    public List<GoodsReceivedNoteDetailModel> goodsReceivedNoteDetails = new ArrayList<>();
}
