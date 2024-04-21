/**
 * 
 */
package com.orbix.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Consumable;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.WardBed;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.ConsumableRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
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
public class MedicineServiceImpl implements MedicineService{
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final MedicineRepository medicineRepository;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	
	private final ConsumableRepository consumableRepository;
	
	@Override
	public Medicine save(Medicine medicine, HttpServletRequest request) {
		
		medicine.setCode(medicine.getCode().replace(" ", ""));
		medicine.setName(medicine.getName().trim().replaceAll("\\s+", " "));
		
		if(medicine.getId() == null) {
			medicine.setCreatedBy(userService.getUser(request).getId());
			medicine.setCreatedOn(dayService.getDay().getId());
			medicine.setCreatedAt(dayService.getTimeStamp());
			
			medicine.setActive(true);
		}
		
		
		if(medicine.getId() == null) {
			medicine = medicineRepository.save(medicine);
			List<Pharmacy> pharmacies = pharmacyRepository.findAll();
			for(Pharmacy pharmacy : pharmacies) {
				PharmacyMedicine pm = new PharmacyMedicine();
				pm.setMedicine(medicine);
				pm.setPharmacy(pharmacy);
				pm.setStock(0);
				pm = pharmacyMedicineRepository.save(pm);
				
				PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
				pharmacyStockCard.setMedicine(medicine);
				pharmacyStockCard.setPharmacy(pharmacy);
				pharmacyStockCard.setQtyIn(pm.getStock());
				pharmacyStockCard.setQtyOut(0);
				pharmacyStockCard.setBalance(0);
				pharmacyStockCard.setReference("Opening stock, medicine creation");
				
				pharmacyStockCard.setCreatedBy(userService.getUserId(request));
				pharmacyStockCard.setCreatedOn(dayService.getDayId());
				pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
				
				pharmacyStockCardRepository.save(pharmacyStockCard);
			}			
		}	
		
		log.info("Saving new medicine to the database");
		return medicineRepository.save(medicine);
	}

	@Override
	public List<Medicine> getMedicines(HttpServletRequest request) {
		log.info("Fetching all medicines");
		return medicineRepository.findAll();
	}
	
	@Override
	public Medicine getMedicineByCode(String code, HttpServletRequest request) {
		return medicineRepository.findByCode(code).get();
	}

	@Override
	public Medicine getMedicineByName(String name, HttpServletRequest request) {
		return medicineRepository.findByName(name).get();
	}

	@Override
	public Medicine getMedicineById(Long id, HttpServletRequest request) {
		return medicineRepository.findById(id).get();
	}

	@Override
	public boolean deleteMedicine(Medicine medicine, HttpServletRequest request) {
		/**
		 * Delete a medicine if a medicine is deletable
		 */
		if(allowDeleteMedicine(medicine) == false) {
			throw new InvalidOperationException("Deleting this medicine is not allowed");
		}
		medicineRepository.delete(medicine);
		return true;
	}
	
	private boolean allowDeleteMedicine(Medicine medicine) {
		/**
		 * Code to check if a medicine is deletable
		 * Returns false if not
		 */
		return false;
	}
	
	@Override
	public Medicine activateMedicine(Medicine med, HttpServletRequest request) {
		
		Optional<Medicine> medicine = medicineRepository.findById(med.getId());
		if(medicine.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		medicine.get().setActive(true);
		
		return medicineRepository.save(medicine.get());
	}
	
	@Override
	public Medicine deactivateMedicine(Medicine med, HttpServletRequest request) {
		
		Optional<Medicine> medicine = medicineRepository.findById(med.getId());
		if(medicine.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		List<PharmacyMedicine> pharmacyMedicines = pharmacyMedicineRepository.findByMedicine(medicine.get());
		for(PharmacyMedicine pharmacyMedicine : pharmacyMedicines) {
			if(pharmacyMedicine.getStock() != 0) {
				throw new InvalidOperationException("Can not deactivate. Medicine is stocked in pharmacy: " + pharmacyMedicine.getPharmacy().getName());
			}
		}
		
		Optional<Consumable> consumable = consumableRepository.findByMedicine(medicine.get());
		if(consumable.isPresent()) {
			consumableRepository.delete(consumable.get());
		}
		
		medicine.get().setActive(false);
		
		return medicineRepository.save(medicine.get());
	}
}
