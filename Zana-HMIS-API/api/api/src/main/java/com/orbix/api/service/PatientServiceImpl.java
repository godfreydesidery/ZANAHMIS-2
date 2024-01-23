/**
 * 
 */
package com.orbix.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientConsumableChart;
import com.orbix.api.domain.PatientDressingChart;
import com.orbix.api.domain.Admission;
import com.orbix.api.domain.AdmissionBed;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ConsultationInsurancePlan;
import com.orbix.api.domain.ConsultationTransfer;
import com.orbix.api.domain.Consumable;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.Dressing;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.PatientInvoice;
import com.orbix.api.domain.PatientInvoiceDetail;
import com.orbix.api.domain.PatientNursingCarePlan;
import com.orbix.api.domain.PatientNursingChart;
import com.orbix.api.domain.PatientNursingProgressNote;
import com.orbix.api.domain.PatientObservationChart;
import com.orbix.api.domain.PatientPrescriptionChart;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.LabTestAttachment;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LabTestTypeInsurancePlan;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.MedicineInsurancePlan;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.ProcedureTypeInsurancePlan;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.RadiologyTypeInsurancePlan;
import com.orbix.api.domain.Registration;
import com.orbix.api.domain.RegistrationInsurancePlan;
import com.orbix.api.domain.Theatre;
import com.orbix.api.domain.Visit;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.WardTypeInsurancePlan;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.AdmissionBedRepository;
import com.orbix.api.repositories.AdmissionRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.CompanyProfileRepository;
import com.orbix.api.repositories.ConsultationInsurancePlanRepository;
import com.orbix.api.repositories.ConsultationRepository;
import com.orbix.api.repositories.ConsultationTransferRepository;
import com.orbix.api.repositories.ConsumableRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DiagnosisTypeRepository;
import com.orbix.api.repositories.DressingRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.LabTestAttachmentRepository;
import com.orbix.api.repositories.PatientInvoiceDetailRepository;
import com.orbix.api.repositories.PatientInvoiceRepository;
import com.orbix.api.repositories.PatientNursingCarePlanRepository;
import com.orbix.api.repositories.PatientNursingChartRepository;
import com.orbix.api.repositories.PatientNursingProgressNoteRepository;
import com.orbix.api.repositories.PatientObservationChartRepository;
import com.orbix.api.repositories.PatientPrescriptionChartRepository;
import com.orbix.api.repositories.LabTestRepository;
import com.orbix.api.repositories.LabTestTypeInsurancePlanRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.MedicineInsurancePlanRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.NonConsultationRepository;
import com.orbix.api.repositories.NurseRepository;
import com.orbix.api.repositories.PatientBillRepository;
import com.orbix.api.repositories.PatientConsumableChartRepository;
import com.orbix.api.repositories.PatientDressingChartRepository;
import com.orbix.api.repositories.PatientRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.repositories.ProcedureRepository;
import com.orbix.api.repositories.ProcedureTypeInsurancePlanRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.repositories.RadiologyRepository;
import com.orbix.api.repositories.RadiologyTypeInsurancePlanRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.RegistrationInsurancePlanRepository;
import com.orbix.api.repositories.RegistrationRepository;
import com.orbix.api.repositories.TheatreRepository;
import com.orbix.api.repositories.VisitRepository;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.repositories.WardTypeInsurancePlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {
	
	private final PatientRepository patientRepository;
	private final PatientBillRepository patientBillRepository;
	private final ConsultationRepository consultationRepository;
	private final NonConsultationRepository nonConsultationRepository;
	private final PatientInvoiceRepository patientInvoiceRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	private final DayRepository dayRepository;
	private final UserService userService;
	private final DayService dayService;
	private final ConsultationInsurancePlanRepository consultationInsurancePlanRepository;
	private final LabTestTypeInsurancePlanRepository labTestTypeInsurancePlanRepository;
	private final MedicineInsurancePlanRepository medicineInsurancePlanRepository;
	private final RadiologyTypeInsurancePlanRepository radiologyTypeInsurancePlanRepository;
	private final ProcedureTypeInsurancePlanRepository procedureTypeInsurancePlanRepository;
	private final LabTestTypeRepository labTestTypeRepository;
	private final LabTestRepository labTestRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final RegistrationInsurancePlanRepository registrationInsurancePlanRepository;
	private final VisitRepository visitRepository;
	private final RadiologyTypeRepository radiologyTypeRepository;
	private final ProcedureTypeRepository procedureTypeRepository;
	private final RadiologyRepository radiologyRepository;
	private final ProcedureRepository procedureRepository;
	private final MedicineRepository medicineRepository;
	private final PrescriptionRepository prescriptionRepository;
	private final RegistrationRepository registrationRepository;
	private final DiagnosisTypeRepository diagnosisTypeRepository;
	private final TheatreRepository theatreRepository;
	private final CompanyProfileRepository companyProfileRepository;
	private final AdmissionRepository admissionRepository;
	private final WardBedRepository wardBedRepository;
	private final AdmissionBedRepository admissionBedRepository;
	private final WardTypeInsurancePlanRepository wardTypeInsurancePlanRepository;
	private final ClinicianRepository clinicianRepository;
	private final DressingRepository dressingRepository;
	private final NurseRepository nurseRepository;
	private final PatientDressingChartRepository patientDressingChartRepository;
	private final ConsumableRepository consumableRepository;
	private final PatientConsumableChartRepository patientConsumableChartRepository;
	private final PatientObservationChartRepository patientObservationChartRepository;
	private final PatientPrescriptionChartRepository patientPrescriptionChartRepository;
	private final PatientNursingChartRepository patientNursingChartRepository;
	private final PatientNursingProgressNoteRepository patientNursingProgressNoteRepository;
	private final PatientNursingCarePlanRepository patientNursingCarePlanRepository;
	private final ConsultationTransferRepository consultationTransferRepository;
	private final LabTestAttachmentRepository labTestAttachmentRepository;
	
	@Override
	public List<Patient> getAll() {
		return patientRepository.findAll();
	}
	
	@Override
	public Patient findBySearchKey(String key) {
		Optional<Patient> p = patientRepository.findBySearchKey(key);
		if(!p.isPresent()) {
			throw new NotFoundException("Patient not found");
		}
		return p.get();
	}
	
	@Override
	public Patient doRegister(Patient p, HttpServletRequest request) {
		// TODO Auto-generated method stub
		double regFee = 0;
		List<CompanyProfile> cps = companyProfileRepository.findAll();
		for(CompanyProfile cp : cps) {
			regFee = cp.getRegistrationFee();
		}
		/**
		 * Save patient after validating credentials
		 */
		p.setCreatedby(userService.getUser(request).getId());
		p.setCreatedOn(dayService.getDay().getId());
		p.setCreatedAt(dayService.getTimeStamp());
		
		Patient patient = patientRepository.save(p);
		
		
		/*
		 * 
		 */	
		/**
		 * generate patient unique file no// change this to conventional no, this is only for starting
		 */
		patient.setNo("MRNO/"+String.valueOf(Year.now().getValue())+"/"+ patient.getId().toString());
		/**
		 * Create a search key; to sanitize searchkey later
		 */
		patient.setSearchKey(String.valueOf(Math.random()));
		patient = patientRepository.save(patient);//generate search key, 
		patient.setSearchKey(createSearchKey(patient.getNo(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getPhoneNo()));
		patient.setSearchKey(Sanitizer.sanitizeString(patient.getSearchKey()));
		/**
		 * Add forensic data to patient
		 */
		
		patient = patientRepository.save(patient);
		
		/**
		 * Create registration patientBill and assign it to patient
		 */
		PatientBill regBill = new PatientBill();
		regBill.setAmount(regFee);
		regBill.setQty(1);
		regBill.setBalance(regFee);
		regBill.setBillItem("Registration");
		regBill.setDescription("Registration Fee"); 
		if(regFee > 0) {
			regBill.setStatus("UNPAID");
		}else {
			regBill.setStatus("VERIFIED");
		}
		regBill.setPatient(patient);
		/**
		 * Add forensic data to registration patientBill
		 */
		regBill.setCreatedby(userService.getUser(request).getId());
		regBill.setCreatedOn(dayService.getDay().getId());
		regBill.setCreatedAt(dayService.getTimeStamp());
		/**
		 * Save Registration patientBill
		 */
		regBill = patientBillRepository.save(regBill);
		/**
		 * Assign registration patientBill to patient
		 */
		
		Registration reg = new Registration();
		reg.setPatient(patient);
		
		reg.setCreatedby(userService.getUser(request).getId());
		reg.setCreatedOn(dayService.getDay().getId());
		reg.setCreatedAt(dayService.getTimeStamp());
		
		reg.setPatientBill(regBill);
		reg.setStatus("ACTIVE");
		registrationRepository.save(reg);
		
		/**
		 * Save patient
		 */
		patient = patientRepository.save(patient);

		/**
		 * For insurance covered patients, check 
		 */
		if(patient.getPaymentType().equalsIgnoreCase("INSURANCE") && regFee > 0) {
			/**
			 * Validate card, if card not valid, throw error, if valid, proceed		
			 */
			
			/**
			 * Load Registration plan
			 */
			Optional<RegistrationInsurancePlan> plan = registrationInsurancePlanRepository.findByInsurancePlanAndCovered(patient.getInsurancePlan(), true);
			if(plan.isPresent()) {
				
				/**
				 * If plan is present, edit registration patientBill to reflect plan price
				 */
				regBill.setAmount(plan.get().getRegistrationFee());
				regBill.setPaid(plan.get().getRegistrationFee());
				regBill.setBalance(0);
				regBill = patientBillRepository.save(regBill);
				/**
				 * Find a pending patientInvoice to register claims, if there is no pending patientInvoice, create one
				 */
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					
					
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add registration patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(regBill);
					patientInvoiceDetail.setAmount(regBill.getAmount());
					patientInvoiceDetail.setDescription("Registration Fee");
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.saveAndFlush(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(regBill);
					patientInvoiceDetail.setAmount(regBill.getAmount());
					patientInvoiceDetail.setDescription("Registration Fee");
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.saveAndFlush(patientInvoiceDetail);
				}
				
				/**
				 * Set registration patientBill to COVERED status, after assigning it to insurance cover
				 */
				regBill.setStatus("COVERED");
				regBill.setPaymentType("INSURANCE");
				regBill.setInsurancePlan(patient.getInsurancePlan());
				regBill.setMembershipNo(patient.getMembershipNo());
				
				/**
				 * Save registration patientBill
				 */
				regBill = patientBillRepository.save(regBill);
				/**
				 * Set patient Registration fee status to PAID
				 */
				patient = patientRepository.save(patient);
			}
					
		}	
		/**
		 * Create patient visit
		 */
		Visit visit = new Visit();
		visit.setPatient(patient);
		visit.setSequence("FIRST");
		visit.setStatus("PENDING");
		visit.setType(patient.getType());
		
		visit.setCreatedby(userService.getUser(request).getId());
		visit.setCreatedOn(dayService.getDay().getId());
		visit.setCreatedAt(dayService.getTimeStamp());
		
		visitRepository.save(visit);
		
		return patient;
	}
	
	@Override
	public Patient doConsultation(Patient p, Clinic c, Clinician cn, HttpServletRequest request) {
		
		if(cn.isActive() == false) {
			throw new InvalidOperationException("Process failed. Clinician/Doctor not active");
		}
		
		Optional<ConsultationTransfer> conTrans = consultationTransferRepository.findByPatientAndStatus(p, "PENDING");
		if(conTrans.isPresent()) {
			if(c.getId() != conTrans.get().getClinic().getId()) {
				throw new InvalidOperationException("Can not send to the specified clinic. Patient has been transfered to "+conTrans.get().getClinic().getName() +" clinic. Please send the patient to the specified clinic");
			}else {
				conTrans.get().setStatus("COMPLETED");
				consultationTransferRepository.save(conTrans.get());
			}
		}
		
		/**
		 * Check whether patient is assigned to a consultation, if yes, throws error
		 */
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("TRANSFERED");
		Optional<Consultation> pendingCon = consultationRepository.findByPatientAndStatusIn(p, statuses);
		if(pendingCon.isPresent()) {
			throw new InvalidOperationException("Patient has pending or held consultation, please consider freeing the patient");
		}
		statuses.add("IN-PROCESS");
		Optional<Consultation> activeCon = consultationRepository.findByPatientAndStatusIn(p, statuses);
		if(activeCon.isPresent()) {
			throw new InvalidOperationException("Patient has an active consultation, please wait for the patient to be released");
		}
		/**
		 * Create a consultation patientBill and assign it to patient and consultation
		 */
		PatientBill conBill = new PatientBill();
		conBill.setAmount(c.getConsultationFee());
		conBill.setPaid(0);
		conBill.setBalance(c.getConsultationFee());
		conBill.setQty(1);
		conBill.setBillItem("Consultation");
		conBill.setDescription("Consultation");
		conBill.setStatus("UNPAID");
		/**
		 * Add forensic data to registration patientBill
		 */
		conBill.setCreatedby(userService.getUser(request).getId());
		conBill.setCreatedOn(dayService.getDay().getId());
		conBill.setCreatedAt(dayService.getTimeStamp());
		/**
		 * Assign patient to consultation patientBill
		 */
		conBill.setPatient(p);
		/**
		 * Save Registration patientBill
		 */
		conBill = patientBillRepository.save(conBill);
		/**
		 * Create consultation
		 */
		Consultation consultation = new Consultation();
		consultation.setPatient(p);
		consultation.setClinic(c);
		consultation.setClinician(cn);
		consultation.setStatus("PENDING");
		consultation.setPatientBill(conBill);
		consultation.setPaymentType(p.getPaymentType());
		
		/**
		 * Set visit, create one if the last visit is not for today
		 */
		Visit visit = new Visit();
		visit.setPatient(p);
		visit.setSequence("SUBSEQUENT");
		visit.setType(p.getType());
		visit.setStatus("PENDING");
		
		visit.setCreatedby(userService.getUser(request).getId());
		visit.setCreatedOn(dayService.getDay().getId());
		visit.setCreatedAt(dayService.getTimeStamp());
		
		visit = visitRepository.save(visit);
		consultation.setVisit(visit);
		
		/**
		 * Add forensic data
		 */
		consultation.setCreatedby(userService.getUser(request).getId());
		consultation.setCreatedOn(dayService.getDay().getId());
		consultation.setCreatedAt(dayService.getTimeStamp());
		/**
		 * Save consultation
		 */
		consultation = consultationRepository.save(consultation);
		
		
		/**
		 * Check whether, if patient should pay by insurance, if the insurance cover is the same as the on registered for the patient
		 */
		if(p.getPaymentType().equals("INSURANCE")) {
			Optional<InsurancePlan> plan = insurancePlanRepository.findByName(p.getInsurancePlan().getName());
			if(!plan.isPresent()) {
				throw new NotFoundException("Insurance plan not found in database");
			}
			/**
			 * If plan has changed, check if previous transactions involving the plan have been signed
			 */
			Optional<PatientInvoice> pendingInv = patientInvoiceRepository.findByPatientAndStatus(p, "PENDING");
			if(p.getPaymentType().equals("INSURANCE")) {
				if(plan.get().getId() != p.getInsurancePlan().getId()) {
					if(pendingInv.isPresent()) {
						throw new InvalidOperationException("Use of two or more insurance plan. The patient should sign of the initial patientInvoice before proceeding with another plan");
					}
				}
				
			}else if(p.getPaymentType().equals("CASH")){
				if(pendingInv.isPresent()) {
					throw new InvalidOperationException("Use of two or more insurance plan. The patient should sign of the initial patientInvoice before proceeding with another plan");
				}
			}
			p.setPaymentType("INSURANCE");
			p.setInsurancePlan(plan.get());
			p.setMembershipNo(p.getMembershipNo());
			p = patientRepository.save(p);
			
			consultation.setPaymentType("INSURANCE");
			consultation.setMembershipNo(p.getMembershipNo());
			consultation.setInsurancePlan(plan.get());
			
			consultation.setCreatedby(userService.getUser(request).getId());
			consultation.setCreatedOn(dayService.getDay().getId());
			consultation.setCreatedAt(dayService.getTimeStamp());
			
			consultation = consultationRepository.save(consultation);
		}else if(p.getPaymentType().equals("CASH")){
			
			p.setPaymentType("CASH");
			p.setInsurancePlan(null);
			p.setMembershipNo("");
			p = patientRepository.save(p);
			
			consultation.setPaymentType("CASH");
			consultation.setMembershipNo("");
			consultation.setInsurancePlan(null);
			
			consultation.setCreatedby(userService.getUser(request).getId());
			consultation.setCreatedOn(dayService.getDay().getId());
			consultation.setCreatedAt(dayService.getTimeStamp());
			
			
			consultation = consultationRepository.save(consultation);
		}else {
			throw new InvalidOperationException("Invalid Payment type selected");
		}
		
		/**
		 * Now, if the patient is covered
		 */
		if(p.getPaymentType().equals("INSURANCE")) {
			
			
			Optional<ConsultationInsurancePlan> consultationPricePlan = consultationInsurancePlanRepository.findByClinicAndInsurancePlanAndCovered(c, p.getInsurancePlan(), true);
			
			if(!consultationPricePlan.isPresent()) {
				throw new InvalidOperationException("Plan not available for this clinic. Please change payment method");
			}
			conBill.setAmount(consultationPricePlan.get().getConsultationFee());
			conBill.setPaid(consultationPricePlan.get().getConsultationFee());
			conBill.setBalance(0);
			conBill.setStatus("COVERED");
			conBill.setPaymentType("INSURANCE");
			conBill.setInsurancePlan(p.getInsurancePlan());
			conBill.setMembershipNo(p.getMembershipNo());
			conBill.setCreatedby(userService.getUser(request).getId());
			conBill.setCreatedOn(dayService.getDay().getId());
			conBill.setCreatedAt(dayService.getTimeStamp());
			
			conBill = patientBillRepository.save(conBill);
			
			/**
			 * Find a pending patientInvoice to register claims, if there is no pending patientInvoice, create one
			 */
			Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(p, "PENDING");
			if(!inv.isPresent()) {
				/**
				 * If no pending patientInvoice
				 */
				PatientInvoice patientInvoice = new PatientInvoice();
				patientInvoice.setNo(String.valueOf(Math.random()));
				patientInvoice.setPatient(p);
				patientInvoice.setInsurancePlan(p.getInsurancePlan());
				patientInvoice.setStatus("PENDING");
				
				patientInvoice.setCreatedby(userService.getUser(request).getId());
				patientInvoice.setCreatedOn(dayService.getDay().getId());
				patientInvoice.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoice = patientInvoiceRepository.save(patientInvoice);
				patientInvoice.setNo(patientInvoice.getId().toString());

				
				
				patientInvoice = patientInvoiceRepository.save(patientInvoice);
				/**
				 * Add registration patientBill claim to patientInvoice
				 */
				PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
				patientInvoiceDetail.setPatientInvoice(patientInvoice);
				patientInvoiceDetail.setPatientBill(conBill);
				patientInvoiceDetail.setAmount(conBill.getAmount());
				patientInvoiceDetail.setDescription("Consultation");
				patientInvoiceDetail.setQty(1);
				
				patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
				patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
				patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoiceDetailRepository.saveAndFlush(patientInvoiceDetail);
			}else {
				/**
				 * If there is a .pending patientInvoice
				 */
				PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
				patientInvoiceDetail.setPatientInvoice(inv.get());
				patientInvoiceDetail.setPatientBill(conBill);
				patientInvoiceDetail.setAmount(conBill.getAmount());
				patientInvoiceDetail.setDescription("Consultation");
				patientInvoiceDetail.setQty(1);
				
				patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
				patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
				patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoiceDetailRepository.saveAndFlush(patientInvoiceDetail);
			}
		}
		
		consultation = consultationRepository.save(consultation);
		return null;
	}

	@Override
	public Patient update(Patient patient, HttpServletRequest request) {
		Optional<Patient> pt = patientRepository.findById(patient.getId());
		if(!pt.isPresent()) {
			throw new NotFoundException("Patient not found in database");
		}
		if(!pt.get().getNo().equals(patient.getNo())) {
			throw new InvalidOperationException("Editing patient file no is not allowed");
		}
		
		pt.get().setSearchKey(createSearchKey(patient.getNo(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getPhoneNo()));
		pt.get().setSearchKey(Sanitizer.sanitizeString(pt.get().getSearchKey()));
		//recreate search key
		pt.get().setFirstName(patient.getFirstName());
		pt.get().setMiddleName(patient.getMiddleName());
		pt.get().setLastName(patient.getLastName());
		pt.get().setDateOfBirth(patient.getDateOfBirth());
		pt.get().setGender(patient.getGender());
		pt.get().setNationality(patient.getNationality());
		pt.get().setNationalId(patient.getNationalId());
		pt.get().setPassportNo(patient.getPassportNo());
		pt.get().setPhoneNo(patient.getPhoneNo());
		pt.get().setEmail(patient.getEmail());
		pt.get().setAddress(patient.getAddress());
		pt.get().setKinFullName(patient.getKinFullName());
		pt.get().setKinRelationship(patient.getKinRelationship());
		pt.get().setKinPhoneNo(patient.getKinPhoneNo());
		
		
		return patientRepository.save(pt.get());
		
	}

	@Override
	public List<Patient> getBySearchKey(String searchKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getSearchKeys() {
		return patientRepository.getSearchKeys();	
	}
	
	private String createSearchKey(String no, String firstName, String middleName, String lastName, String phoneNo) {
		String key = no +" "+ firstName +" "+ middleName +" "+ lastName +" "+ phoneNo;
		key = key.trim().replaceAll("\\s+", " ");
		key = key.replaceAll("[+^]*#$%&", ""); 
		return  key;
	}

	@Override
	public LabTest saveLabTest(LabTest test, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request) {
		Patient patient = new Patient();
		Optional<LabTestType> ltt = labTestTypeRepository.findById(test.getLabTestType().getId());
		 
		if(!ltt.isPresent()) {
			throw new NotFoundException("Lab Test type not found");
		}
		Optional<DiagnosisType> dt;
		
		if(test.getDiagnosisType().getId() != null) {
			dt = diagnosisTypeRepository.findById(test.getDiagnosisType().getId());
			if(!dt.isPresent() && test.getDiagnosisType().getId() != null)	{
				throw new NotFoundException("Diagnosis type not found");
			}else {
				test.setDiagnosisType(dt.get());
			}
		}else {
			test.setDiagnosisType(null);
		}
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, labtest should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, labtest should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, labtest should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, labtest should not have more than two properties");
		}		
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, labtest must have one property");
		}
		
		if(c.isPresent()) {
			patient = c.get().getPatient();
			test.setConsultation(c.get());
			test.setClinician(c.get().getClinician());
		}
		if(nc.isPresent()) {
			NonConsultation non;// = new NonConsultation();
			if(nc.get().getStatus().equals("PENDING")) {
				nc.get().setStatus("IN-PROCESS");
				non =	nonConsultationRepository.save(nc.get());
			}else if(nc.get().getStatus().equals("IN-PROCESS")) {
				non = nc.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			patient = non.getPatient();
			test.setNonConsultation(non);
		}
		
		if(a.isPresent()) {
			Admission adm;// = new NonConsultation();
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off/ or discharged");
			}
			patient = adm.getPatient();
			test.setAdmission(adm);
			Optional<Clinician> clin = clinicianRepository.findByUser(userService.getUser(request));
			if(clin.isPresent()) {
				test.setClinician(clin.get());
			}
		}		
		test.setLabTestType(ltt.get());
		test.setStatus("PENDING");
				
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(test.getLabTestType().getPrice());
		patientBill.setPaid(0);
		patientBill.setBalance(test.getLabTestType().getPrice());
		patientBill.setQty(1);
		patientBill.setBillItem("Lab Test");
		patientBill.setDescription("Lab Test: "+test.getLabTestType().getName());
		patientBill.setStatus("UNPAID");
		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<LabTestTypeInsurancePlan> labTestTypePricePlan = labTestTypeInsurancePlanRepository.findByLabTestTypeAndInsurancePlanAndCovered(ltt.get(), patient.getInsurancePlan(), true);
			
			if(labTestTypePricePlan.isPresent()) {
				patientBill.setAmount(labTestTypePricePlan.get().getPrice());
				patientBill.setPaid(labTestTypePricePlan.get().getPrice());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(labTestTypePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
								
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, patient.getInsurancePlan(),"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Lab Test: "+test.getLabTestType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Lab Test: "+test.getLabTestType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(test.getLabTestType().getPrice());
				patientBill.setPaid(0);
				patientBill.setBalance(test.getLabTestType().getPrice());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Lab Test: "+test.getLabTestType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Lab Test: "+test.getLabTestType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
			
		}
		test.setPatient(patient);
		test.setPatientBill(patientBill);
		labTestRepository.save(test);	
		return null;
	}
	
	@Override
	public Radiology saveRadiology(Radiology radio, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request) {
		Patient patient = new Patient();
		Optional<RadiologyType> rt = radiologyTypeRepository.findById(radio.getRadiologyType().getId());
				 
		if(!rt.isPresent()) {
			throw new NotFoundException("Radiology type not found");
		}
		Optional<DiagnosisType> dt;		
		if(radio.getDiagnosisType().getId() != null) {
			dt = diagnosisTypeRepository.findById(radio.getDiagnosisType().getId());
			if(!dt.isPresent() && radio.getDiagnosisType().getId() != null)	{
				throw new NotFoundException("Diagnosis type not found");
			}else {
				radio.setDiagnosisType(dt.get());
			}
		}else {
			radio.setDiagnosisType(null);
		}
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, radiology should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, radiology should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, radiology should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, radiology should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, radiology should have one property");
		}
		if(c.isPresent()) {
			patient = c.get().getPatient();
			radio.setConsultation(c.get());
			radio.setClinician(c.get().getClinician());
		}
		
		if(nc.isPresent()) {
			NonConsultation non;// = new NonConsultation();
			if(nc.get().getStatus().equals("PENDING")) {
				nc.get().setStatus("IN-PROCESS");
				non =	nonConsultationRepository.save(nc.get());
			}else if(nc.get().getStatus().equals("IN-PROCESS")) {
				non = nc.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			patient = non.getPatient();
			radio.setNonConsultation(non);
		}
		if(a.isPresent()) {
			Admission adm;// = new NonConsultation();
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			Optional<Clinician> clin = clinicianRepository.findByUser(userService.getUser(request));
			if(clin.isPresent()) {
				radio.setClinician(clin.get());
			}
			patient = adm.getPatient();
			radio.setAdmission(adm);
		}
		
		radio.setRadiologyType(rt.get());
		radio.setStatus("PENDING");
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(radio.getRadiologyType().getPrice());
		patientBill.setPaid(0);
		patientBill.setBalance(radio.getRadiologyType().getPrice());
		patientBill.setQty(1);
		patientBill.setBillItem("Radiology");
		patientBill.setDescription("Radiology: "+radio.getRadiologyType().getName());
		patientBill.setStatus("UNPAID");		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<RadiologyTypeInsurancePlan> radiologyTypePricePlan = radiologyTypeInsurancePlanRepository.findByRadiologyTypeAndInsurancePlanAndCovered(rt.get(), patient.getInsurancePlan(), true);
			
			if(radiologyTypePricePlan.isPresent()) {
				patientBill.setAmount(radiologyTypePricePlan.get().getPrice());
				patientBill.setPaid(radiologyTypePricePlan.get().getPrice());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(radiologyTypePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Radiology: "+radio.getRadiologyType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Radiology: "+radio.getRadiologyType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(radio.getRadiologyType().getPrice());
				patientBill.setPaid(0);
				patientBill.setBalance(radio.getRadiologyType().getPrice());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Radiology: "+radio.getRadiologyType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Radiology: "+radio.getRadiologyType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
		}
		radio.setPatient(patient);
		radio.setPatientBill(patientBill);
		return radiologyRepository.save(radio);		
	}
	
	
	@Override
	public Procedure saveProcedure(Procedure procedure, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request) {
		Patient patient = new Patient();
		Optional<ProcedureType> pr = procedureTypeRepository.findById(procedure.getProcedureType().getId());
		if(procedure.getType().equals("THEATRE")) {
			Optional<Theatre> th = theatreRepository.findByName(procedure.getTheatre().getName());
			if(th.isEmpty()) {
				throw new InvalidOperationException("Theatre not found");
			}
			
			procedure.setTheatre(th.get());
		}else {
			procedure.setTheatre(null);
		}
		if(procedure.getDiagnosisType().getId() != null) {
			Optional<DiagnosisType> dt = diagnosisTypeRepository.findById(procedure.getDiagnosisType().getId());
			if(dt.isEmpty()) {
				throw new NotFoundException("Diagnosis Type not found");
			}else {
				procedure.setDiagnosisType(dt.get());
			}
		}else {
			procedure.setDiagnosisType(null);
		}
		
		if(!pr.isPresent()) {
			throw new NotFoundException("Procedure type not found");
		}
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should have one property");
		}
		if(c.isPresent()) {
			patient = c.get().getPatient();
			procedure.setConsultation(c.get());
			procedure.setClinician(c.get().getClinician());
		}
		
		if(nc.isPresent()) {
			NonConsultation non;// = new NonConsultation();
			if(nc.get().getStatus().equals("PENDING")) {
				nc.get().setStatus("IN-PROCESS");
				non =	nonConsultationRepository.save(nc.get());
			}else if(nc.get().getStatus().equals("IN-PROCESS")) {
				non = nc.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			patient = non.getPatient();
			procedure.setNonConsultation(non);
		}
		
		if(a.isPresent()) {
			Admission adm;// = new NonConsultation();
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			Optional<Clinician> clin = clinicianRepository.findByUser(userService.getUser(request));
			if(clin.isPresent()) {
				procedure.setClinician(clin.get());
			}
			patient = adm.getPatient();
			procedure.setAdmission(adm);
		}
		
		procedure.setProcedureType(pr.get());
		
		
		
		procedure.setStatus("PENDING");
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(procedure.getProcedureType().getPrice());
		patientBill.setPaid(0);
		patientBill.setBalance(procedure.getProcedureType().getPrice());
		patientBill.setQty(1);
		patientBill.setBillItem("Procedure");
		patientBill.setDescription("Procedure: "+procedure.getProcedureType().getName());
		patientBill.setStatus("UNPAID");		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<ProcedureTypeInsurancePlan> procedureTypePricePlan = procedureTypeInsurancePlanRepository.findByProcedureTypeAndInsurancePlanAndCovered(pr.get(), patient.getInsurancePlan(), true);
			
			if(procedureTypePricePlan.isPresent()) {
				patientBill.setAmount(procedureTypePricePlan.get().getPrice());
				patientBill.setPaid(procedureTypePricePlan.get().getPrice());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(procedureTypePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Procedure: "+procedure.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Procedure: "+procedure.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(procedure.getProcedureType().getPrice());
				patientBill.setPaid(0);
				patientBill.setBalance(procedure.getProcedureType().getPrice());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Procedure: "+procedure.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Procedure: "+procedure.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
		}
		procedure.setPatient(patient);
		procedure.setPatientBill(patientBill);
		return procedureRepository.save(procedure);		
	}
	
	
	
	@Override
	public Prescription savePrescription(Prescription prescription, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request) {
		Patient patient = new Patient();
		Optional<Medicine> md = medicineRepository.findByName(prescription.getMedicine().getName());
		 
		if(!md.isPresent()) {
			throw new NotFoundException("Medicine not found");
		}
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, prescription should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, prescription should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, prescription should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, prescription should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, prescription should have one property");
		}
		if(c.isPresent()) {
			patient = c.get().getPatient();
			prescription.setConsultation(c.get());
			prescription.setClinician(c.get().getClinician());
		}
		if(nc.isPresent()) {
			patient = nc.get().getPatient();
			prescription.setNonConsultation(nc.get());
		}
		
		if(a.isPresent()) {
			Admission adm;// = new NonConsultation();
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			Optional<Clinician> clin = clinicianRepository.findByUser(userService.getUser(request));
			if(clin.isPresent()) {
				prescription.setClinician(clin.get());
			}
			patient = adm.getPatient();
			prescription.setAdmission(adm);
		}
		
		prescription.setMedicine(md.get());
		prescription.setStatus("NOT-GIVEN");
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(prescription.getMedicine().getPrice() * prescription.getQty());
		patientBill.setPaid(0);
		patientBill.setBalance(prescription.getMedicine().getPrice() * prescription.getQty());
		patientBill.setQty(prescription.getQty());
		patientBill.setBillItem("Medication");
		patientBill.setDescription("Medicine: "+prescription.getMedicine().getName());
		patientBill.setStatus("UNPAID");		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<MedicineInsurancePlan> medicinePricePlan = medicineInsurancePlanRepository.findByMedicineAndInsurancePlanAndCovered(md.get(), patient.getInsurancePlan(), true);
			
			if(medicinePricePlan.isPresent()) {
				patientBill.setAmount(medicinePricePlan.get().getPrice() * prescription.getQty());
				patientBill.setPaid(medicinePricePlan.get().getPrice() * prescription.getQty());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(medicinePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Medicine: "+prescription.getMedicine().getName());
					patientInvoiceDetail.setQty(prescription.getQty());
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Medicine: "+prescription.getMedicine().getName());
					patientInvoiceDetail.setQty(prescription.getQty());
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(prescription.getMedicine().getPrice() * prescription.getQty());
				patientBill.setPaid(0);
				patientBill.setBalance(prescription.getMedicine().getPrice() * prescription.getQty());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Medicine: "+prescription.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Medicine: "+prescription.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
		}
		prescription.setIssued(0);
		prescription.setBalance(prescription.getQty());
		prescription.setPatient(patient);
		prescription.setPatientBill(patientBill);
		return prescriptionRepository.save(prescription);		
	}

	@Override
	public Admission doAdmission(Patient p, WardBed wb, HttpServletRequest request) {
		
		if(!wb.isActive()) {
			throw new InvalidOperationException("Could not process admission, bed not active");
		}
		
		if(!wb.getStatus().equals("EMPTY")){
			throw new InvalidOperationException("Could not process admission, bed already occupied. Please select a different bed.");
		}
		wb.setStatus("WAITING");
		wb = wardBedRepository.save(wb);
		
		Admission admission = new Admission();
		admission.setPatient(p);
		admission.setPaymentType(p.getPaymentType());
		admission.setInsurancePlan(p.getInsurancePlan());
		admission.setMembershipNo(p.getMembershipNo());
		admission.setWardBed(wb);
		admission.setStatus("PENDING");
				
		/**
		 * Set visit, create one if the last visit is not for today
		 */
		Visit visit = new Visit();
		visit.setPatient(p);
		
		visit.setStatus("PENDING");
		visit.setType(p.getType());
		
		
		visit.setSequence("SUBSEQUENT");
		
		visit.setCreatedby(userService.getUser(request).getId());
		visit.setCreatedOn(dayService.getDay().getId());
		visit.setCreatedAt(dayService.getTimeStamp());
		
		visit = visitRepository.save(visit);
		admission.setVisit(visit);
		
		admission.setCreatedBy(userService.getUser(request).getId());
		admission.setCreatedOn(dayService.getDay().getId());
		admission.setCreatedAt(dayService.getTimeStamp());
		
		admission.setAdmittedBy(userService.getUser(request).getId());
		admission.setAdmittedOn(dayService.getDay().getId());
		admission.setAdmittedAt(dayService.getTimeStamp());
		
		admission = admissionRepository.save(admission);
		
		/**
		 * Create ward bed bill
		 */
		PatientBill wardBedBill = new PatientBill();
		wardBedBill.setAmount(wb.getWard().getWardType().getPrice());
		wardBedBill.setPaid(0);
		wardBedBill.setBalance(wb.getWard().getWardType().getPrice());
		wardBedBill.setQty(1);
		wardBedBill.setBillItem("Bed");
		wardBedBill.setDescription("Ward Bed / Room");
		wardBedBill.setStatus("UNPAID");
		/**
		 * Add forensic data to registration patientBill
		 */
		wardBedBill.setCreatedby(userService.getUser(request).getId());
		wardBedBill.setCreatedOn(dayService.getDay().getId());
		wardBedBill.setCreatedAt(dayService.getTimeStamp());
		/**
		 * Assign patient to consultation patientBill
		 */
		wardBedBill.setPatient(p);
		/**
		 * Save Registration patientBill
		 */
		wardBedBill = patientBillRepository.save(wardBedBill);
		
		AdmissionBed admissionBed = new AdmissionBed();
		admissionBed.setAdmission(admission);
		admissionBed.setPatient(p);
		admissionBed.setWardBed(wb);
		admissionBed.setPatientBill(wardBedBill);
		admissionBed.setStatus("OPENED");
		admissionBed.setOpenedAt(dayService.getTimeStamp());
		admissionBed = admissionBedRepository.save(admissionBed);
		
		p.setType("INPATIENT");
		p = patientRepository.save(p);
		
		if(p.getPaymentType().equals("INSURANCE")) {
						
			WardTypeInsurancePlan eligiblePlan = null;
			
			List<WardTypeInsurancePlan> wardTypePricePlans = wardTypeInsurancePlanRepository.findByInsurancePlanAndCovered(p.getInsurancePlan(), true);
			double eligiblePrice = 0;
			for(WardTypeInsurancePlan plan : wardTypePricePlans) {
				if(plan.getPrice() > eligiblePrice || plan.getInsurancePlan().getId() == p.getInsurancePlan().getId()) {
					eligiblePrice = plan.getPrice();
					eligiblePlan = plan;
					if(plan.getInsurancePlan().getId() == p.getInsurancePlan().getId()) {
						break;
					}
				}
			}	
			
			if(eligiblePlan != null) {
				wardBedBill.setAmount(eligiblePlan.getPrice());
				wardBedBill.setPaid(eligiblePlan.getPrice());
				wardBedBill.setBalance(0);
				wardBedBill.setPaymentType("INSURANCE");
				wardBedBill.setInsurancePlan(eligiblePlan.getInsurancePlan());
				wardBedBill.setMembershipNo(p.getMembershipNo());
				wardBedBill.setStatus("COVERED");				
				wardBedBill = patientBillRepository.save(wardBedBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(p, p.getInsurancePlan(),"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(p);
					patientInvoice.setAdmission(admission);
					patientInvoice.setInsurancePlan(p.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(wardBedBill);
					patientInvoiceDetail.setAmount(wardBedBill.getAmount());
					patientInvoiceDetail.setDescription("Ward Bed / Room");
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(wardBedBill);
					patientInvoiceDetail.setAmount(wardBedBill.getAmount());
					patientInvoiceDetail.setDescription("Ward Bed / Room");
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
				
				
				
				
				if(eligiblePlan.getInsurancePlan().getId() != p.getInsurancePlan().getId() && (wb.getWard().getWardType().getPrice() - eligiblePlan.getPrice() > 0)) {
					PatientBill supplementaryWardBedBill = new PatientBill();
										
					supplementaryWardBedBill.setAmount(wb.getWard().getWardType().getPrice() - eligiblePlan.getPrice());
					supplementaryWardBedBill.setPaid(0);
					supplementaryWardBedBill.setBalance(wb.getWard().getWardType().getPrice() - eligiblePlan.getPrice());
					supplementaryWardBedBill.setStatus("UNPAID");
					supplementaryWardBedBill.setBillItem("Bed");
					supplementaryWardBedBill.setDescription("Ward Bed / Room (Top up)");
					supplementaryWardBedBill.setPrincipalPatientBill(wardBedBill);
					
					supplementaryWardBedBill.setCreatedby(userService.getUser(request).getId());
					supplementaryWardBedBill.setCreatedOn(dayService.getDay().getId());
					supplementaryWardBedBill.setCreatedAt(dayService.getTimeStamp());
					
					supplementaryWardBedBill = patientBillRepository.save(supplementaryWardBedBill);
					wardBedBill.setSupplementaryPatientBill(supplementaryWardBedBill);
					wardBedBill = patientBillRepository.save(wardBedBill);
					
					Optional<PatientInvoice> supInv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(p, null,"PENDING");
					if(!supInv.isPresent()) {
						/**
						 * If no pending patientInvoice
						 */
						PatientInvoice patientInvoice = new PatientInvoice();
						patientInvoice.setNo(String.valueOf(Math.random()));
						patientInvoice.setPatient(p);
						patientInvoice.setAdmission(admission);
						patientInvoice.setInsurancePlan(null);
						patientInvoice.setStatus("PENDING");
						
						patientInvoice.setCreatedby(userService.getUser(request).getId());
						patientInvoice.setCreatedOn(dayService.getDay().getId());
						patientInvoice.setCreatedAt(dayService.getTimeStamp());
						
						patientInvoice = patientInvoiceRepository.save(patientInvoice);
						patientInvoice.setNo(patientInvoice.getId().toString());
						patientInvoice = patientInvoiceRepository.save(patientInvoice);
						/**
						 * Add lab test patientBill claim to patientInvoice
						 */
						PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
						patientInvoiceDetail.setPatientInvoice(patientInvoice);
						patientInvoiceDetail.setPatientBill(supplementaryWardBedBill);
						patientInvoiceDetail.setAmount(supplementaryWardBedBill.getAmount());
						patientInvoiceDetail.setDescription("Ward Bed / Room (Top up)");
						patientInvoiceDetail.setQty(1);
						
						patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
						patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
						patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
						
						patientInvoiceDetailRepository.save(patientInvoiceDetail);
					}else {
						/**
						 * If there is a .pending patientInvoice
						 */
						PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
						patientInvoiceDetail.setPatientInvoice(supInv.get());
						patientInvoiceDetail.setPatientBill(supplementaryWardBedBill);
						patientInvoiceDetail.setAmount(supplementaryWardBedBill.getAmount());
						patientInvoiceDetail.setDescription("Ward Bed / Room (Top up)");
						patientInvoiceDetail.setQty(1);
						
						patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
						patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
						patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
						
						patientInvoiceDetailRepository.save(patientInvoiceDetail);
					}					
				}else {
					List<String> sts = new ArrayList<>();
					sts.add("PENDING");
					sts.add("IN-PROCESS");
					List<Consultation> cons = consultationRepository.findAllByPatientAndStatusIn(p, sts);
					for(Consultation con : cons) {
						con.setStatus("SIGNED-OUT");
						consultationRepository.save(con);
					}
					admission.setStatus("IN-PROCESS");
					admission = admissionRepository.save(admission);
					wb.setStatus("OCCUPIED");
					wardBedRepository.save(wb);
				}
			}else {
				//throw new InvalidOperationException("")
			}
		}else {
			Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(p, null,"PENDING");
			if(!inv.isPresent()) {
				/**
				 * If no pending patientInvoice
				 */
				PatientInvoice patientInvoice = new PatientInvoice();
				patientInvoice.setNo(String.valueOf(Math.random()));
				patientInvoice.setPatient(p);
				patientInvoice.setAdmission(admission);
				patientInvoice.setInsurancePlan(null);
				patientInvoice.setStatus("PENDING");
				
				patientInvoice.setCreatedby(userService.getUser(request).getId());
				patientInvoice.setCreatedOn(dayService.getDay().getId());
				patientInvoice.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoice = patientInvoiceRepository.save(patientInvoice);
				patientInvoice.setNo(patientInvoice.getId().toString());
				patientInvoice = patientInvoiceRepository.save(patientInvoice);
				/**
				 * Add lab test patientBill claim to patientInvoice
				 */
				PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
				patientInvoiceDetail.setPatientInvoice(patientInvoice);
				patientInvoiceDetail.setPatientBill(wardBedBill);
				patientInvoiceDetail.setAmount(wardBedBill.getAmount());
				patientInvoiceDetail.setDescription("Ward Bed / Room");
				patientInvoiceDetail.setQty(1);
				
				patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
				patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
				patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoiceDetailRepository.save(patientInvoiceDetail);
			}else {
				/**
				 * If there is a .pending patientInvoice
				 */
				PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
				patientInvoiceDetail.setPatientInvoice(inv.get());
				patientInvoiceDetail.setPatientBill(wardBedBill);
				patientInvoiceDetail.setAmount(wardBedBill.getAmount());
				patientInvoiceDetail.setDescription("Ward Bed / Room");
				patientInvoiceDetail.setQty(1);
				
				patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
				patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
				patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
				
				patientInvoiceDetailRepository.save(patientInvoiceDetail);
			}
		}
		return admission;
	}

	@Override
	public PatientDressingChart savePatientDressingChart(PatientDressingChart chart, Optional<Consultation> c,
			Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request) {
		Optional<ProcedureType> pt = procedureTypeRepository.findById(chart.getProcedureType().getId());
		if(pt.isEmpty()) {
			throw new NotFoundException("Procedure type not found");
		}
		List<Dressing> dress = dressingRepository.findAllByProcedureType(pt.get());
		if(dress.isEmpty()) {
			throw new NotFoundException("Procedure type is not listed as dressing");
		}		
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, procedure should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}
		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			chart.setAdmission(a.get());
			chart.setNurse(n.get());
		}	
		chart.setProcedureType(pt.get());
		
		//dressingChart.setStatus("PENDING");
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(chart.getProcedureType().getPrice());
		patientBill.setPaid(0);
		patientBill.setBalance(chart.getProcedureType().getPrice());
		patientBill.setQty(1);
		patientBill.setBillItem("Procedure");
		patientBill.setDescription("Dressing: "+chart.getProcedureType().getName());
		patientBill.setStatus("UNPAID");		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<ProcedureTypeInsurancePlan> procedureTypePricePlan = procedureTypeInsurancePlanRepository.findByProcedureTypeAndInsurancePlanAndCovered(pt.get(), patient.getInsurancePlan(), true);
			
			if(procedureTypePricePlan.isPresent()) {
				patientBill.setAmount(procedureTypePricePlan.get().getPrice());
				patientBill.setPaid(procedureTypePricePlan.get().getPrice());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(procedureTypePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Dressing: "+chart.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Dressing: "+chart.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(chart.getProcedureType().getPrice());
				patientBill.setPaid(0);
				patientBill.setBalance(chart.getProcedureType().getPrice());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Dressing: "+chart.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Dressing: "+chart.getProcedureType().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
		}
		chart.setPatient(patient);
		chart.setPatientBill(patientBill);
		
		chart.setCreatedby(userService.getUser(request).getId());
		chart.setCreatedOn(dayService.getDay().getId());
		chart.setCreatedAt(dayService.getTimeStamp());
		
		return patientDressingChartRepository.save(chart);	
		
	}

	@Override
	public PatientConsumableChart savePatientConsumableChart(PatientConsumableChart chart, Optional<Consultation> c,
			Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request) {
		Optional<Medicine> med = medicineRepository.findById(chart.getMedicine().getId());
		if(med.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		if(chart.getQty() <= 0) {
			throw new InvalidEntryException("Qty can not be zero");
		}
		List<Consumable> consum = consumableRepository.findAllByMedicine(med.get());
		if(consum.isEmpty()) {
			throw new NotFoundException("Medicine is not listed as consumable");
		}		
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}
		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			chart.setAdmission(a.get());
			chart.setNurse(n.get());
		}		
		chart.setMedicine(med.get());
		chart.setStatus("NOT-GIVEN");
		
		PatientBill patientBill = new PatientBill();
		patientBill.setAmount(chart.getMedicine().getPrice());
		patientBill.setPaid(0);
		patientBill.setBalance(chart.getMedicine().getPrice() * chart.getQty());
		patientBill.setQty(chart.getQty());
		patientBill.setBillItem("Medication");
		patientBill.setDescription("Consumable: "+chart.getMedicine().getName());
		patientBill.setStatus("UNPAID");		
		patientBill.setCreatedby(userService.getUser(request).getId());
		patientBill.setCreatedOn(dayService.getDay().getId());
		patientBill.setCreatedAt(dayService.getTimeStamp());
		patientBill.setPatient(patient);
		patientBill = patientBillRepository.save(patientBill);
		
		if(patient.getPaymentType().equals("INSURANCE") || a.isPresent() == true) {
			
			Optional<MedicineInsurancePlan> medicinePricePlan = medicineInsurancePlanRepository.findByMedicineAndInsurancePlanAndCovered(med.get(), patient.getInsurancePlan(), true);
			
			if(medicinePricePlan.isPresent()) {
				patientBill.setAmount(medicinePricePlan.get().getPrice() * chart.getQty());
				patientBill.setPaid(medicinePricePlan.get().getPrice() * chart.getQty());
				patientBill.setBalance(0);
				patientBill.setStatus("COVERED");
				patientBill.setPaymentType("INSURANCE");
				patientBill.setMembershipNo(patient.getMembershipNo());
				patientBill.setInsurancePlan(medicinePricePlan.get().getInsurancePlan());
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndStatus(patient, "PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(patient.getInsurancePlan());
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Consumable: "+chart.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Consumable: "+chart.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}else if(a.isPresent() == true) {
				
				patientBill.setAmount(chart.getMedicine().getPrice() * chart.getQty());
				patientBill.setPaid(0);
				patientBill.setBalance(chart.getMedicine().getPrice() * chart.getQty());
				patientBill.setStatus("VERIFIED");
				patientBill = patientBillRepository.save(patientBill);
				
				Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(patient, null,"PENDING");
				if(!inv.isPresent()) {
					/**
					 * If no pending patientInvoice
					 */
					PatientInvoice patientInvoice = new PatientInvoice();
					patientInvoice.setNo(String.valueOf(Math.random()));
					patientInvoice.setPatient(patient);
					patientInvoice.setInsurancePlan(null);
					patientInvoice.setStatus("PENDING");
					
					patientInvoice.setCreatedby(userService.getUser(request).getId());
					patientInvoice.setCreatedOn(dayService.getDay().getId());
					patientInvoice.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					patientInvoice.setNo(patientInvoice.getId().toString());
					patientInvoice = patientInvoiceRepository.save(patientInvoice);
					/**
					 * Add lab test patientBill claim to patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(patientInvoice);
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Consumable: "+chart.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}else {
					/**
					 * If there is a .pending patientInvoice
					 */
					PatientInvoiceDetail patientInvoiceDetail = new PatientInvoiceDetail();
					patientInvoiceDetail.setPatientInvoice(inv.get());
					patientInvoiceDetail.setPatientBill(patientBill);
					patientInvoiceDetail.setAmount(patientBill.getAmount());
					patientInvoiceDetail.setDescription("Consumable: "+chart.getMedicine().getName());
					patientInvoiceDetail.setQty(1);
					
					patientInvoiceDetail.setCreatedby(userService.getUser(request).getId());
					patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
					patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
					
					patientInvoiceDetailRepository.save(patientInvoiceDetail);
				}
			}
		}
		chart.setPatient(patient);
		chart.setPatientBill(patientBill);
		
		chart.setCreatedby(userService.getUser(request).getId());
		chart.setCreatedOn(dayService.getDay().getId());
		chart.setCreatedAt(dayService.getTimeStamp());
		
		return patientConsumableChartRepository.save(chart);			
	}

	@Override
	public PatientObservationChart savePatientObservationChart(PatientObservationChart chart, Optional<Consultation> c,
			Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request) {
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}
		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			chart.setAdmission(a.get());
			chart.setNurse(n.get());
		}	
		
		
		chart.setPatient(patient);
		
		chart.setCreatedby(userService.getUser(request).getId());
		chart.setCreatedOn(dayService.getDay().getId());
		chart.setCreatedAt(dayService.getTimeStamp());
		
		return patientObservationChartRepository.save(chart);
	}

	@Override
	public PatientPrescriptionChart savePatientPrescriptionChart(PatientPrescriptionChart chart,
			Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n,
			HttpServletRequest request) {
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}
		
		Optional<Prescription> p = prescriptionRepository.findById(chart.getPrescription().getId());
		if(p.isEmpty()) {
			throw new NotFoundException("Medical prescription detail not found");
		}	
		if(!p.get().getStatus().equals("GIVEN")) {
			throw new InvalidOperationException("Prescription not picked from pharmacy");
		}
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				//continue
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			chart.setAdmission(a.get());
			chart.setNurse(n.get());
		}		
		chart.setPatient(patient);
		
		chart.setCreatedby(userService.getUser(request).getId());
		chart.setCreatedOn(dayService.getDay().getId());
		chart.setCreatedAt(dayService.getTimeStamp());
		
		return patientPrescriptionChartRepository.save(chart);
	}

	@Override
	public PatientNursingChart savePatientNursingChart(PatientNursingChart chart, Optional<Consultation> c,
			Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request) {
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}
		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			chart.setAdmission(a.get());
			chart.setNurse(n.get());
		}		
		
		chart.setPatient(patient);
		
		chart.setCreatedby(userService.getUser(request).getId());
		chart.setCreatedOn(dayService.getDay().getId());
		chart.setCreatedAt(dayService.getTimeStamp());
		
		return patientNursingChartRepository.save(chart);
	}

	@Override
	public PatientNursingProgressNote savePatientNursingProgressNote(PatientNursingProgressNote note,
			Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n,
			HttpServletRequest request) {
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			note.setAdmission(a.get());
			note.setNurse(n.get());
		}		
		
		note.setPatient(patient);
		
		note.setCreatedby(userService.getUser(request).getId());
		note.setCreatedOn(dayService.getDay().getId());
		note.setCreatedAt(dayService.getTimeStamp());
		
		return patientNursingProgressNoteRepository.save(note);
	}
	
	@Override
	public PatientNursingCarePlan savePatientNursingCarePlan(PatientNursingCarePlan plan,
			Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n,
			HttpServletRequest request) {
		if(n.isEmpty()) {
			throw new NotFoundException("Nurse information not found");
		}		
		Patient patient = new Patient();
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}		
		if(c.isPresent() && nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(c.isPresent() && !nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should not have more than two properties");
		}
		if(!c.isPresent() && !nc.isPresent() && !a.isPresent()) {
			throw new InvalidOperationException("Could not save, chart should have one property");
		}
		if(c.isPresent()) {
			throw new InvalidOperationException("Operation not available for outpatients");
		}		
		if(nc.isPresent()) {
			throw new InvalidOperationException("Operation not available for outsiders");
		}		
		if(a.isPresent()) {
			Admission adm;
			if(a.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Could not be done. Admission not verified");
			}else if(a.get().getStatus().equals("IN-PROCESS")) {
				adm = a.get();
			}else {
				throw new InvalidOperationException("Could not be done. Patient already signed off");
			}
			
			patient =a.get().getPatient();
			plan.setAdmission(a.get());
			plan.setNurse(n.get());
		}		
		
		plan.setPatient(patient);
		
		plan.setCreatedby(userService.getUser(request).getId());
		plan.setCreatedOn(dayService.getDay().getId());
		plan.setCreatedAt(dayService.getTimeStamp());
		
		return patientNursingCarePlanRepository.save(plan);
	}

	@Override
	public ConsultationTransfer createConsultationTransfer(ConsultationTransfer transfer, HttpServletRequest request) {		
		if(!transfer.getConsultation().getStatus().equals("IN-PROCESS")) {
			throw new InvalidOperationException("Can not transfer. Not an active consultation");
		}
		List<LabTest> labTests = labTestRepository.findByConsultation(transfer.getConsultation());
		List<Radiology> radiologies = radiologyRepository.findByConsultation(transfer.getConsultation());
		List<Procedure> procedures = procedureRepository.findByConsultation(transfer.getConsultation());
		List<Prescription> prescriptions = prescriptionRepository.findByConsultation(transfer.getConsultation());
		
		List<ConsultationTransfer> contras = consultationTransferRepository.findAllByPatientAndStatus(transfer.getConsultation().getPatient(), "PENDING");
		if(!contras.isEmpty()) {
			throw new InvalidOperationException("Can not transfer, the patient already have a pending transfer");
		}
		
		for(LabTest test : labTests) {
			if(test.getStatus() != null) {
				if(test.getStatus().equals("PENDING")) {
					throw new InvalidOperationException("Can not transfer. The patient has a pending lab test. Please consider canceling the test");
				}
			}
			
		}
		for(Radiology test : radiologies) {
			if(test.getStatus() != null) {
				if(test.getStatus().equals("PENDING")) {
					throw new InvalidOperationException("Can not transfer. The patient has a pending radiology test. Please consider canceling the test");
				}
			}
			
		}
		for(Procedure test : procedures) {
			if(test.getStatus() != null) {
				if(test.getStatus().equals("PENDING")) {
					throw new InvalidOperationException("Can not transfer. The patient has a pending procedure. Please consider canceling the procedure");
				}
			}
			
		}
		for(Prescription test : prescriptions) {
			if(test.getStatus() != null) {
				if(test.getStatus().equals("PENDING")) {
					throw new InvalidOperationException("Can not transfer. The patient has a pending prescription. Please consider canceling the prescription");
				}
			}
			
		}
		
		Consultation con = transfer.getConsultation();
		
		if(transfer.getClinic().getId() == con.getClinic().getId()) {
			throw new InvalidOperationException("Can not transfer to the same clinic");
		}
		
		con.setStatus("TRANSFERED");
		con = consultationRepository.save(con);
	
		transfer.setStatus("PENDING");
		transfer.setConsultation(con);///??
		transfer.setPatient(con.getPatient());
		
		transfer.setCreatedby(userService.getUser(request).getId());
		transfer.setCreatedOn(dayService.getDay().getId());
		transfer.setCreatedAt(dayService.getTimeStamp());
		
		return consultationTransferRepository.save(transfer);
	}

	@Override
	public ResponseEntity<Map<String, String>> saveLabTestAttachment(LabTest labTest, MultipartFile file, String name, HttpServletRequest request) {
		
		log.info("handling request parts: {}", file);

	    try {
	      
	      //File f = new ClassPathResource("").getFile();
	      
	      List<CompanyProfile> comps = companyProfileRepository.findAll();
	      CompanyProfile companyProfile = null;
	      for(CompanyProfile comp : comps) {
	    	  companyProfile = comp;
	      }
	      
	      if(companyProfile == null) {
	    	  throw new NotFoundException("Company Profile not found");
	      }
	      if(companyProfile.getPublicPath() == null) {
	    	  throw new NotFoundException("Driver not found. Contact Administrator");
	      }
	      if(companyProfile.getPublicPath().equals("")) {
	    	  throw new NotFoundException("Driver not found. Contact System Administrator");
	      }
	      
	      //final Path path = Paths.get(f.getAbsolutePath() + File.separator + "static" + File.separator + "image");
	      final Path path = Paths.get(companyProfile.getPublicPath());

	      if (!Files.exists(path)) {
	        Files.createDirectories(path);
	      }
	      
	      //Path filePath = path.resolve(file.getOriginalFilename());
	      
	      
	      
	      String fileRawName = ("LT" + labTest.getId().toString() + labTest.getPatient().getNo() + String.valueOf(Math.random()) + LocalDateTime.now().toString())
	    		  .trim().replace("/", "").replace(".", "").replace(":", "").replace("-", "");
	      String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
	      
	      String fileName = fileRawName + "." + fileExtension; 
	    		  	      
	      Path filePath = path.resolve(fileName);
	      
	      
	      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	      
	      String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	          .path("/image/")
	          .path(file.getOriginalFilename())
	          .toUriString();

	      var result = Map.of(
	          "filename", file.getOriginalFilename(),
	          "fileUri", fileUri
	      );
	      
	      
	      
	      //now put here lab attachments logic
	      
	      LabTestAttachment labTestAttachment = new LabTestAttachment();
	      labTestAttachment.setName(name);
	      labTestAttachment.setFileName(fileName);
	      labTestAttachment.setLabTest(labTest);
	      
	      
	      //labTestAttachment.setCreatedBy(userService.getUser(request).getId());
	      labTestAttachment.setCreatedBy(labTest.getCollectedBy());
	      labTestAttachment.setCreatedOn(dayService.getDay().getId());
	      labTestAttachment.setCreatedAt(dayService.getTimeStamp());
	      
	      labTestAttachmentRepository.save(labTestAttachment);
	      
	      //return ok().body(result);
	      return null;

	    } catch (IOException e) {
	      log.error(e.getMessage());
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
		
}
