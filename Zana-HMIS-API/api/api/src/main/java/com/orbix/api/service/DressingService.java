/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Dressing;

/**
 * @author Godfrey
 *
 */
public interface DressingService {
	Dressing add(Dressing dressing, HttpServletRequest request);
	boolean deleteById(Long id);
}
