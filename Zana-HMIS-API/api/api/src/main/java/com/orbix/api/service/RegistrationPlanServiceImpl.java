/**
 * 
 */
package com.orbix.api.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.RegistrationInsurancePlan;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.RegistrationInsurancePlanRepository;
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
public class RegistrationPlanServiceImpl implements RegistrationPlanService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final RegistrationInsurancePlanRepository registrationInsurancePlanRepository;
	
	@Override
	public RegistrationInsurancePlan save(InsurancePlan insurancePlan, double price, HttpServletRequest request) {
		Optional<RegistrationInsurancePlan> p = registrationInsurancePlanRepository.findByInsurancePlan(insurancePlan);
		RegistrationInsurancePlan plan = new RegistrationInsurancePlan();
		if(p.isPresent()) {
			//save existing
			p.get().setRegistrationFee(price);
			plan = p.get();
		}else {
			plan.setInsurancePlan(insurancePlan);
			plan.setRegistrationFee(price);
		}
		return registrationInsurancePlanRepository.save(plan);
	}

	//@Override
	//public List<Registration> getRegistrations() {
		//log.info("Fetching all registrations");
		//return registrationRepository.findAll();
	//}

	

	@Override
	public boolean deleteRegistrationInsurancePlan(InsurancePlan insurancePlan, HttpServletRequest request) {
		/**
		 * Delete a registration if a registration is deletable
		 */
		Optional<RegistrationInsurancePlan> p = registrationInsurancePlanRepository.findByInsurancePlan(insurancePlan);
		
		registrationInsurancePlanRepository.delete(p.get());
		return true;
	}
	
	private boolean allowDeleteRegistration() {
		/**
		 * Code to check if a registration is deletable
		 * Returns false if not
		 */
		return false;
	}
}
