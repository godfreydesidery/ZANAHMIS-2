/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.LabTestType;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeService {
	LabTestType save(LabTestType labTestType, HttpServletRequest request);	
	List<LabTestType>getLabTestTypes(HttpServletRequest request); // return all the labTestTypes
	LabTestType getLabTestTypeByName(String name, HttpServletRequest request);
	LabTestType getLabTestTypeById(Long id, HttpServletRequest request);
	boolean deleteLabTestType(LabTestType labTestType, HttpServletRequest request);
}
