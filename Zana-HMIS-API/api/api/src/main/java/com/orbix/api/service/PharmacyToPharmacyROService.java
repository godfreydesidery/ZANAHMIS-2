/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.PharmacyToPharmacyRO;
import com.orbix.api.domain.PharmacyToPharmacyRODetail;
import com.orbix.api.models.PharmacyToPharmacyROModel;
import com.orbix.api.models.RecordModel;

/**
 * @author Godfrey
 *
 */
public interface PharmacyToPharmacyROService {
	PharmacyToPharmacyROModel save(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	boolean saveDetail(PharmacyToPharmacyRODetail pharmacyToPharmacyRODetail, HttpServletRequest request);
	
	PharmacyToPharmacyROModel verify(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	PharmacyToPharmacyROModel approve(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	PharmacyToPharmacyROModel submit(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	
	PharmacyToPharmacyROModel _return(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);
	PharmacyToPharmacyROModel reject(PharmacyToPharmacyRO pharmacyToPharmacyRO, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestRequestOrderNo();
}
