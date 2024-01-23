/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.LocalPurchaseOrder;

/**
 * @author Godfrey
 *
 */
public interface GoodsReceivedNoteService {
	GoodsReceivedNote create(LocalPurchaseOrder localPurchaseOrder, HttpServletRequest request);
	GoodsReceivedNote approve(GoodsReceivedNote goodsReceivedNote, HttpServletRequest request);
}
