/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Consumable;
import com.orbix.api.domain.Dressing;
import com.orbix.api.domain.Medicine;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.ConsumableRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.MedicineRepository;
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
public class ConsumableServiceImpl implements ConsumableService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	
	private final ConsumableRepository consumableRepository;
	private final MedicineRepository medicineRepository;
	@Override
	public Consumable add(Consumable d, HttpServletRequest request) {	
		Consumable consumable = new Consumable();
		Optional<Medicine> md = medicineRepository.findById(d.getMedicine().getId());
		
		if(md.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		List<Consumable> cs = consumableRepository.findAllByMedicine(md.get());
		if(!cs.isEmpty()) {
			throw new InvalidOperationException("Consumable already exists");
		}
		consumable.setMedicine(md.get());
		
		consumable.setCreatedBy(userService.getUser(request).getId());
		consumable.setCreatedOn(dayService.getDay().getId());
		consumable.setCreatedAt(dayService.getTimeStamp());
		
		return consumableRepository.save(consumable);
		
	}
	
	@Override
	public boolean deleteById(Long id) {
		
		consumableRepository.deleteById(id);
		
		return true;
	}
}
