/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orbix.api.domain.Store;
import com.orbix.api.domain.Supplier;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class LocalPurchaseOrderModel {
	public Long id = null;	
	public String no = "";
	public LocalDate orderDate;
	public LocalDate validUntil;
	public String status = "";	
	public String statusDescription = "";
    public Supplier supplier = null;
    public String created = "";
    public String verified = "";
    public String approved = "";
    
    public Store store = null;
    
    public List<LocalPurchaseOrderDetailModel> localPurchaseOrderDetails = new ArrayList<>();
}
