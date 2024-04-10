package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Cashier;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.CashierRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CashierServiceImpl implements CashierService{

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final CashierRepository cashierRepository;
	
	@Override
	public Cashier save(Cashier cashier, HttpServletRequest request) {
		
		Optional<User> u = userRepository.findByCode(cashier.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		if(!u.get().getFirstName().equals(cashier.getFirstName()) || !u.get().getMiddleName().equals(cashier.getMiddleName()) || !u.get().getLastName().equals(cashier.getLastName())){
			throw new InvalidOperationException("Provided names do not match with user account");
		}
		cashier.setNickname(u.get().getNickname());
		cashier.setUser(u.get());

		cashier.setNickname(cashier.getFirstName()+ " "+cashier.getMiddleName()+ " "+cashier.getLastName()+" "+cashier.getCode());
		
		if(cashier.getId() == null) {
			cashier.setCreatedBy(userService.getUser(request).getId());
			cashier.setCreatedOn(dayService.getDay().getId());
			cashier.setCreatedAt(dayService.getTimeStamp());
			
			cashier.setActive(true);
		}
		log.info("Saving new cashier to the database");
		return cashierRepository.save(cashier);
	}

	@Override
	public List<Cashier> getCashiers(HttpServletRequest request) {
		log.info("Fetching all cashiers");
		return cashierRepository.findAll();
	}
	
	@Override
	public List<Cashier> getActiveCashiers(HttpServletRequest request) {
		log.info("Fetching all cashiers");
		return cashierRepository.findAllByActive(true);
	}

	@Override
	public Cashier getCashierByName(String name, HttpServletRequest request) {
		return cashierRepository.findByNickname(name).get();
	}

	@Override
	public Cashier getCashierById(Long id, HttpServletRequest request) {
		return cashierRepository.findById(id).get();
	}

	@Override
	public boolean deleteCashier(Cashier cashier, HttpServletRequest request) {
		/**
		 * Delete a cashier if a cashier is deletable
		 */
		if(allowDeleteCashier(cashier) == false) {
			throw new InvalidOperationException("Deleting this cashier is not allowed");
		}
		cashierRepository.delete(cashier);
		return true;
	}
	
	private boolean allowDeleteCashier(Cashier cashier) {
		/**
		 * Code to check if a cashier is deletable
		 * Returns false if not
		 */
		return false;
	}
}