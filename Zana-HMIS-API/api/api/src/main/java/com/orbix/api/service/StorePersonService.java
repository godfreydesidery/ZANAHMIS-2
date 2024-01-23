/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.StorePerson;

/**
 * @author Godfrey
 *
 */
public interface StorePersonService {
	StorePerson save(StorePerson storePerson, HttpServletRequest request);	
	List<StorePerson>getStorePersons(HttpServletRequest request); // return all the clinics
	List<StorePerson>getActiveStorePersons(HttpServletRequest request);
	StorePerson getStorePersonByName(String name, HttpServletRequest request);
	StorePerson getStorePersonById(Long id, HttpServletRequest request);
	boolean deleteStorePerson(StorePerson storePerson, HttpServletRequest request);
}
