/**
 * 
 */
package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Consumable;
import com.orbix.api.domain.Dressing;

/**
 * @author Godfrey
 *
 */
public interface ConsumableService {
	Consumable add(Consumable consumable, HttpServletRequest request);
	boolean deleteById(Long id);
}
