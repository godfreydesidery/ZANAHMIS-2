/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Theatre;

/**
 * @author Godfrey
 *
 */
public interface TheatreService {
	Theatre save(Theatre theatre, HttpServletRequest request);	
	List<Theatre>getTheatres(HttpServletRequest request); // return all the theatres
	Theatre getTheatreByName(String name, HttpServletRequest request);
	Theatre getTheatreById(Long id, HttpServletRequest request);
	boolean deleteTheatre(Theatre theatre, HttpServletRequest request);
	List<String> getNames(HttpServletRequest request);
	/**
	 * @param theatreName
	 * @return
	 */
	Theatre getByName(String theatreName, HttpServletRequest request);
	
}
