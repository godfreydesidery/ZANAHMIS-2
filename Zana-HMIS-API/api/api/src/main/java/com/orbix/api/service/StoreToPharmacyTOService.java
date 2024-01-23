/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PharmacyToStoreRO;
import com.orbix.api.domain.PharmacyToStoreRODetail;
import com.orbix.api.domain.StoreToPharmacyTO;
import com.orbix.api.domain.StoreToPharmacyTODetail;
import com.orbix.api.models.PharmacyToStoreROModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.StoreToPharmacyTOModel;

/**
 * @author Godfrey
 *
 */
public interface StoreToPharmacyTOService {
	StoreToPharmacyTOModel createOrder(PharmacyToStoreRO pharmacyToStoreRO, HttpServletRequest request);
	boolean saveDetail(StoreToPharmacyTODetail storeToPharmacyTODetail, HttpServletRequest request);
	
	
	StoreToPharmacyTOModel verify(StoreToPharmacyTO storeToPharmacyTO, HttpServletRequest request);
	StoreToPharmacyTOModel approve(StoreToPharmacyTO storeToPharmacyTO, HttpServletRequest request);
	StoreToPharmacyTOModel issue(StoreToPharmacyTO storeToPharmacyTO, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestTransferOrderNo();
}
