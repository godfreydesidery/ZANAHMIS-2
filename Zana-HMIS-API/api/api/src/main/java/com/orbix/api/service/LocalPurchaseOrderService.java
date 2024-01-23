/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.models.LocalPurchaseOrderModel;
import com.orbix.api.models.RecordModel;

/**
 * @author Godfrey
 *
 */
public interface LocalPurchaseOrderService {
	LocalPurchaseOrderModel save(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	boolean saveDetail(LocalPurchaseOrderDetail localPurchaseOrderDetail, HttpServletRequest request);
	
	LocalPurchaseOrderModel verify(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	LocalPurchaseOrderModel approve(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	LocalPurchaseOrderModel submit(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	
	LocalPurchaseOrderModel _return(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	LocalPurchaseOrderModel reject(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestRequestOrderNo();
}
