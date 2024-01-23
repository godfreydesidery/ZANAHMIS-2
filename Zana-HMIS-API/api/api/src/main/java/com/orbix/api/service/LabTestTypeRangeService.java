/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeRange;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeRangeService {
	LabTestTypeRange save(LabTestTypeRange labTestTypeRange, HttpServletRequest request);	
	List<LabTestTypeRange>getLabTestTypeRanges(LabTestType labTestType, HttpServletRequest request); // return all the labTestTypeRanges
	LabTestTypeRange getLabTestTypeRangeByName(String name, HttpServletRequest request);
	LabTestTypeRange getLabTestTypeRangeById(Long id, HttpServletRequest request);
	boolean deleteLabTestTypeRange(LabTestTypeRange labTestTypeRange, HttpServletRequest request);
}
