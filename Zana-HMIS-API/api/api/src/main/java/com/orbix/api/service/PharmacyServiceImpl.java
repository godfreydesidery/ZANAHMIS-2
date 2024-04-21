/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
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
public class PharmacyServiceImpl implements PharmacyService{

	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final PharmacyRepository pharmacyRepository;
	private final MedicineRepository medicineRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	
	@Override
	public Pharmacy save(Pharmacy pharmacy, HttpServletRequest request) {
		
		pharmacy.setName(pharmacy.getName());
		
		if(pharmacy.getId() == null) {
			pharmacy.setCreatedBy(userService.getUser(request).getId());
			pharmacy.setCreatedOn(dayService.getDay().getId());
			pharmacy.setCreatedAt(dayService.getTimeStamp());
			
			pharmacy.setActive(true);
		}
		
		if(!(pharmacy.getCategory().equals("INPATIENT") || pharmacy.getCategory().equals("OUTPATIENT") || pharmacy.getCategory().equals("ALL"))) {
			throw new InvalidOperationException("Invalid category name");
		}
		
		if(pharmacy.getId() == null) {
			pharmacy = pharmacyRepository.save(pharmacy);
			List<Medicine> medicines = medicineRepository.findAll();
			for(Medicine medicine : medicines) {
				PharmacyMedicine pm = new PharmacyMedicine();
				pm.setMedicine(medicine);
				pm.setPharmacy(pharmacy);
				pm.setStock(0);
				pharmacyMedicineRepository.save(pm);
				
				PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
				pharmacyStockCard.setMedicine(medicine);
				pharmacyStockCard.setPharmacy(pharmacy);
				pharmacyStockCard.setQtyIn(pm.getStock());
				pharmacyStockCard.setQtyOut(0);
				pharmacyStockCard.setBalance(0);
				pharmacyStockCard.setReference("Opening stock, pharmacy registration");
				
				pharmacyStockCard.setCreatedBy(userService.getUserId(request));
				pharmacyStockCard.setCreatedOn(dayService.getDayId());
				pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
				
				pharmacyStockCardRepository.save(pharmacyStockCard);
			}			
		}		
		log.info("Saving new pharmacy to the database");
		return pharmacyRepository.save(pharmacy);
	}

	@Override
	public List<Pharmacy> getPharmacies(HttpServletRequest request) {
		log.info("Fetching all pharmacies");
		return pharmacyRepository.findAll();
	}

	@Override
	public Pharmacy getPharmacyByName(String name, HttpServletRequest request) {
		return pharmacyRepository.findByName(name).get();
	}

	@Override
	public Pharmacy getPharmacyById(Long id, HttpServletRequest request) {
		return pharmacyRepository.findById(id).get();
	}

	@Override
	public boolean deletePharmacy(Pharmacy pharmacy, HttpServletRequest request) {
		/**
		 * Delete a pharmacy if a pharmacy is deletable
		 */
		if(allowDeletePharmacy(pharmacy) == false) {
			throw new InvalidOperationException("Deleting this pharmacy is not allowed");
		}
		pharmacyRepository.delete(pharmacy);
		return true;
	}
	
	private boolean allowDeletePharmacy(Pharmacy pharmacy) {
		/**
		 * Code to check if a pharmacy is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public List<String> getNames(HttpServletRequest request) {
		return pharmacyRepository.getNames();	
	}

	@Override
	public Pharmacy getByName(String pharmacyName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return pharmacyRepository.findByName(pharmacyName).get();
	}

	
	
}
