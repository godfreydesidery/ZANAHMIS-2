/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.DiagnosisType;

/**
 * @author Godfrey
 *
 */
public interface DiagnosisTypeService {
	DiagnosisType save(DiagnosisType diagnosisType, HttpServletRequest request);	
	List<DiagnosisType>getDiagnosisTypes(HttpServletRequest request); // return all the diagnosisTypes
	DiagnosisType getDiagnosisTypeByName(String name, HttpServletRequest request);
	DiagnosisType getDiagnosisTypeById(Long id, HttpServletRequest request);
	boolean deleteDiagnosisType(DiagnosisType diagnosisType, HttpServletRequest request);
}
