/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRN;
import com.orbix.api.domain.PharmacyToPharmacyRNDetail;
import com.orbix.api.models.RecordModel;
import com.orbix.api.models.PharmacyToPharmacyRNModel;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyRNService {
	PharmacyToPharmacyRNModel createReceivingNote(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	boolean saveDetail(PharmacyToPharmacyRNDetail pharmacyToPharmacyRNDetail, HttpServletRequest request);
	
	
	PharmacyToPharmacyRNModel approveReceivingNote(PharmacyToPharmacyRN pharmacyToPharmacyRN, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestReceivingNoteNo();
}
