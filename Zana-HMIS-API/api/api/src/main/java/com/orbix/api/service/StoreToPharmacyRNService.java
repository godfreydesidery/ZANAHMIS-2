/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.StoreToPharmacyRN;
import com.orbix.api.domain.StoreToPharmacyRNDetail;
import com.orbix.api.domain.StoreToPharmacyTODetail;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.StoreToPharmacyRNModel;
import com.orbix.api.models.StoreToPharmacyTOModel;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyRNService {
	StoreToPharmacyRNModel createReceivingNote(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request);
	boolean saveDetail(StoreToPharmacyRNDetail storeToPharmacyRNDetail, HttpServletRequest request);
	
	
	StoreToPharmacyRNModel approveReceivingNote(StoreToPharmacyRN storeToPharmacyRN, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestReceivingNoteNo();
}
