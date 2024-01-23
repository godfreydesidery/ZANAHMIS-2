/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyTO;
import com.orbix.api.domain.PharmacyToPharmacyTODetail;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.PharmacyToPharmacyTOModel;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyTOService {
	PharmacyToPharmacyTOModel createOrder(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	boolean saveDetail(PharmacyToPharmacyTODetail pharmacyToPharmacyTODetail, HttpServletRequest request);
	
	
	PharmacyToPharmacyTOModel verify(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request);
	PharmacyToPharmacyTOModel approve(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request);
	PharmacyToPharmacyTOModel issue(PharmacyToPharmacyTO pharmacyToPharmacyTO, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestTransferOrderNo();
}
