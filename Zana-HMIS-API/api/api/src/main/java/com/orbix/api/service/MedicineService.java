/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.WardBed;

/**
 * @author Godfrey
 *
 */
public interface MedicineService {
	Medicine save(Medicine medicine, HttpServletRequest request);	
	List<Medicine>getMedicines(HttpServletRequest request); // return all the medicines
	Medicine getMedicineByCode(String code, HttpServletRequest request);
	Medicine getMedicineByName(String name, HttpServletRequest request);
	Medicine getMedicineById(Long id, HttpServletRequest request);
	boolean deleteMedicine(Medicine medicine, HttpServletRequest request);
	
	Medicine activateMedicine(Medicine medicine, HttpServletRequest request);
	Medicine deactivateMedicine(Medicine medicine, HttpServletRequest request);
}
