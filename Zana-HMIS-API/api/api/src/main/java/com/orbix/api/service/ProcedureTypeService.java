/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.ProcedureType;

/**
 * @author Godfrey
 *
 */
public interface ProcedureTypeService {
	ProcedureType save(ProcedureType procedureType, HttpServletRequest request);	
	List<ProcedureType>getProcedureTypes(HttpServletRequest request); // return all the procedureTypes
	ProcedureType getProcedureTypeByName(String name, HttpServletRequest request);
	ProcedureType getProcedureTypeById(Long id, HttpServletRequest request);
	boolean deleteProcedureType(ProcedureType procedureType, HttpServletRequest request);
}
