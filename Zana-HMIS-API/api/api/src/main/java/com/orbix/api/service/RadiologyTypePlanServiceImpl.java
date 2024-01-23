/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.RadiologyTypeInsurancePlanRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RadiologyTypePlanServiceImpl implements RadiologyTypePlanService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final RadiologyTypeInsurancePlanRepository radiologyTypeInsurancePlanRepository;
	
	@Override
	public RadiologyTypeInsurancePlan save(InsurancePlan insurancePlan, RadiologyType radiologyType, double price, HttpServletRequest request) {
		Optional<RadiologyTypeInsurancePlan> p = radiologyTypeInsurancePlanRepository.findByInsurancePlanAndRadiologyType(insurancePlan, radiologyType);
		RadiologyTypeInsurancePlan plan = new RadiologyTypeInsurancePlan();
		if(p.isPresent()) {
			//save existing
			p.get().setPrice(price);
			plan = p.get();
		}else {
			plan.setInsurancePlan(insurancePlan);
			plan.setRadiologyType(radiologyType);
			plan.setPrice(price);
		}
		return radiologyTypeInsurancePlanRepository.save(plan);
	}

	//@Override
	//public List<RadiologyType> getRadiologyTypes() {
		//log.info("Fetching all radiologyTypes");
		//return radiologyTypeRepository.findAll();
	//}

	

	@Override
	public boolean deleteRadiologyTypeInsurancePlan(InsurancePlan insurancePlan, RadiologyType radiologyType, HttpServletRequest request) {
		/**
		 * Delete a radiologyType if a radiologyType is deletable
		 */
		Optional<RadiologyTypeInsurancePlan> p = radiologyTypeInsurancePlanRepository.findByInsurancePlanAndRadiologyType(insurancePlan, radiologyType);
		
		radiologyTypeInsurancePlanRepository.delete(p.get());
		return true;
	}
	
	private boolean allowDeleteRadiologyType(RadiologyType radiologyType) {
		/**
		 * Code to check if a radiologyType is deletable
		 * Returns false if not
		 */
		return false;
	}
}
