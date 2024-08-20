/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.Item;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyCustomer;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyMedicineBatch;
import com.orbix.api.domain.PharmacySaleOrder;
import com.orbix.api.domain.PharmacySaleOrderDetail;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.PrescriptionBatch;
import com.orbix.api.domain.Store;
import com.orbix.api.domain.StoreItemBatch;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PharmacySaleOrderModel;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PharmacyCustomerRepository;
import com.orbix.api.repositories.PharmacyMedicineBatchRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacySaleOrderDetailRepository;
import com.orbix.api.repositories.PharmacySaleOrderRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.PrescriptionBatchRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.service.PharmacyService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.PatientService;
import com.orbix.api.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Godfrey
 *
 */
@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class PharmacyResource {

	private final PharmacyRepository pharmacyRepository;
	private final PharmacyService pharmacyService;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	private final MedicineRepository medicineRepository;
	private final PharmacyMedicineBatchRepository pharmacyMedicineBatchRepository;
	private final PrescriptionBatchRepository prescriptionBatchRepository;
	private final PrescriptionRepository prescriptionRepository;
	
	private final PharmacyCustomerRepository pharmacyCustomerRepository;
	private final PharmacySaleOrderRepository pharmacySaleOrderRepository;
	private final PharmacySaleOrderDetailRepository pharmacySaleOrderDetailRepository;
	
	private final PatientService patientService;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/pharmacies")
	public ResponseEntity<List<Pharmacy>>getPharmacies(HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacyService.getPharmacies(request));
	}
	
	@GetMapping("/pharmacies/get")
	public ResponseEntity<Pharmacy> getPharmacy(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacyService.getPharmacyById(id, request));
	}
	
	@GetMapping("/pharmacies/get_by_name")
	public ResponseEntity<Pharmacy> getPharmacyByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacyService.getPharmacyByName(name, request));
	}
	
	@GetMapping("/pharmacies/get_names")
	public ResponseEntity<List<String>> getPharmacyNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = pharmacyService.getNames(request);
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/pharmacies/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Pharmacy>save(
			@RequestBody Pharmacy pharmacy,
			HttpServletRequest request){
		pharmacy.setName(pharmacy.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/pharmacies/save").toUriString());
		return ResponseEntity.created(uri).body(pharmacyService.save(pharmacy, request));
	}
	
	@GetMapping("/pharmacies/load_pharmacies_like")
	public ResponseEntity<List<Pharmacy>> getPharmacyNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Pharmacy> pharmacies = new ArrayList<Pharmacy>();
		pharmacies = pharmacyRepository.findAllByNameContainingOrCodeContaining(value, value);
		return ResponseEntity.ok().body(pharmacies);
	}
	
	@GetMapping("/pharmacies/get_pharmacy_medicine_list")
	public ResponseEntity<List<PharmacyMedicine>> getPharmacyMedicineList(
			@RequestParam(name = "pharmacy_name") String pharmacyName,
			HttpServletRequest request){
		
		Optional<Pharmacy> p = pharmacyRepository.findByName(pharmacyName);
		if(p.isEmpty()) {
			throw new NotFoundException("Pharmacy not found");
		}
		
		List<PharmacyMedicine> pharmacyMedicines = pharmacyMedicineRepository.findAllByPharmacy(p.get());
		List<PharmacyMedicine> pharmacyMedicinesToShow = new ArrayList<>();
		for(PharmacyMedicine pm : pharmacyMedicines) {
			if(pm.getMedicine().isActive() == true) {
				pharmacyMedicinesToShow.add(pm);
			}
		}
		
		
		return ResponseEntity.ok().body(pharmacyMedicinesToShow);
	}
	
	@GetMapping("/pharmacies/get_pharmacy_medicine_batches")
	public ResponseEntity<List<PharmacyMedicineBatch>> getPharmacyMedicineBathList(
			@RequestParam(name = "pharmacy_id") Long pharmacyId,
			@RequestParam(name = "medicine_id") Long medicineId,
			HttpServletRequest request){
		
		Optional<Pharmacy> pharmacy_ = pharmacyRepository.findById(pharmacyId);
		if(pharmacy_.isEmpty()) {
			throw new NotFoundException("Pharmacy not found");
		}
		Optional<Medicine> medicine_ = medicineRepository.findById(medicineId);
		if(medicine_.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		List<PharmacyMedicineBatch> pharmacyMedicineBatches = pharmacyMedicineBatchRepository.findAllByPharmacyAndMedicineAndQtyGreaterThan(pharmacy_.get(), medicine_.get(), 0);
		
		return ResponseEntity.ok().body(pharmacyMedicineBatches);
	}
	
	@GetMapping("/pharmacies/get_prescription_batches")
	public ResponseEntity<List<PrescriptionBatch>> getPrescriptionBathList(
			@RequestParam(name = "prescription_id") Long prescriptionId,
			HttpServletRequest request){
		
		Optional<Prescription> prescription_ = prescriptionRepository.findById(prescriptionId);
		if(prescription_.isEmpty()) {
			throw new NotFoundException("Prescription not found");
		}
		
		
		List<PrescriptionBatch> prescriptionBatches = prescriptionBatchRepository.findAllByPrescription(prescription_.get());
		
		return ResponseEntity.ok().body(prescriptionBatches);
	}
	
	@PostMapping("/pharmacies/update_stock")
	@PreAuthorize("hasAnyAuthority('MEDICINE_STOCK-UPDATE')")
	public void updateStock(
			@RequestBody LPharmacyMedicine pm,
			HttpServletRequest request){
		
		PharmacyMedicine pharmacyMedicine = pharmacyMedicineRepository.findById(pm.getId()).get();
		Pharmacy pharmacy = pharmacyMedicine.getPharmacy();
		Medicine medicine = pharmacyMedicine.getMedicine();
		if(medicine.isActive() == false) {
			throw new InvalidOperationException("Can not update stock. Medicine not active");
		}
		if(pm.getStock() < 0) {
			throw new InvalidOperationException("Negative value is not allowed");
		}
		pharmacyMedicine.setStock(pm.getStock());
		pharmacyMedicineRepository.save(pharmacyMedicine);
		
		PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
		pharmacyStockCard.setMedicine(medicine);
		pharmacyStockCard.setPharmacy(pharmacy);
		pharmacyStockCard.setQtyIn(pm.getStock());
		pharmacyStockCard.setQtyOut(0);
		pharmacyStockCard.setBalance(pm.getStock());
		pharmacyStockCard.setReference("Stock Update");
		
		pharmacyStockCard.setCreatedBy(userService.getUserId(request));
		pharmacyStockCard.setCreatedOn(dayService.getDayId());
		pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
		
		pharmacyStockCardRepository.save(pharmacyStockCard);
		
	}
	
	
	
	//Pharmacy sales
	/**
	 * 
	 * @param request
	 * @return
	 */
	
	@GetMapping("/pharmacies/pharmacy_sale_orders")
	public ResponseEntity<List<PharmacySaleOrder>>getPharmacySalesOrders(HttpServletRequest request){
		return ResponseEntity.ok().body(patientService.getPharmacySaleOrders());
	}
	
	@GetMapping("/pharmacies/pharmacy_customers")
	public ResponseEntity<List<PharmacyCustomer>>getPharmacyCustomers(HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacyCustomerRepository.findAll());
	}
	
	@GetMapping("/medicines/load_pharmacy_customers_like")
	public ResponseEntity<List<PharmacyCustomer>> getPharmacyCustomersNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<PharmacyCustomer> customers = new ArrayList<PharmacyCustomer>();
		customers = pharmacyCustomerRepository.findAllByNameContaining(value);
		return ResponseEntity.ok().body(customers);
	}
	
	@GetMapping("/pharmacies/pharmacy_customers/get")
	public ResponseEntity<PharmacyCustomer> getPharmacyCustomerById(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(pharmacyCustomerRepository.findById(id).get());
	}
	
	
	@PostMapping("/pharmacies/save_pharmacy_sale_order")
	//@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<PharmacySaleOrderModel>savePharmacySaleOrder(
			@RequestBody PharmacySaleOrder pharmacySaleOrder,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/pharmacies/save").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePharmacySaleOrder(pharmacySaleOrder, request));
	}
	
	
	@PostMapping("/pharmacies/save_pharmacy_sale_order_detail")
	public ResponseEntity<PharmacySaleOrderDetail>saveSaleOrderDetail(
			@RequestBody PharmacySaleOrderDetail detail,
			HttpServletRequest request){

		
		Optional<Medicine> medicine_ = medicineRepository.findById(detail.getMedicine().getId());
		if(medicine_.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		
		
		if(detail.getId() == null) {
			detail.setCreatedBy(userService.getUser(request).getId());
			detail.setCreatedOn(dayService.getDay().getId());
			detail.setCreatedAt(dayService.getTimeStamp());	
			
			detail.setOrderedby(userService.getUser(request).getId());
			detail.setOrderedOn(dayService.getDay().getId());
			detail.setOrderedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_prescription").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePharmacySaleOrderDetail(detail, request));
	}
	
	@GetMapping("/pharmacies/load_pharmacy_sale_order_details")
	public ResponseEntity<List<PharmacySaleOrderDetail>> getPharmacySaleOrderDetailsByOrderId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<PharmacySaleOrder> order_ = pharmacySaleOrderRepository.findById(id);
		if(order_.isEmpty()) {
			throw new NotFoundException("Order not found");
		}
		List<PharmacySaleOrderDetail> details = pharmacySaleOrderDetailRepository.findAllByPharmacySaleOrder(order_.get());
		return ResponseEntity.ok().body(details);
	}
	
}

@Data
class LPharmacyMedicine {
	Long id;
	double stock;
}

