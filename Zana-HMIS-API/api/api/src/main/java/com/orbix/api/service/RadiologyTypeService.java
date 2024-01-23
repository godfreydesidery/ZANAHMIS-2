/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.RadiologyType;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
public interface RadiologyTypeService {
	RadiologyType save(RadiologyType radiologyType, HttpServletRequest request);	
	List<RadiologyType>getRadiologyTypes(HttpServletRequest request); // return all the radiologyTypes
	RadiologyType getRadiologyTypeByName(String name, HttpServletRequest request);
	RadiologyType getRadiologyTypeById(Long id, HttpServletRequest request);
	boolean deleteRadiologyType(RadiologyType radiologyType, HttpServletRequest request);
}
