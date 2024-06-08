/**
 * 
 */
package com.orbix.api.api;

import java.time.LocalDate;
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

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.AdmissionBed;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Collection;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientInvoice;
import com.orbix.api.domain.PatientInvoiceDetail;
import com.orbix.api.domain.PatientPayment;
import com.orbix.api.domain.PatientPaymentDetail;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.Registration;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.AdmissionBedRepository;
import com.orbix.api.repositories.AdmissionRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.CollectionRepository;
import com.orbix.api.repositories.ConsultationRepository;
import com.orbix.api.repositories.LabTestRepository;
import com.orbix.api.repositories.NonConsultationRepository;
import com.orbix.api.repositories.PatientBillRepository;
import com.orbix.api.repositories.PatientInvoiceDetailRepository;
import com.orbix.api.repositories.PatientInvoiceRepository;
import com.orbix.api.repositories.PatientPaymentDetailRepository;
import com.orbix.api.repositories.PatientPaymentRepository;
import com.orbix.api.repositories.PatientRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.repositories.ProcedureRepository;
import com.orbix.api.repositories.RadiologyRepository;
import com.orbix.api.repositories.RegistrationRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.service.ClinicianService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

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
public class PatientBillResource {
	
	private final PatientBillRepository patientBillRepository;
	private final PatientRepository patientRepository;
	private final ConsultationRepository consultationRepository;
	private final NonConsultationRepository nonConsultationRepository;
	private final AdmissionRepository admissionRepository;
	private final PatientPaymentRepository patientPaymentRepository;
	private final PatientPaymentDetailRepository patientPaymentDetailRepository;
	
	private final LabTestRepository labTestRepository;
	private final ProcedureRepository procedureRepository;
	private final PrescriptionRepository prescriptionRepository;
	private final RadiologyRepository radiologyRepository;
	private final AdmissionBedRepository admissionBedRepository;
	private final RegistrationRepository registrationRepository;
	private final WardBedRepository wardBedRepository;
	
	private final PatientInvoiceRepository patientInvoiceRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	private final CollectionRepository collectionRepository;
	
	
	@GetMapping("/bills/get_registration_bill")
	public ResponseEntity<PatientBill> getRegistrationBill(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		PatientBill bill = registrationRepository.findByPatient(patient).getPatientBill();
		if(!bill.getStatus().equals("UNPAID")) {
			return null;
		}
		return ResponseEntity.ok().body(bill);
	}
	
	@GetMapping("/bills/get_consultation_bill")
	public ResponseEntity<PatientBill> getConsultationBill(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		Optional<Consultation> c = consultationRepository.findByPatientAndStatusIn(patient, statuses);
		if(!c.isPresent()) {
			return null;
		}
		if(!c.get().getPatientBill().getStatus().equals("UNPAID")) {
			return null;
		}
		return ResponseEntity.ok().body(c.get().getPatientBill());
	}
	
	@PostMapping("/bills/confirm_registration_and_consultation_payment")
	public ResponseEntity<PatientBill> confirmRegistrationAndConsultationPayment(
			@RequestParam(name = "patient_id") Long patientId, @RequestParam(name = "total_amount") double totalAmount,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patientId).get();
		
		double amount = 0;
		PatientBill registrationBill = registrationRepository.findByPatient(patient).getPatientBill();
		if(registrationBill.getStatus().equals("UNPAID")) {
			amount = amount + registrationBill.getAmount();
		}
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		List<Consultation> cs = consultationRepository.findAllByPatientAndStatusIn(patient, statuses);
		
		PatientPayment payment = new PatientPayment();
		payment.setAmount(totalAmount);
		
		payment.setCreatedBy(userService.getUser(request).getId());
		payment.setCreatedOn(dayService.getDay().getId());
		payment.setCreatedAt(dayService.getTimeStamp());
		
		payment.setStatus("RECEIVED");
		payment = patientPaymentRepository.save(payment);
				
		if(registrationBill.getStatus().equals("UNPAID")) {
			registrationBill.setBalance(0);
			registrationBill.setPaid(registrationBill.getAmount());
			registrationBill.setStatus("PAID");
			
			registrationBill.setCreatedBy(userService.getUser(request).getId());
			registrationBill.setCreatedOn(dayService.getDay().getId());
			registrationBill.setCreatedAt(dayService.getTimeStamp());
			
			if(registrationBill.getBillItem() == null) {
				registrationBill.setBillItem("Registration");
			}
			
			registrationBill = patientBillRepository.save(registrationBill);
			
			PatientPaymentDetail pd = new PatientPaymentDetail();
			pd.setPatientBill(registrationBill);
			pd.setPatientPayment(payment);
			pd.setDescription("Registration Payment");
			pd.setStatus("RECEIVED");
			
			pd.setCreatedBy(userService.getUser(request).getId());
			pd.setCreatedOn(dayService.getDay().getId());
			pd.setCreatedAt(dayService.getTimeStamp());
			
			patientPaymentDetailRepository.save(pd);	
			
			
			
			Collection collection = new Collection();
			collection.setPatientBill(registrationBill);
			collection.setAmount(registrationBill.getAmount());
			collection.setItemName("Registration");
			collection.setPaymentChannel("Cash");
			collection.setPaymentReferenceNo("NA");
			collection.setPatient(patient);
			collection.setCreatedBy(userService.getUser(request).getId());
			collection.setCreatedOn(dayService.getDay().getId());
			collection.setCreatedAt(dayService.getTimeStamp());
			collectionRepository.save(collection);
			
		}
		
		for(Consultation c : cs) {
			PatientBill bill = c.getPatientBill();
			
			if(bill.getBillItem() == null) {
				bill.setBillItem("Consultation");
				bill = patientBillRepository.save(bill);
			}
			
			
			bill.setBalance(0);
			bill.setPaid(bill.getAmount());
			bill.setStatus("PAID");
			
			bill.setCreatedBy(userService.getUser(request).getId());
			bill.setCreatedOn(dayService.getDay().getId());
			bill.setCreatedAt(dayService.getTimeStamp());
			
			patientBillRepository.save(bill);
			PatientPaymentDetail pd = new PatientPaymentDetail();
			pd.setPatientBill(bill);
			pd.setPatientPayment(payment);
			pd.setDescription("Consultation Payment");
			pd.setStatus("RECEIVED");
			
			pd.setCreatedBy(userService.getUser(request).getId());
			pd.setCreatedOn(dayService.getDay().getId());
			pd.setCreatedAt(dayService.getTimeStamp());
			
			patientPaymentDetailRepository.save(pd);
			
			Collection collection = new Collection();
			collection.setPatientBill(bill);
			collection.setAmount(bill.getAmount());
			collection.setItemName("Consultation");
			collection.setPaymentChannel("Cash");
			collection.setPaymentReferenceNo("NA");
			collection.setPatient(patient);
			collection.setCreatedBy(userService.getUser(request).getId());
			collection.setCreatedOn(dayService.getDay().getId());
			collection.setCreatedAt(dayService.getTimeStamp());
			collectionRepository.save(collection);
			
			amount = amount + bill.getAmount();
		}
		
		if(totalAmount != amount) {
			throw new InvalidOperationException("Could not confirm payment. Insufficient payment");
		}
		
		
		return ResponseEntity.ok().body(null);
	}
	
	@PostMapping("/bills/confirm_bills_payment")
	//@PreAuthorize("hasAnyAuthority('BILL-A')")
	public ResponseEntity<PatientBill> confirmBillsPayment(
			@RequestBody List<PatientBill> bills,
			@RequestParam(name = "total_amount") double totalAmount,
			HttpServletRequest request){
		
		double amount = 0;
		PatientPayment payment = new PatientPayment();
		payment.setAmount(totalAmount);
		
		payment.setCreatedBy(userService.getUser(request).getId());
		payment.setCreatedOn(dayService.getDay().getId());
		payment.setCreatedAt(dayService.getTimeStamp());
		payment.setStatus("RECEIVED");
		
		payment = patientPaymentRepository.save(payment);
		
		for(PatientBill bill : bills) {
			Optional<PatientBill> b = patientBillRepository.findById(bill.getId());
			if(!b.isPresent()) {
				throw new NotFoundException("Bill not found; Bill ID :"+bill.getId().toString());
			}
			if(!(b.get().getStatus().equals("UNPAID") || b.get().getStatus().equals("VERIFIED"))) {
				throw new InvalidOperationException("One or more bills have been paid/covered/canceled. Only unpaid or verified bills can be paid");
			}
			if(b.get().getStatus().equals("UNPAID") || b.get().getStatus().equals("VERIFIED")) {
				b.get().setBalance(0);
				b.get().setPaid(b.get().getAmount());
				b.get().setStatus("PAID");
				
				patientBillRepository.save(b.get());
				PatientPaymentDetail pd = new PatientPaymentDetail();
				pd.setPatientBill(bill);
				pd.setPatientPayment(payment);
				pd.setDescription(b.get().getDescription());
				pd.setStatus("RECEIVED");
				
				pd.setCreatedBy(userService.getUser(request).getId());
				pd.setCreatedOn(dayService.getDay().getId());
				pd.setCreatedAt(dayService.getTimeStamp());
				
				patientPaymentDetailRepository.save(pd);
				
				if(bill.getBillItem() == null) {
					b.get().setBillItem("NA");
					//b.get() = patientBillRepository.save(b.get());
				}
				
				Collection collection = new Collection();
				collection.setPatientBill(bill);
				collection.setAmount(b.get().getAmount());
				collection.setItemName(b.get().getBillItem());
				collection.setPaymentChannel("Cash");
				collection.setPaymentReferenceNo("NA");
				collection.setPatient(b.get().getPatient());
				collection.setCreatedBy(userService.getUser(request).getId());
				collection.setCreatedOn(dayService.getDay().getId());
				collection.setCreatedAt(dayService.getTimeStamp());
				collectionRepository.save(collection);
				
				amount = amount + b.get().getAmount();
				
				List<PatientInvoiceDetail> invds = patientInvoiceDetailRepository.findAllByPatientBill(b.get());
				if(!invds.isEmpty()) {
					for(PatientInvoiceDetail invd : invds) {
						PatientInvoice invoice = invd.getPatientInvoice();
						invd.setStatus("PAID");
						patientInvoiceDetailRepository.save(invd);
						invoice.setAmountPaid(invoice.getAmountPaid() + invd.getAmount());
						patientInvoiceRepository.save(invoice);
					}
				}
				
				List<Admission> adms = admissionRepository.findAllByPatientAndStatus(b.get().getPatient(), "PENDING");
				List<Consultation> cons = consultationRepository.findAllByPatientAndStatus(b.get().getPatient(), "IN-PROCESS");
				if(!adms.isEmpty()) {
					for(Admission adm : adms) {
						adm.setStatus("IN-PROCESS");
						adm = admissionRepository.save(adm);
						adm.getWardBed().setStatus("OCCUPIED");
						wardBedRepository.save(adm.getWardBed());
					}
					for(Consultation con : cons) {
						con.setStatus("SIGNED-OUT");
						consultationRepository.save(con);
					}
				}
			}
		}
		if(amount != totalAmount) {
			throw new InvalidOperationException("Could not confirm payment. Insufficient payment/ amount mismatch");
		}		
		return ResponseEntity.ok().body(null);
	}
	
	@GetMapping("/bills/get_lab_test_bills")
	public ResponseEntity<List<PatientBill>> getLabTestBills(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		List<LabTest> tests = new ArrayList<>();
		Optional<Consultation> c = consultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		Optional<NonConsultation> nc = nonConsultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		//Optional<Admission> a = admissionRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		List<PatientBill> bills = new ArrayList<>();
		if(c.isPresent()) {
			tests = labTestRepository.findAllByConsultationAndStatusIn(c.get(), statuses);
		}else if(nc.isPresent()) {
			tests = labTestRepository.findAllByNonConsultationAndStatusIn(nc.get(), statuses);
		}//else if(a.isPresent()) {
			//tests = labTestRepository.findAllByAdmissionAndStatusIn(a.get(), statuses);
		//}			
		for(LabTest test : tests) {
			if(test.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(test.getPatientBill());
			}
		}		
		return ResponseEntity.ok().body(bills);
	}
	
	@GetMapping("/bills/get_procedure_bills")
	public ResponseEntity<List<PatientBill>> getProcedureBills(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		List<Procedure> procedures = new ArrayList<>();
		Optional<Consultation> c = consultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		Optional<NonConsultation> nc = nonConsultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		//Optional<Admission> a = admissionRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		List<PatientBill> bills = new ArrayList<>();
		if(c.isPresent()) {
			procedures = procedureRepository.findAllByConsultationAndStatusIn(c.get(), statuses);
		}else if(nc.isPresent()) {
			procedures = procedureRepository.findAllByNonConsultationAndStatusIn(nc.get(), statuses);
		}//else if(a.isPresent()) {
			//procedures = procedureRepository.findAllByAdmissionAndStatusIn(a.get(), statuses);
		//}			
		for(Procedure procedure : procedures) {
			if(procedure.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(procedure.getPatientBill());
			}
		}		
		return ResponseEntity.ok().body(bills);
	}
	
	@GetMapping("/bills/get_prescription_bills")
	public ResponseEntity<List<PatientBill>> getPrescriptionBills(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("NOT-GIVEN");
		List<Prescription> prescriptions = new ArrayList<>();
		Optional<Consultation> c = consultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		Optional<NonConsultation> nc = nonConsultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		//Optional<Admission> a = admissionRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		List<PatientBill> bills = new ArrayList<>();
		if(c.isPresent()) {
			prescriptions = prescriptionRepository.findAllByConsultationAndStatusIn(c.get(), statuses);
		}else if(nc.isPresent()) {
			prescriptions = prescriptionRepository.findAllByNonConsultationAndStatusIn(nc.get(), statuses);
		}//else if(a.isPresent()) {
			//prescriptions = prescriptionRepository.findAllByAdmissionAndStatusIn(a.get(), statuses);
		//}			
		for(Prescription prescription : prescriptions) {
			if(prescription.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(prescription.getPatientBill());
			}
		}		
		return ResponseEntity.ok().body(bills);
	}
	
	@GetMapping("/bills/get_radiology_bills")
	public ResponseEntity<List<PatientBill>> getRadiologyBills(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		Patient patient = patientRepository.findById(patient_id).get();
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		List<Radiology> radiologies = new ArrayList<>();
		Optional<Consultation> c = consultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		Optional<NonConsultation> nc = nonConsultationRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		//Optional<Admission> a = admissionRepository.findByPatientAndStatus(patient, "IN-PROCESS");
		List<PatientBill> bills = new ArrayList<>();
		if(c.isPresent()) {
			radiologies = radiologyRepository.findAllByConsultationAndStatusIn(c.get(), statuses);
		}else if(nc.isPresent()) {
			radiologies = radiologyRepository.findAllByNonConsultationAndStatusIn(nc.get(), statuses);
		}//else if(a.isPresent()) {
			//radiologies = radiologyRepository.findAllByAdmissionAndStatusIn(a.get(), statuses);
		//}			
		for(Radiology radiology : radiologies) {
			if(radiology.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(radiology.getPatientBill());
			}
		}		
		return ResponseEntity.ok().body(bills);
	}
	
	@GetMapping("/bills/get_inpatient_bills")
	public ResponseEntity<List<PatientBill>> getPatientBills(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		Optional<Patient> patient = patientRepository.findById(patient_id);
		List<Admission> adms = admissionRepository.findAllByPatientAndStatusIn(patient.get(), statuses);
		if(adms.isEmpty()) {
			throw new NotFoundException("No admission available");
		}
		
		Optional<Admission> admission = admissionRepository.findByPatientAndStatusIn(patient.get(), statuses);
		if(admission.isEmpty()) {
			throw new NotFoundException("No admission available");
		}
		
		List<AdmissionBed> admissionBeds = new ArrayList<>();
		List<LabTest> labTests = new ArrayList<>();
		List<Radiology> radiologies = new ArrayList<>();
		List<Procedure> procedures = new ArrayList<>();
		List<Prescription> prescriptions = new ArrayList<>();
		
		List<PatientBill> bills = new ArrayList<>();
		
		admissionBeds = admissionBedRepository.findAllByAdmission(admission.get());		
		for(AdmissionBed admissionBed : admissionBeds) {
			if(admissionBed.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(admissionBed.getPatientBill());
			}
			if(admissionBed.getPatientBill().getSupplementaryPatientBill() != null) {
				if(admissionBed.getPatientBill().getSupplementaryPatientBill().getStatus().equals("UNPAID")) {
					bills.add(admissionBed.getPatientBill().getSupplementaryPatientBill());
				}
			}
		}	
		
		labTests = labTestRepository.findAllByAdmission(admission.get());		
		for(LabTest labTest : labTests) {
			if(labTest.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(labTest.getPatientBill());
			}
			if(labTest.getPatientBill().getSupplementaryPatientBill() != null) {
				if(labTest.getPatientBill().getSupplementaryPatientBill().getStatus().equals("UNPAID")) {
					bills.add(labTest.getPatientBill().getSupplementaryPatientBill());
				}
			}
		}
		
		radiologies = radiologyRepository.findAllByAdmission(admission.get());		
		for(Radiology radiology : radiologies) {
			if(radiology.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(radiology.getPatientBill());
			}
			if(radiology.getPatientBill().getSupplementaryPatientBill() != null) {
				if(radiology.getPatientBill().getSupplementaryPatientBill().getStatus().equals("UNPAID")) {
					bills.add(radiology.getPatientBill().getSupplementaryPatientBill());
				}
			}
		}
		
		procedures = procedureRepository.findAllByAdmission(admission.get());		
		for(Procedure procedure : procedures) {
			if(procedure.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(procedure.getPatientBill());
			}
			if(procedure.getPatientBill().getSupplementaryPatientBill() != null) {
				if(procedure.getPatientBill().getSupplementaryPatientBill().getStatus().equals("UNPAID")) {
					bills.add(procedure.getPatientBill().getSupplementaryPatientBill());
				}
			}
		}
		
		prescriptions = prescriptionRepository.findAllByAdmission(admission.get());		
		for(Prescription prescription : prescriptions) {
			if(prescription.getPatientBill().getStatus().equals("UNPAID")) {
				bills.add(prescription.getPatientBill());
			}
			if(prescription.getPatientBill().getSupplementaryPatientBill() != null) {
				if(prescription.getPatientBill().getSupplementaryPatientBill().getStatus().equals("UNPAID")) {
					bills.add(prescription.getPatientBill().getSupplementaryPatientBill());
				}
			}
		}
		
		return ResponseEntity.ok().body(bills);
	}
	
}
