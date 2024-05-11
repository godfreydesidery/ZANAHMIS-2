/**
 * 
 */
package com.orbix.api.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientConsumableChart;
import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.ClinicalNote;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.ClinicianPerformance;
import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ConsultationTransfer;
import com.orbix.api.domain.DeceasedNote;
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.DischargePlan;
import com.orbix.api.domain.ExternalMedicalProvider;
import com.orbix.api.domain.FinalDiagnosis;
import com.orbix.api.domain.GeneralExamination;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.PatientInvoice;
import com.orbix.api.domain.PatientInvoiceDetail;
import com.orbix.api.domain.PatientNursingCarePlan;
import com.orbix.api.domain.PatientNursingChart;
import com.orbix.api.domain.PatientNursingProgressNote;
import com.orbix.api.domain.PatientObservationChart;
import com.orbix.api.domain.PatientPaymentDetail;
import com.orbix.api.domain.PatientPrescriptionChart;
import com.orbix.api.domain.PatientVital;
import com.orbix.api.domain.Pharmacy;
import com.orbix.api.domain.PharmacyMedicine;
import com.orbix.api.domain.PharmacyMedicineBatch;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.LabTestAttachment;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientCreditNote;
import com.orbix.api.domain.PatientDressingChart;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.PrescriptionBatch;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.ProcedureType;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.RadiologyAttachment;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.ReferralPlan;
import com.orbix.api.domain.StoreItemBatch;
import com.orbix.api.domain.StoreToPharmacyBatch;
import com.orbix.api.domain.User;
import com.orbix.api.domain.Visit;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.WorkingDiagnosis;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.MissingInformationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.exceptions.ResourceNotFoundException;
import com.orbix.api.models.ClinicalNoteModel;
import com.orbix.api.models.ConsultationModel;
import com.orbix.api.models.FinalDiagnosisModel;
import com.orbix.api.models.GeneralExaminationModel;
import com.orbix.api.models.LabTestAttachmentModel;
import com.orbix.api.models.LabTestModel;
import com.orbix.api.models.PatientConsumableChartModel;
import com.orbix.api.models.PatientDressingChartModel;
import com.orbix.api.models.PatientModel;
import com.orbix.api.models.PatientNursingCarePlanModel;
import com.orbix.api.models.PatientNursingChartModel;
import com.orbix.api.models.PatientNursingProgressNoteModel;
import com.orbix.api.models.PatientObservationChartModel;
import com.orbix.api.models.PatientPrescriptionChartModel;
import com.orbix.api.models.PrescriptionModel;
import com.orbix.api.models.ProcedureModel;
import com.orbix.api.models.RadiologyAttachmentModel;
import com.orbix.api.models.RadiologyModel;
import com.orbix.api.models.WorkingDiagnosisModel;
import com.orbix.api.repositories.AdmissionRepository;
import com.orbix.api.repositories.ClinicRepository;
import com.orbix.api.repositories.ClinicalNoteRepository;
import com.orbix.api.repositories.ClinicianPerformanceRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.CompanyProfileRepository;
import com.orbix.api.repositories.ConsultationRepository;
import com.orbix.api.repositories.ConsultationTransferRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DeceasedNoteRepository;
import com.orbix.api.repositories.DiagnosisTypeRepository;
import com.orbix.api.repositories.DischargePlanRepository;
import com.orbix.api.repositories.ExternalMedicalProviderRepository;
import com.orbix.api.repositories.FinalDiagnosisRepository;
import com.orbix.api.repositories.GeneralExaminationRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
import com.orbix.api.repositories.LabTestAttachmentRepository;
import com.orbix.api.repositories.LabTestRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.NonConsultationRepository;
import com.orbix.api.repositories.NurseRepository;
import com.orbix.api.repositories.PatientBillRepository;
import com.orbix.api.repositories.PatientConsumableChartRepository;
import com.orbix.api.repositories.PatientCreditNoteRepository;
import com.orbix.api.repositories.PatientDressingChartRepository;
import com.orbix.api.repositories.PatientInvoiceDetailRepository;
import com.orbix.api.repositories.PatientInvoiceRepository;
import com.orbix.api.repositories.PatientNursingCarePlanRepository;
import com.orbix.api.repositories.PatientNursingChartRepository;
import com.orbix.api.repositories.PatientNursingProgressNoteRepository;
import com.orbix.api.repositories.PatientObservationChartRepository;
import com.orbix.api.repositories.PatientPaymentDetailRepository;
import com.orbix.api.repositories.PatientPaymentRepository;
import com.orbix.api.repositories.PatientPrescriptionChartRepository;
import com.orbix.api.repositories.PatientRepository;
import com.orbix.api.repositories.PatientVitalRepository;
import com.orbix.api.repositories.PharmacyMedicineBatchRepository;
import com.orbix.api.repositories.PharmacyMedicineRepository;
import com.orbix.api.repositories.PharmacyRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.PrescriptionBatchRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.repositories.ProcedureRepository;
import com.orbix.api.repositories.ProcedureTypeRepository;
import com.orbix.api.repositories.RadiologyAttachmentRepository;
import com.orbix.api.repositories.RadiologyRepository;
import com.orbix.api.repositories.RadiologyTypeRepository;
import com.orbix.api.repositories.ReferralPlanRepository;
import com.orbix.api.repositories.VisitRepository;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.repositories.WorkingDiagnosisRepository;
import com.orbix.api.service.ClinicianPerformanceService;
import com.orbix.api.service.CompanyProfileService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.PatientCreditNoteService;
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
@MultipartConfig
@Transactional
public class PatientResource {
	private final PatientService patientService;
	private final PatientRepository patientRepository;
	private final ClinicRepository clinicRepository;
	private final ClinicianRepository clinicianRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final ConsultationRepository consultationRepository;
	private final NonConsultationRepository nonConsultationRepository;
	private final PatientBillRepository patientBillRepository;
	private final PatientPaymentRepository patientPaymentRepository;
	private final PatientPaymentDetailRepository patientPaymentDetailRepository;
	private final PatientCreditNoteRepository patientCreditNoteRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	private final PatientInvoiceRepository patientInvoiceRepository;
	private final VisitRepository visitRepository;
	private final DayRepository dayRepository;
	private final ClinicalNoteRepository clinicalNoteRepository;
	private final GeneralExaminationRepository generalExaminationRepository;
	private final DiagnosisTypeRepository diagnosisTypeRepository;
	private final WorkingDiagnosisRepository workingDiagnosisRepository;
	private final FinalDiagnosisRepository finalDiagnosisRepository;
	private final LabTestRepository labTestRepository;
	private final RadiologyRepository radiologyRepository;
	private final ProcedureRepository procedureRepository;
	private final PrescriptionRepository prescriptionRepository;
	private final UserService userService;
	private final DayService dayService;
	private final LabTestTypeRepository labTestTypeRepository;
	private final RadiologyTypeRepository radiologyTypeRepository;
	private final ProcedureTypeRepository procedureTypeRepository;
	private final MedicineRepository medicineRepository;
	private final AdmissionRepository admissionRepository;
	private final PharmacyRepository pharmacyRepository;
	private final PharmacyMedicineRepository pharmacyMedicineRepository;
	private final PatientCreditNoteService patientCreditNoteService;
	private final CompanyProfileRepository companyProfileRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	private final WardBedRepository wardBedRepository;
	private final NurseRepository nurseRepository;
	private final PatientDressingChartRepository patientDressingChartRepository;
	private final PatientConsumableChartRepository patientConsumableChartRepository;
	private final PatientObservationChartRepository patientObservationChartRepository;
	private final PatientPrescriptionChartRepository patientPrescriptionChartRepository;
	private final PatientNursingChartRepository patientNursingChartRepository;
	private final PatientNursingProgressNoteRepository patientNursingProgressNoteRepository;
	private final PatientNursingCarePlanRepository patientNursingCarePlanRepository;
	private final ConsultationTransferRepository consultationTransferRepository;
	private final PharmacyMedicineBatchRepository pharmacyMedicineBatchRepository;
	private final DischargePlanRepository dischargePlanRepository;
	private final DeceasedNoteRepository deceasedNoteRepository;
	private final ReferralPlanRepository referralPlanRepository;
	
	private final ExternalMedicalProviderRepository externalMedicalProviderRepository;
	
	private final PrescriptionBatchRepository prescriptionBatchRepository;
	
	private final LabTestAttachmentRepository labTestAttachmentRepository;
	private final RadiologyAttachmentRepository radiologyAttachmentRepository;
	
	private final PatientVitalRepository patientVitalRepository;
	
	private final ClinicianPerformanceService clinicianPerformanceService;
	
	
	@GetMapping("/patients")
	public ResponseEntity<List<Patient>>getMaterials(
			HttpServletRequest request){
		return ResponseEntity.ok().body(patientService.getAll());
	}
	
	@GetMapping("/patients/get_by_search_key")
	public ResponseEntity<Patient> getProductBySearchKey(
			@RequestParam(name = "search_key") String searchKey,
			HttpServletRequest request){
		return ResponseEntity.ok().body(patientService.findBySearchKey(searchKey));
	}
	
	@GetMapping("/patients/get")
	public ResponseEntity<Patient> get(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(patientRepository.findById(id).get());
	}
	
	@GetMapping("/patients/get_all_search_keys")
	public ResponseEntity<List<String>> getSearchKeys(){
		List<String> keys = new ArrayList<String>();
		keys = patientService.getSearchKeys();
		return ResponseEntity.ok().body(keys);
	}
	
	@PostMapping("/patients/register")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-CREATE')")
	public ResponseEntity<Patient>registerPatient(
			@RequestBody Patient patient,
			HttpServletRequest request){
		if(patient.getNo().equals("") || patient.getNo().equals("NA")) {
			patient.setNo("NA");
		}
		if(patient.getPaymentType().equals("INSURANCE")) {
			InsurancePlan plan = insurancePlanRepository.findByName(patient.getInsurancePlan().getName()).get();
			patient.setInsurancePlan(plan);
			if(patient.getMembershipNo().equals("")) {
				throw new MissingInformationException("Membership number required");
			}
			
		}else {
			patient.setInsurancePlan(null);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/register").toUriString());
		return ResponseEntity.created(uri).body(patientService.doRegister(patient, request));
	}
	
	@PostMapping("/patients/change_payment_type")
	//@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-CREATE')")
	public ResponseEntity<Patient>changePaymentType(
			@RequestBody PatientModel patient,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patient.getId());
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found");
		}
		
		if(!(patient.getType() == "OUTPATIENT" || patient.getType() == "INPATIENT" || patient.getType() == "OUTSIDER")) {
			//throw new InvalidOperationException("Payment type can only be changed to OUTPATIENT, INPATIENT or OUTSIDER");
		}
				
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		statuses.add("STOPPED");
		statuses.add("HELD");
		
		List<Consultation> consultations = consultationRepository.findAllByPatientAndStatusIn(p.get(), statuses);
		if(!consultations.isEmpty()) {
			throw new InvalidOperationException("Could not change. Patient has an ongoing medical operation s");
		}
		List<NonConsultation> nonConsultations = nonConsultationRepository.findAllByPatientAndStatusIn(p.get(), statuses);
		if(!nonConsultations.isEmpty()) {			
			for(NonConsultation nonConsultation : nonConsultations) {
				List<LabTest> labTests = labTestRepository.findByNonConsultation(nonConsultation);
				if(!labTests.isEmpty()) {
					throw new InvalidOperationException("Could not change. Patient has an ongoing medical operation s");
				}
				List<Radiology> radiologies = radiologyRepository.findByNonConsultation(nonConsultation);
				if(!radiologies.isEmpty()) {
					throw new InvalidOperationException("Could not change. Patient has an ongoing medical operation s");
				}
				List<Procedure> procedures = procedureRepository.findByNonConsultation(nonConsultation);
				if(!procedures.isEmpty()) {
					throw new InvalidOperationException("Could not change. Patient has an ongoing medical operation s");
				}
				nonConsultation.setStatus("SIGNED-OUT");
				nonConsultationRepository.save(nonConsultation);
			}
		}
		List<Admission> admissions = admissionRepository.findAllByPatientAndStatusIn(p.get(), statuses);
		if(!admissions.isEmpty()) {
			throw new InvalidOperationException("Could not change. Patient has an ongoing medical operation s");
		}
		
		if(patient.getPaymentType().equals("INSURANCE")) {
			InsurancePlan plan = insurancePlanRepository.findByName(patient.getInsurancePlan().getName()).get();
			p.get().setPaymentType(patient.getPaymentType());
			p.get().setInsurancePlan(plan);
			p.get().setMembershipNo(patient.getMembershipNo());			
			if(patient.getMembershipNo().equals("")) {
				throw new MissingInformationException("Membership number required");
			}
			patientRepository.save(p.get());
		}else {
			p.get().setInsurancePlan(null);
			p.get().setMembershipNo("");
			p.get().setPaymentType("CASH");
			patientRepository.save(p.get());			
		}
		
		return null;
	}
	
	@PostMapping("/patients/update")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-UPDATE')")
	public ResponseEntity<Patient>updatePatient(
			@RequestBody Patient patient,
			HttpServletRequest request){
		if(patient.getPaymentType().equals("INSURANCE")) {
			InsurancePlan plan = insurancePlanRepository.findByName(patient.getInsurancePlan().getName()).get();
			patient.setInsurancePlan(plan);
			if(patient.getMembershipNo().equals("")) {
				throw new MissingInformationException("Membership number required");
			}
			
		}else {
			patient.setInsurancePlan(null);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/update").toUriString());
		return ResponseEntity.created(uri).body(patientService.update(patient, request));
	}
	
	@PostMapping("/patients/change_type")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-UPDATE')")
	public ResponseEntity<Patient>changeType(
			@RequestBody Patient patient,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(patient.getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Could not process, patient not available");
		}
		
		
		
		if(p.get().getType() == null) {
			p.get().setType("OUTPATIENT");{
				patientRepository.save(p.get());
			}
		}
		
		if((p.get().getType().equals("OUTPATIENT") || p.get().getType().equals("OUTSIDER"))){
			List<String> statuses = new ArrayList<>();
			statuses.add("PENDING");
			statuses.add("IN-PROCESS");
			statuses.add("TRANSFERED");
			if(p.get().getType().equals("OUTPATIENT")) {
				List<Consultation> cs = consultationRepository.findAllByPatientAndStatusIn(p.get(), statuses);
				if(cs.isEmpty() == false) {
					throw new InvalidOperationException("Can not change patient type, the patient has an active consultation.");
				}else {
					p.get().setType("OUTSIDER");
					patient = patientRepository.save(p.get());
				}
			}else if(p.get().getType().equals("OUTSIDER")) {
				List<NonConsultation> ncs = nonConsultationRepository.findAllByPatientAndStatusIn(p.get(), statuses);				
				boolean cancelable = true;	
				if(ncs.isEmpty() == false) {					
					for(NonConsultation nc : ncs) {						
						List<LabTest> tests = labTestRepository.findAllByNonConsultation(nc);
						for(LabTest test : tests) {
							if(test.getStatus() != null) {
								if(test.getStatus().equals("PENDING")) {
									PatientBill bill = test.getPatientBill();
									if(bill.getStatus() != null) {
										if(bill.getStatus().equals("UNPAID")) {
											labTestRepository.delete(test);
											patientBillRepository.delete(bill);
										}else {
											cancelable = false;
										}
									}else {
										labTestRepository.delete(test);
										patientBillRepository.delete(bill);
									}
								}
							}
						}						
						List<Radiology> radiologies = radiologyRepository.findAllByNonConsultation(nc);
						for(Radiology radiology : radiologies) {
							if(radiology.getStatus() != null) {
								if(radiology.getStatus().equals("PENDING")) {
									PatientBill bill = radiology.getPatientBill();
									if(bill.getStatus() != null) {
										if(bill.getStatus().equals("UNPAID")) {
											radiologyRepository.delete(radiology);
											patientBillRepository.delete(bill);
										}else {
											cancelable = false;
										}
									}else {
										radiologyRepository.delete(radiology);
										patientBillRepository.delete(bill);
									}
								}
							}
						}						
						List<Procedure> procedures = procedureRepository.findAllByNonConsultation(nc);
						for(Procedure procedure : procedures) {
							if(procedure.getStatus() != null) {
								if(procedure.getStatus().equals("PENDING")) {
									PatientBill bill = procedure.getPatientBill();
									if(bill.getStatus() != null) {
										if(bill.getStatus().equals("UNPAID")) {
											procedureRepository.delete(procedure);
											patientBillRepository.delete(bill);
										}else {
											cancelable = false;
										}
									}else {
										procedureRepository.delete(procedure);
										patientBillRepository.delete(bill);
									}
								}
							}
						}
					}
				}
				if(cancelable == false) {
					throw new InvalidOperationException("Can not change patient type, the has pending paid services. Please consider clearing with the patient.");
				}
				p.get().setType("OUTPATIENT");
				patient = patientRepository.save(p.get());
			}
		}else if(p.get().getType().equals("INPATIENT")) {
			throw new InvalidOperationException("This operation is not allowed for inpatients");
		}else {
			throw new InvalidOperationException("Patient type could not be changed.");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/change_type").toUriString());
		return ResponseEntity.created(uri).body(patient);
	}
	
	@PostMapping("/patients/do_consultation")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-CREATE','PATIENT-UPDATE')")
	public ResponseEntity<Patient>consultation(

			@RequestParam Long patient_id, @RequestParam String clinic_name, @RequestParam String clinician_name, 
			HttpServletRequest request){
		Optional<Patient> patient_ = patientRepository.findById(patient_id);
		Optional<Clinic> c = clinicRepository.findByName(clinic_name);
		Optional<Clinician> cn = clinicianRepository.findByNickname(clinician_name);
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		List<Admission> adms = admissionRepository.findAllByPatientAndStatusIn(patient_.get(), statuses);
		if(!adms.isEmpty()) {
			throw new InvalidOperationException("Could not process consultation, the patient has an active admission");
		}
		if(!patient_.get().getType().equals("OUTPATIENT")) {
			throw new InvalidOperationException("Please change patient type to OUTPATIENT to continue with operation");
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/do_consultation").toUriString());
		return ResponseEntity.created(uri).body(patientService.doConsultation(patient_.get(), c.get(), cn.get(), request));
	}
	
	@PostMapping("/patients/create_consultation_transfer")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<ConsultationTransfer>doConsultationTransfer(
			@RequestBody ConsultationTransfer transfer, 
			HttpServletRequest request){
		Optional<Consultation> con = consultationRepository.findById(transfer.getConsultation().getId());		
		Optional<Clinic> c = clinicRepository.findById(transfer.getClinic().getId());
		
		
		if(con.isEmpty()) {
			throw new NotFoundException("Consultation not found");
		}
		if(c.isEmpty()) {
			throw new NotFoundException("Clinic not found");
		}
		
		transfer.setConsultation(con.get());
		transfer.setClinic(c.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/create_consultation_transfer").toUriString());
		return ResponseEntity.created(uri).body(patientService.createConsultationTransfer(transfer, request));
	}
	
	@GetMapping("/patients/get_consultation_transfers")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<ConsultationTransfer>>getConsultationTransfers(
			HttpServletRequest request){
		
		List<ConsultationTransfer> transfers = consultationTransferRepository.findAllByStatus("PENDING");
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_consultation_transfers").toUriString());
		return ResponseEntity.created(uri).body(transfers);
	}
	
	@PostMapping("/patients/cancel_consultation")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-CREATE','PATIENT-UPDATE')")
	public ResponseEntity<Boolean>cancelConsultation(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(!c.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not cancel, only a PENDING consultation can be canceled");
		}
		/**
		 * Cancel the consultation
		 */
		Consultation consultation = c.get();
		consultation.setStatus("CANCELED");
		consultation = consultationRepository.save(consultation);		
		/**
		 * Now find the patientBill associated with the consultation
		 */
		PatientBill patientBill = patientBillRepository.findById(consultation.getPatientBill().getId()).get();
		/**
		 * Now cancel the patientBill
		 */
		patientBill.setStatus("CANCELED");
		patientBill = patientBillRepository.save(patientBill);
		/**
		 * Find payment associated with the patientBill, if there is
		 */
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a payment associated with this patientBill, refund it, and create a credit note for it
		 */
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			PatientPaymentDetail ppd = pd.get();
			ppd.setStatus("REFUNDED");
			ppd = patientPaymentDetailRepository.save(ppd);
			/**
			 * Create credit note
			 */
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(ppd.getPatientBill().getAmount());
			patientCreditNote.setPatient(consultation.getPatient());
			patientCreditNote.setReference("Canceled consultation");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		/**
		 * Find patientInvoice detail associated with this patientBill
		 */
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/cancel_consultation").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/free_consultation")
	@PreAuthorize("hasAnyAuthority('PATIENT-ALL','PATIENT-CREATE','PATIENT-UPDATE')")
	public ResponseEntity<Boolean>freeConsultation(
			@RequestParam Long id, 
			@RequestParam String no,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(!c.get().getStatus().equals("TRANSFERED")) {
			if(c.get().getStatus().equals("IN-PROCESS")) {
				if(no.isEmpty()) {
					throw new InvalidEntryException("To free the patient, please enter patients registration number");
				}
				Optional<Patient> p = patientRepository.findByNo(no);
				if(p.isEmpty()) {
					throw new NotFoundException("Invalid number");
				}
				if(p.get().getId() != c.get().getPatient().getId()) {
					throw new NotFoundException("Invalid number");
				}
				c.get().setStatus("SIGNED-OUT");
				consultationRepository.save(c.get());
				List<LabTest> labTests = labTestRepository.findByConsultation(c.get());
				for (LabTest labTest : labTests) {
					PatientBill patientBill = labTest.getPatientBill();
					if(patientBill.getStatus() != null) {
						if(patientBill.getStatus().equals("UNPAID")) {
							patientBill.setStatus("CANCELED");
							patientBillRepository.save(patientBill);
						}
					}else {
						patientBill.setStatus("CANCELED");
						patientBillRepository.save(patientBill);
					}
					
				}
				List<Radiology> radiologies = radiologyRepository.findByConsultation(c.get());
				for (Radiology radiology : radiologies) {
					PatientBill patientBill = radiology.getPatientBill();
					if(patientBill.getStatus() != null) {
						if(patientBill.getStatus().equals("UNPAID")) {
							patientBill.setStatus("CANCELED");
							patientBillRepository.save(patientBill);
						}
					}else {
						patientBill.setStatus("CANCELED");
						patientBillRepository.save(patientBill);
					}
				}
				List<Procedure> procedures = procedureRepository.findByConsultation(c.get());
				for (Procedure procedure : procedures) {
					PatientBill patientBill = procedure.getPatientBill();
					if(patientBill.getStatus() != null) {
						if(patientBill.getStatus().equals("UNPAID")) {
							patientBill.setStatus("CANCELED");
							patientBillRepository.save(patientBill);
						}
					}else {
						patientBill.setStatus("CANCELED");
						patientBillRepository.save(patientBill);
					}
				}
				
				List<Prescription> prescriptions = prescriptionRepository.findByConsultation(c.get());
				for (Prescription prescription : prescriptions) {
					PatientBill patientBill = prescription.getPatientBill();
					if(patientBill.getStatus() != null) {
						if(patientBill.getStatus().equals("UNPAID")) {
							patientBill.setStatus("CANCELED");
							patientBillRepository.save(patientBill);
						}
					}else {
						patientBill.setStatus("CANCELED");
						patientBillRepository.save(patientBill);
					}
				}
				
			}else {
				throw new InvalidOperationException("Could not free, only a TRANSFERED or IN-PROCESS consultation can be freed");
			}
		}
		/**
		 * Cancel the consultation
		 */
		Consultation consultation = c.get();
		consultation.setStatus("SIGNED-OUT");
		consultation = consultationRepository.save(consultation);		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/free_consultation").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@GetMapping("/patients/get_active_consultations")
	public ResponseEntity<List<Consultation>>getActiveConsultations(
			@RequestParam Long patient_id,
			HttpServletRequest request){
			
		Optional<Patient> p = patientRepository.findById(patient_id);
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		statuses.add("TRANSFERED");
		List<Consultation> consultations = consultationRepository.findAllByPatientAndStatusIn(p.get(), statuses);
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_active_consultation").toUriString());
		return ResponseEntity.created(uri).body(consultations);
	}
	
	@GetMapping("/patients/last_visit_date_time")
	public ResponseEntity<LocalDateTime>getLastVisitDateTime(
			@RequestParam(name = "patient_id") Long patient_id,
			HttpServletRequest request){
			
		Optional<Patient> p = patientRepository.findById(patient_id);
		
		List<Visit> visits = visitRepository.findAllByPatient(p.get());
		LocalDateTime lastVistiDateTime = null;
		for(Visit visit : visits) {
			lastVistiDateTime = visit.getCreatedAt();
		}
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/last_visit_date_time").toUriString());
		return ResponseEntity.created(uri).body(lastVistiDateTime);
	}
	
	@GetMapping("/patients/load_pending_consultations_by_clinician_id")    // to do later
	public ResponseEntity<List<Consultation>> loadPendingConsultationsByClinician(
			@RequestParam(name = "clinician_id") Long clinicianId,
			HttpServletRequest request){
		if(clinicianId == null) {
			throw new NotFoundException("User not present in doctors register");
		}
		Optional<Clinician> c = clinicianRepository.findById(clinicianId);
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		List<Consultation> cons = consultationRepository.findAllByClinicianAndStatusIn(c.get(), statuses);
		/**
		 * Should load paid or insurance covered consultations only
		 */
		List<Consultation> consultationsToShow = new ArrayList<>();
		for(Consultation cn : cons) {
			if(cn.getPatientBill().getStatus().equals("PAID") || cn.getPatientBill().getStatus().equals("COVERED")) {
				consultationsToShow.add(cn);
			}
		}		
		return ResponseEntity.ok().body(consultationsToShow);
	}
	
	@GetMapping("/patients/load_in_process_consultations_by_clinician_id")    // to do later
	public ResponseEntity<List<Consultation>> loadInProcessConsultationsByClinician(
			@RequestParam(name = "clinician_id") Long clinicianId,
			HttpServletRequest request){
		if(clinicianId == null) {
			throw new NotFoundException("User not present in doctors register");
		}
		Optional<Clinician> c = clinicianRepository.findById(clinicianId);
		if(!c.isPresent()) {
			throw new NotFoundException("Clinician not found");
		}
		
		List<String> statuses = new ArrayList<>();
		statuses.add("IN-PROCESS");
		statuses.add("TRANSFERED");
		List<Consultation> cons = consultationRepository.findAllByClinicianAndStatusIn(c.get(), statuses);
		
		return ResponseEntity.ok().body(cons);
	}
	
	@GetMapping("/patients/open_consultation")    // to do later
	public ResponseEntity<Boolean> openConsultation(
			@RequestParam(name = "consultation_id") Long consultationId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		if(c.get().getStatus().equals("PENDING")) {
			if(c.get().getPatientBill().getStatus().equals("PAID") || c.get().getPatientBill().getStatus().equals("COVERED")) {
				c.get().setStatus("IN-PROCESS");
				consultationRepository.save(c.get());
				
				ClinicianPerformance clinicianPerformance = new ClinicianPerformance();
				clinicianPerformance.setClinician(c.get().getClinician());
				clinicianPerformance.setConsultation(c.get());
				
				clinicianPerformanceService.check(clinicianPerformance, request);
				return ResponseEntity.ok().body(true);
			}else {
				throw new InvalidOperationException("Could not open. Payment not verified.");
			}
		}else {
			throw new InvalidOperationException("Could not open. Not a pending consultation.");
		}				
	}
	
	@GetMapping("/patients/load_consultation")    // to do later
	public ResponseEntity<Consultation> loadConsultation(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(c.isPresent()) {
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_consultation").toUriString());
			return ResponseEntity.created(uri).body(c.get());
		}else {
			throw new NotFoundException("Consultation not found");
		}
	}
	
	@GetMapping("/patients/load_non_consultation")    // to do later
	public ResponseEntity<NonConsultation> loadNonConsultation(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<NonConsultation> nc = nonConsultationRepository.findById(id);
		if(nc.isPresent()) {
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_non_consultation").toUriString());
			return ResponseEntity.created(uri).body(nc.get());
		}else {
			throw new NotFoundException("Non Consultation not found");
		}
	}
	
	@GetMapping("/patients/cancel_consultation_transfer")    // to do later
	public ResponseEntity<Consultation> cancelConsultationTransfer(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(c.isPresent()) {			
			if(c.get().getStatus().equals("TRANSFERED")) {
				Optional<ConsultationTransfer> conTra = consultationTransferRepository.findByConsultationAndStatus(c.get(), "PENDING");
				if(conTra.isPresent()) {
					if(conTra.get().getStatus().equals("PENDING")) {
						conTra.get().setStatus("CANCELED");
						consultationTransferRepository.save(conTra.get());
						c.get().setStatus("IN-PROCESS");	
						consultationRepository.save(c.get());
					}else {
						throw new InvalidOperationException("Could not cancel transfer");
					}
				}
			}
			
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/consultation_transfer").toUriString());
			return ResponseEntity.created(uri).body(c.get());
		}else {
			throw new NotFoundException("Consultation not found");
		}
	}
	
	
	////Dohere patient vital, this is draft, no time, update it, called in a meeting
	
	
	
	@PostMapping("/patients/save_patient_vitals") 
	public ResponseEntity<PatientVital> savePatientVitals(
			@RequestBody PatientVital patientVital,
			HttpServletRequest request){
		
		Optional<Consultation> c = consultationRepository.findById(patientVital.getConsultation().getId());
		Optional<NonConsultation> nc = nonConsultationRepository.findById(patientVital.getNonConsultation().getId());
		Optional<Admission> a = admissionRepository.findById(patientVital.getAdmission().getId());
		
		Consultation consultation = null;
		NonConsultation nonConsultation = null;
		Admission admission = null;
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		
		if(c.isPresent() && nc.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(c.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		
		PatientVital vital = null;
		if(c.isPresent() && nc.isEmpty() && a.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByConsultation(c.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			consultation = c.get();
		}
		if(c.isEmpty() && a.isPresent() && nc.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByAdmission(a.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			admission = a.get();
		}
		
		if(c.isEmpty() && nc.isPresent() && a.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByNonConsultation(nc.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			nonConsultation = nc.get();
		}
		
		if(c.isEmpty() && a.isEmpty() && nc.isEmpty()) {
			throw new NotFoundException("No Admission or Consultation or Non Consultation found");
		}
		
		if(vital != null) {
			if(vital.getStatus().equals("SUBMITTED")) {
				throw new InvalidOperationException("Could not modify vitals. Vitals already submitted");
			}
			vital.setBodyMassIndex(patientVital.getBodyMassIndex());
			vital.setBodyMassIndexComment(patientVital.getBodyMassIndexComment());
			vital.setBodySurfaceArea(patientVital.getBodySurfaceArea());
			vital.setHeight(patientVital.getHeight());
			vital.setPressure(patientVital.getPressure());
			vital.setPulseRate(patientVital.getPulseRate());
			vital.setRespiratoryRate(patientVital.getRespiratoryRate());
			vital.setSaturationOxygen(patientVital.getSaturationOxygen());
			vital.setTemperature(patientVital.getTemperature());
			vital.setWeight(patientVital.getWeight());
			vital.setDescription(patientVital.getDescription());
			vital.setStatus("PENDING");
			
			vital = patientVitalRepository.save(vital);
		}else {
			vital = new PatientVital();
			vital.setBodyMassIndex(patientVital.getBodyMassIndex());
			vital.setBodyMassIndexComment(patientVital.getBodyMassIndexComment());
			vital.setBodySurfaceArea(patientVital.getBodySurfaceArea());
			vital.setHeight(patientVital.getHeight());
			vital.setPressure(patientVital.getPressure());
			vital.setPulseRate(patientVital.getPulseRate());
			vital.setRespiratoryRate(patientVital.getRespiratoryRate());
			vital.setSaturationOxygen(patientVital.getSaturationOxygen());
			vital.setTemperature(patientVital.getTemperature());
			vital.setWeight(patientVital.getWeight());
			vital.setDescription(patientVital.getDescription());
			vital.setConsultation(consultation);
			vital.setNonConsultation(nonConsultation);
			vital.setAdmission(admission);
			vital.setStatus("PENDING");
			
			vital.setCreatedBy(userService.getUser(request).getId());
			vital.setCreatedOn(dayService.getDay().getId());
			vital.setCreatedAt(dayService.getTimeStamp());
			
			vital = patientVitalRepository.save(vital);
		}
			
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_vitals").toUriString());
		return ResponseEntity.created(uri).body(vital);
	}
	
	
	
	@PostMapping("/patients/submit_patient_vitals") 
	public ResponseEntity<PatientVital> submitPatientVitals(
			@RequestBody PatientVital patientVital,
			HttpServletRequest request){
		
		Optional<Consultation> c = consultationRepository.findById(patientVital.getConsultation().getId());
		Optional<NonConsultation> nc = nonConsultationRepository.findById(patientVital.getNonConsultation().getId());
		Optional<Admission> a = admissionRepository.findById(patientVital.getAdmission().getId());
		
		//Consultation consultation = null;
		//NonConsultation nonConsultation = null;
		//Admission admission = null;
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		
		if(c.isPresent() && nc.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(c.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		
		PatientVital vital = null;
		if(c.isPresent() && nc.isEmpty() && a.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByConsultation(c.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			//consultation = c.get();
		}
		if(c.isEmpty() && a.isPresent() && nc.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByAdmission(a.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			//admission = a.get();
		}
		
		if(c.isEmpty() && nc.isPresent() && a.isEmpty()) {
			
			List<PatientVital> vitals = patientVitalRepository.findAllByNonConsultation(nc.get());
			for(PatientVital v : vitals) {
				vital = v;
			}
			//nonConsultation = nc.get();
		}
		
		if(c.isEmpty() && a.isEmpty() && nc.isEmpty()) {
			throw new NotFoundException("No Admission or Consultation or Non Consultation found");
		}
		
		if(vital != null) {
			if(vital.getStatus().equals("SUBMITTED")) {
				throw new InvalidOperationException("Could not resubmit vitals. Vitals already submitted");
			}else if(vital.getStatus().equals("PENDING")) {
				vital.setStatus("SUBMITTED");
				vital = patientVitalRepository.save(vital);
			}else {
				throw new InvalidOperationException("Operation failed");
			}
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/submit_patient_vitals").toUriString());
		return ResponseEntity.created(uri).body(vital);
	}
	
	
	
	
	
	
	
	
	@GetMapping("/patients/load_admission")    // to do later
	public ResponseEntity<Admission> loadAdmission(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(id);
		if(a.isPresent()) {
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_admission").toUriString());
			
			ClinicianPerformance clinicianPerformance = new ClinicianPerformance();
			
			Optional<Clinician> _clinician = clinicianRepository.findByUser(userService.getUser(request));
			
			clinicianPerformance.setClinician(_clinician.get());
			clinicianPerformance.setAdmission(a.get());
			
			clinicianPerformanceService.check(clinicianPerformance, request);
			
			return ResponseEntity.created(uri).body(a.get());
		}else {
			throw new NotFoundException("Admission not found");
		}
	}
	
	@GetMapping("/patients/load_clinical_note_by_consultation_id")
	public ResponseEntity<ClinicalNote> loadClinicalNoteByConsultationId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);		
		if(c.isPresent()) {
			Optional<ClinicalNote> n = clinicalNoteRepository.findByConsultation(c.get());
			if(n.isPresent()) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_clinical_note_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(n.get());
			}else {
				/**
				 * Create one, and return it
				 */
				ClinicalNote note = new ClinicalNote();
				note.setConsultation(c.get());
				
				note.setCreatedBy(userService.getUser(request).getId());
				note.setCreatedOn(dayService.getDay().getId());
				note.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_clinical_note_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(clinicalNoteRepository.save(note));
			}
		}else {
			throw new NotFoundException("Consultation not found");
		}
			
	}
	
	@GetMapping("/patients/load_general_examination_by_consultation_id")
	public ResponseEntity<GeneralExamination> loadGeneralExaminationByConsultationId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);		
		if(c.isPresent()) {
			Optional<GeneralExamination> n = generalExaminationRepository.findByConsultation(c.get());
			if(n.isPresent()) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(n.get());
			}else {
				/**
				 * Create one, and return it
				 */
				GeneralExamination exam = new GeneralExamination();
				exam.setConsultation(c.get());
				
				exam.setCreatedBy(userService.getUser(request).getId());
				exam.setCreatedOn(dayService.getDay().getId());
				exam.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(generalExaminationRepository.save(exam));
			}
		}else {
			throw new NotFoundException("Consultation not found");
		}
			
	}
	
	@GetMapping("/patients/load_patient_vitals_by_consultation_id")
	public ResponseEntity<PatientVital> loadPatientVitalByConsultationId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);		
		if(c.isPresent()) {
			Optional<PatientVital> n = patientVitalRepository.findByConsultation(c.get());
			if(n.isPresent()) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_patient_vitals_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(n.get());
			}else {
				/**
				 * Create one, and return it
				 */
				PatientVital vital = new PatientVital();
				vital.setConsultation(c.get());
				vital.setStatus("EMPTY");
				
				vital.setCreatedBy(userService.getUser(request).getId());
				vital.setCreatedOn(dayService.getDay().getId());
				vital.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_patient_vitals_by_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(patientVitalRepository.save(vital));
			}
		}else {
			throw new NotFoundException("Consultation not found");
		}
			
	}
	
	
	@GetMapping("/patients/request_patient_vitals_by_consultation_id")
	public ResponseEntity<GeneralExamination> requestPatientVitalByConsultationId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);	
		Optional<PatientVital> vital_ = patientVitalRepository.findByConsultation(c.get());
		if(vital_.isPresent()) {
			if(!vital_.get().getStatus().equals("SUBMITTED")) {
				throw new InvalidOperationException("Vitals already requested or not submitted");
			}
			
			GeneralExamination exam = new GeneralExamination();
			exam.setBodyMassIndex(vital_.get().getBodyMassIndex());
			exam.setBodyMassIndexComment(vital_.get().getBodyMassIndexComment());
			exam.setBodySurfaceArea(vital_.get().getBodySurfaceArea());
			exam.setHeight(vital_.get().getHeight());
			exam.setPressure(vital_.get().getPressure());
			exam.setPulseRate(vital_.get().getPulseRate());
			exam.setRespiratoryRate(vital_.get().getRespiratoryRate());
			exam.setSaturationOxygen(vital_.get().getSaturationOxygen());
			exam.setTemperature(vital_.get().getTemperature());
			exam.setWeight(vital_.get().getWeight());
			exam.setDescription(vital_.get().getDescription());
			vital_.get().setStatus("ARCHIVED");
			patientVitalRepository.save(vital_.get());
			URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_patient_vitals_by_consultation_id").toUriString());
			return ResponseEntity.created(uri).body(exam);
		}else {
			throw new NotFoundException("Vital Signs not found");
		}
		
	}
	
	
	
	
	@GetMapping("/patients/load_clinical_note_by_admission_id")
	public ResponseEntity<ClinicalNote> loadClinicalNoteByAdmissionId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(id);		
		if(a.isPresent()) {
			
			
			List<ClinicalNote> ns = clinicalNoteRepository.findAllByAdmission(a.get());
			ClinicalNote note = null;
			for(ClinicalNote n : ns) {
				note = n;
			}
			
			if(note != null) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_clinical_note_by_admission_id").toUriString());
				return ResponseEntity.created(uri).body(note);
			}else {
				/**
				 * Create one, and return it
				 */
				note = new ClinicalNote();
				note.setAdmission(a.get());
				
				note.setCreatedBy(userService.getUser(request).getId());
				note.setCreatedOn(dayService.getDay().getId());
				note.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_clinical_note_by_admission_id").toUriString());
				return ResponseEntity.created(uri).body(clinicalNoteRepository.save(note));
			}
		}else {
			throw new NotFoundException("Admission not found");
		}
			
	}
	
	@GetMapping("/patients/load_general_examination_by_non_consultation_id")
	public ResponseEntity<GeneralExamination> loadGeneralExaminationByNonConsultationId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<NonConsultation> nc = nonConsultationRepository.findById(id);		
		if(nc.isPresent()) {
			Optional<GeneralExamination> n = generalExaminationRepository.findByNonConsultation(nc.get());
			if(n.isPresent()) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_non_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(n.get());
			}else {
				/**
				 * Create one, and return it
				 */
				GeneralExamination exam = new GeneralExamination();
				exam.setNonConsultation(nc.get());
				
				exam.setCreatedBy(userService.getUser(request).getId());
				exam.setCreatedOn(dayService.getDay().getId());
				exam.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_non_consultation_id").toUriString());
				return ResponseEntity.created(uri).body(generalExaminationRepository.save(exam));
			}
		}else {
			throw new NotFoundException("Non Consultation not found");
		}
			
	}
	
	@GetMapping("/patients/load_general_examination_by_admission_id")
	public ResponseEntity<GeneralExamination> loadGeneralExaminationByAdmissionId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(id);		
		if(a.isPresent()) {
			
			List<GeneralExamination> es = generalExaminationRepository.findAllByAdmission(a.get());
			GeneralExamination exam = null;
			for(GeneralExamination e : es) {
				exam = e;
			}
			
			//Optional<GeneralExamination> n = generalExaminationRepository.findByConsultation(c.get());
			if(exam != null) {
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_admission_id").toUriString());
				return ResponseEntity.created(uri).body(exam);
			}else {
				/**
				 * Create one, and return it
				 */
				exam = new GeneralExamination();
				exam.setAdmission(a.get());
				
				exam.setCreatedBy(userService.getUser(request).getId());
				exam.setCreatedOn(dayService.getDay().getId());
				exam.setCreatedAt(dayService.getTimeStamp());
				
				URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_general_examination_by_admission_id").toUriString());
				return ResponseEntity.created(uri).body(generalExaminationRepository.save(exam));
			}
		}else {
			throw new NotFoundException("Consultation not found");
		}
			
	}
	
	@PostMapping("/patients/save_clinical_note_and_general_examination") 
	public ResponseEntity<CG> saveCG(
			@RequestBody CG cg,
			HttpServletRequest request){
		
		Optional<Consultation> c = consultationRepository.findById(cg.getClinicalNote().getConsultation().getId());
		Optional<NonConsultation> nc = nonConsultationRepository.findById(cg.getClinicalNote().getNonConsultation().getId());
		Optional<Admission> a = admissionRepository.findById(cg.getClinicalNote().getAdmission().getId());
		
		Consultation consultation = null;
		NonConsultation nonConsultation = null;
		Admission admission = null;
		
		if(c.isPresent() && nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		
		if(c.isPresent() && nc.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(nc.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		if(c.isPresent() && a.isPresent()) {
			throw new InvalidOperationException("Patient can not have admission and consultation and outsider simultaneously");
		}
		ClinicalNote note = null;
		GeneralExamination exam = null;
		if(c.isPresent() && nc.isEmpty() && a.isEmpty()) {
			List<ClinicalNote> notes = clinicalNoteRepository.findAllByConsultation(c.get());
			for(ClinicalNote n : notes) {
				note = n;
			}
			List<GeneralExamination> exams = generalExaminationRepository.findAllByConsultation(c.get());
			for(GeneralExamination e : exams) {
				exam = e;
			}
			consultation = c.get();
		}
		if(c.isEmpty() && a.isPresent() && nc.isEmpty()) {
			List<ClinicalNote> notes = clinicalNoteRepository.findAllByAdmission(a.get());
			for(ClinicalNote n : notes) {
				note = n;
			}
			List<GeneralExamination> exams = generalExaminationRepository.findAllByAdmission(a.get());
			for(GeneralExamination e : exams) {
				exam = e;
			}
			admission = a.get();
		}
		
		if(c.isEmpty() && nc.isPresent() && a.isEmpty()) {
			List<ClinicalNote> notes = clinicalNoteRepository.findAllByNonConsultation(nc.get());
			for(ClinicalNote n : notes) {
				note = n;
			}
			List<GeneralExamination> exams = generalExaminationRepository.findAllByNonConsultation(nc.get());
			for(GeneralExamination e : exams) {
				exam = e;
			}
			nonConsultation = nc.get();
		}
		
		if(c.isEmpty() && a.isEmpty() && nc.isEmpty()) {
			throw new NotFoundException("No Admission or Consultation found");
		}
		//Optional<ClinicalNote> cn = clinicalNoteRepository.findByConsultation(c.get());
		//ClinicalNote note = new ClinicalNote();
		if(note != null) {
			note.setMainComplain(cg.getClinicalNote().getMainComplain());
			note.setDrugsAndAllergyHistory(cg.getClinicalNote().getDrugsAndAllergyHistory());
			note.setFamilyAndSocialHistory(cg.getClinicalNote().getFamilyAndSocialHistory());
			note.setPastMedicalHistory(cg.getClinicalNote().getPastMedicalHistory());
			note.setPhysicalExamination(cg.getClinicalNote().getPhysicalExamination());
			note.setPresentIllnessHistory(cg.getClinicalNote().getPresentIllnessHistory());
			note.setReviewOfOtherSystems(cg.getClinicalNote().getReviewOfOtherSystems());
			note.setManagementPlan(cg.getClinicalNote().getManagementPlan());
			
			note = clinicalNoteRepository.save(note);
		}else {
			note = new ClinicalNote();
			note.setMainComplain(cg.getClinicalNote().getMainComplain());
			note.setDrugsAndAllergyHistory(cg.getClinicalNote().getDrugsAndAllergyHistory());
			note.setFamilyAndSocialHistory(cg.getClinicalNote().getFamilyAndSocialHistory());
			note.setPastMedicalHistory(cg.getClinicalNote().getPastMedicalHistory());
			note.setPhysicalExamination(cg.getClinicalNote().getPhysicalExamination());
			note.setPresentIllnessHistory(cg.getClinicalNote().getPresentIllnessHistory());
			note.setReviewOfOtherSystems(cg.getClinicalNote().getReviewOfOtherSystems());
			note.setManagementPlan(cg.getClinicalNote().getManagementPlan());
			note.setConsultation(consultation);
			note.setNonConsultation(nonConsultation);
			note.setAdmission(admission);
			
			note.setCreatedBy(userService.getUser(request).getId());
			note.setCreatedOn(dayService.getDay().getId());
			note.setCreatedAt(dayService.getTimeStamp());
			
			note = clinicalNoteRepository.save(note);
		}
		
		//c = consultationRepository.findById(cg.getGeneralExamination().getConsultation().getId());
		//if(!c.isPresent()) {
			//throw new NotFoundException("Consultation not found");
		//}
		//Optional<GeneralExamination> ge = generalExaminationRepository.findByConsultation(c.get());
		//GeneralExamination exam = new GeneralExamination();
		if(exam != null) {
			exam.setBodyMassIndex(cg.getGeneralExamination().getBodyMassIndex());
			exam.setBodyMassIndexComment(cg.getGeneralExamination().getBodyMassIndexComment());
			exam.setBodySurfaceArea(cg.getGeneralExamination().getBodySurfaceArea());
			exam.setHeight(cg.getGeneralExamination().getHeight());
			exam.setPressure(cg.getGeneralExamination().getPressure());
			exam.setPulseRate(cg.getGeneralExamination().getPulseRate());
			exam.setRespiratoryRate(cg.getGeneralExamination().getRespiratoryRate());
			exam.setSaturationOxygen(cg.getGeneralExamination().getSaturationOxygen());
			exam.setTemperature(cg.getGeneralExamination().getTemperature());
			exam.setWeight(cg.getGeneralExamination().getWeight());
			exam.setDescription(cg.getGeneralExamination().getDescription());
			
			exam = generalExaminationRepository.save(exam);
		}else {
			exam = new GeneralExamination();
			exam.setBodyMassIndex(cg.getGeneralExamination().getBodyMassIndex());
			exam.setBodyMassIndexComment(cg.getGeneralExamination().getBodyMassIndexComment());
			exam.setBodySurfaceArea(cg.getGeneralExamination().getBodySurfaceArea());
			exam.setHeight(cg.getGeneralExamination().getHeight());
			exam.setPressure(cg.getGeneralExamination().getPressure());
			exam.setPulseRate(cg.getGeneralExamination().getPulseRate());
			exam.setRespiratoryRate(cg.getGeneralExamination().getRespiratoryRate());
			exam.setSaturationOxygen(cg.getGeneralExamination().getSaturationOxygen());
			exam.setTemperature(cg.getGeneralExamination().getTemperature());
			exam.setWeight(cg.getGeneralExamination().getWeight());
			exam.setDescription(cg.getGeneralExamination().getDescription());
			exam.setConsultation(consultation);
			exam.setNonConsultation(nonConsultation);
			exam.setAdmission(admission);
			
			exam.setCreatedBy(userService.getUser(request).getId());
			exam.setCreatedOn(dayService.getDay().getId());
			exam.setCreatedAt(dayService.getTimeStamp());
			
			exam = generalExaminationRepository.save(exam);
		}
		
		CG cgToReturn = new CG();
		cgToReturn.setClinicalNote(note);
		cgToReturn.setGeneralExamination(exam);
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_clinical_note_and_general_examination").toUriString());
		return ResponseEntity.created(uri).body(cgToReturn);
	}
	
	@PostMapping("/patients/send_clinical_note_and_general_examination_to_history") 
	public ResponseEntity<CG> sendCGToHistory(
			@RequestBody CG cg,
			HttpServletRequest request){
		
		Optional<Consultation> c = consultationRepository.findById(cg.getClinicalNote().getConsultation().getId());
		Optional<Admission> a = admissionRepository.findById(cg.getClinicalNote().getAdmission().getId());
		
		if(c.isPresent()) {
			throw new InvalidOperationException("Not Allowed for consultations");
		}
				
		if(a.isEmpty()) {
			throw new NotFoundException("No Admission found");
		}
		
		ClinicalNote note = new ClinicalNote();	
		note.setAdmission(a.get());
		
		note.setCreatedBy(userService.getUser(request).getId());
		note.setCreatedOn(dayService.getDay().getId());
		note.setCreatedAt(dayService.getTimeStamp());
		note = clinicalNoteRepository.saveAndFlush(note);
		
		GeneralExamination exam = new GeneralExamination();
		exam.setAdmission(a.get());
		
		exam.setCreatedBy(userService.getUser(request).getId());
		exam.setCreatedOn(dayService.getDay().getId());
		exam.setCreatedAt(dayService.getTimeStamp());
		exam = generalExaminationRepository.saveAndFlush(exam);
		
		
		
		CG cgToReturn = new CG();
		cgToReturn.setClinicalNote(note);
		cgToReturn.setGeneralExamination(exam);
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/send_clinical_note_and_general_examination_to_history").toUriString());
		return ResponseEntity.created(uri).body(cgToReturn);
	}
	
	@PostMapping("/patients/save_working_diagnosis") 
	public ResponseEntity<WorkingDiagnosis> saveWorkingDiagnosis(
			@RequestBody WorkingDiagnosis diagnosis,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(diagnosis.getConsultation().getId());
		if(!c.isPresent()) {
			throw new NotFoundException("Consultation not found");
		}
		Optional<DiagnosisType> dt = diagnosisTypeRepository.findById(diagnosis.getDiagnosisType().getId());
		if(!dt.isPresent()) {
			throw new NotFoundException("Diagnosis type not found");
		}
		if(workingDiagnosisRepository.existsByConsultationAndDiagnosisType(c.get(), dt.get())) {
			throw new InvalidOperationException("Duplicate Diagnosis Types is not allowed");
		}
		diagnosis.setConsultation(c.get());
		diagnosis.setDiagnosisType(dt.get());
		diagnosis.setPatient(c.get().getPatient());
		
		if(diagnosis.getId() == null) {
			diagnosis.setCreatedBy(userService.getUser(request).getId());
			diagnosis.setCreatedOn(dayService.getDay().getId());
			diagnosis.setCreatedAt(dayService.getTimeStamp());
		}
		
		
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_working_diagnosis").toUriString());
		return ResponseEntity.created(uri).body(workingDiagnosisRepository.save(diagnosis));
	}
	
	@PostMapping("/patients/save_admission_working_diagnosis") 
	public ResponseEntity<WorkingDiagnosis> saveAdmissionWorkingDiagnosis(
			@RequestBody WorkingDiagnosis diagnosis,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(diagnosis.getAdmission().getId());
		if(!a.isPresent()) {
			throw new NotFoundException("Admission not found");
		}
		Optional<DiagnosisType> dt = diagnosisTypeRepository.findById(diagnosis.getDiagnosisType().getId());
		if(!dt.isPresent()) {
			throw new NotFoundException("Diagnosis type not found");
		}
		
		diagnosis.setAdmission(a.get());
		diagnosis.setDiagnosisType(dt.get());
		diagnosis.setPatient(a.get().getPatient());
		
		if(diagnosis.getId() == null) {
			diagnosis.setCreatedBy(userService.getUser(request).getId());
			diagnosis.setCreatedOn(dayService.getDay().getId());
			diagnosis.setCreatedAt(dayService.getTimeStamp());
		}
		
		
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_admission_working_diagnosis").toUriString());
		return ResponseEntity.created(uri).body(workingDiagnosisRepository.save(diagnosis));
	}
	
	@GetMapping("/patients/load_working_diagnosis") 
	public ResponseEntity<List<WorkingDiagnosisModel>> loasWorkingDiagnosises(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Consultation not found");
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_working_diagnosis").toUriString());
		
		List<WorkingDiagnosis> workingDiagnosises = workingDiagnosisRepository.findAllByConsultation(c.get());
		
		List<WorkingDiagnosisModel> models = new ArrayList<>();
		for(WorkingDiagnosis l : workingDiagnosises) {
			WorkingDiagnosisModel model= new WorkingDiagnosisModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setDiagnosisType(l.getDiagnosisType());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}			
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);		
	}
	
	@GetMapping("/patients/load_admission_working_diagnosis") 
	public ResponseEntity<List<WorkingDiagnosisModel>> loadAdmissionWorkingDiagnosises(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(id);
		if(!a.isPresent()) {
			throw new NotFoundException("Admission not found");
		}		
		
		List<WorkingDiagnosis> workingDiagnosises = workingDiagnosisRepository.findAllByAdmission(a.get());
		
		List<WorkingDiagnosisModel> models = new ArrayList<>();
		for(WorkingDiagnosis l : workingDiagnosises) {
			WorkingDiagnosisModel model= new WorkingDiagnosisModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setDiagnosisType(l.getDiagnosisType());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}			
			models.add(model);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_admission_working_diagnosis").toUriString());
		return ResponseEntity.created(uri).body(models);		
	}
	
	@PostMapping("/patients/save_final_diagnosis") 
	public ResponseEntity<FinalDiagnosis> saveFinalDiagnosis(
			@RequestBody FinalDiagnosis diagnosis,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(diagnosis.getConsultation().getId());
		if(!c.isPresent()) {
			throw new NotFoundException("Consultation not found");
		}
		Optional<DiagnosisType> dt = diagnosisTypeRepository.findById(diagnosis.getDiagnosisType().getId());
		if(!dt.isPresent()) {
			throw new NotFoundException("Diagnosis type not found");
		}
		if(finalDiagnosisRepository.existsByConsultationAndDiagnosisType(c.get(), dt.get())) {
			throw new InvalidOperationException("Duplicate Diagnosis Types is not allowed");
		}
		diagnosis.setConsultation(c.get());
		diagnosis.setDiagnosisType(dt.get());
		diagnosis.setPatient(c.get().getPatient());
		
		if(diagnosis.getId() == null) {
			diagnosis.setCreatedBy(userService.getUser(request).getId());
			diagnosis.setCreatedOn(dayService.getDay().getId());
			diagnosis.setCreatedAt(dayService.getTimeStamp());
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_final_diagnosis").toUriString());
		
		return ResponseEntity.created(uri).body(finalDiagnosisRepository.save(diagnosis));
	}
	
	@PostMapping("/patients/save_admission_final_diagnosis") 
	public ResponseEntity<FinalDiagnosis> saveAdmissionFinalDiagnosis(
			@RequestBody FinalDiagnosis diagnosis,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(diagnosis.getAdmission().getId());
		if(!a.isPresent()) {
			throw new NotFoundException("Admission not found");
		}
		Optional<DiagnosisType> dt = diagnosisTypeRepository.findById(diagnosis.getDiagnosisType().getId());
		if(!dt.isPresent()) {
			throw new NotFoundException("Diagnosis type not found");
		}
		
		diagnosis.setAdmission(a.get());
		diagnosis.setDiagnosisType(dt.get());
		diagnosis.setPatient(a.get().getPatient());
		
		if(diagnosis.getId() == null) {
			diagnosis.setCreatedBy(userService.getUser(request).getId());
			diagnosis.setCreatedOn(dayService.getDay().getId());
			diagnosis.setCreatedAt(dayService.getTimeStamp());
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_admission_final_diagnosis").toUriString());
		
		return ResponseEntity.created(uri).body(finalDiagnosisRepository.save(diagnosis));
	}
	
	@GetMapping("/patients/load_final_diagnosis") 
	public ResponseEntity<List<FinalDiagnosisModel>> loadFinalDiagnosises(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("Consultation not found");
		}		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_final_diagnosis").toUriString());
		
		List<FinalDiagnosis> finalDiagnosises = finalDiagnosisRepository.findAllByConsultation(c.get());
		
		List<FinalDiagnosisModel> models = new ArrayList<>();
		for(FinalDiagnosis l : finalDiagnosises) {
			FinalDiagnosisModel model= new FinalDiagnosisModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setDiagnosisType(l.getDiagnosisType());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}			
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);	
	}
	
	@GetMapping("/patients/load_admission_final_diagnosis") 
	public ResponseEntity<List<FinalDiagnosisModel>> loadAdmissionFinalDiagnosises(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Admission> a = admissionRepository.findById(id);
		if(!a.isPresent()) {
			throw new NotFoundException("Admission not found");
		}		
		
		List<FinalDiagnosis> finalDiagnosises = finalDiagnosisRepository.findAllByAdmission(a.get());
		
		List<FinalDiagnosisModel> models = new ArrayList<>();
		for(FinalDiagnosis l : finalDiagnosises) {
			FinalDiagnosisModel model= new FinalDiagnosisModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setDiagnosisType(l.getDiagnosisType());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}			
			models.add(model);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_admission_final_diagnosis").toUriString());
		return ResponseEntity.created(uri).body(models);	
	}
	
	@GetMapping("/patients/delete_working_diagnosis") 
	public void deleteWorkingDiagnosis(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){				
		workingDiagnosisRepository.deleteById(id);
	}
	
	@GetMapping("/patients/delete_final_diagnosis") 
	public void deleteFinalDiagnosis(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){				
		finalDiagnosisRepository.deleteById(id);
	}
	
	@PostMapping("/patients/save_lab_test")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<LabTest>saveLabTest(
			@RequestBody LabTest labTest,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id, 
			@RequestParam Long admission_id,
			HttpServletRequest request){
		Optional<Consultation> consultation_ = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nonConsultation_ = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		
		Optional<LabTestType> lt = labTestTypeRepository.findById(labTest.getLabTestType().getId());
		if(consultation_.isPresent()) {
			if(!consultation_.get().getPatient().getType().equals("OUTPATIENT")) {
				throw new InvalidOperationException("Patient is not an outpatient. Please reload patient to continue with this operation.");
			}
			if(labTestRepository.existsByConsultationAndLabTestType(consultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Lab Test Types is not allowed");
			}
		}else if(nonConsultation_.isPresent()) {
			if(!nonConsultation_.get().getPatient().getType().equals("OUTSIDER")) {
				throw new InvalidOperationException("Patient is not an outsider. Please reload patient to continue with this operation.");
			}
			if(labTestRepository.existsByNonConsultationAndLabTestType(nonConsultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Lab Test Types is not allowed");
			}
		}
		
		if(labTest.getId() == null) {
			labTest.setCreatedBy(userService.getUser(request).getId());
			labTest.setCreatedOn(dayService.getDay().getId());
			labTest.setCreatedAt(dayService.getTimeStamp());
			
			labTest.setOrderedBy(userService.getUser(request).getId());
			labTest.setOrderedOn(dayService.getDay().getId());
			labTest.setOrderedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_lab_test").toUriString());
		return ResponseEntity.created(uri).body(patientService.saveLabTest(labTest, consultation_, nonConsultation_, adm, request));
	}
	
	@PostMapping("/patients/save_radiology")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<Radiology>saveRadiology(
			@RequestBody Radiology radiology,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id, 
			@RequestParam Long admission_id,
			HttpServletRequest request){
		Optional<Consultation> consultation_ = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nonConsultation_ = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		
		Optional<RadiologyType> lt = radiologyTypeRepository.findById(radiology.getRadiologyType().getId());
		if(consultation_.isPresent()) {
			if(!consultation_.get().getPatient().getType().equals("OUTPATIENT")) {
				throw new InvalidOperationException("Patient is not an outpatient. Please reload patient to continue with this operation.");
			}
			if(radiologyRepository.existsByConsultationAndRadiologyType(consultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Radiology Types is not allowed");
			}
		}else if(nonConsultation_.isPresent()) {
			if(!nonConsultation_.get().getPatient().getType().equals("OUTSIDER")) {
				throw new InvalidOperationException("Patient is not an outsider. Please reload patient to continue with this operation.");
			}
			if(radiologyRepository.existsByNonConsultationAndRadiologyType(nonConsultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Radiology Types is not allowed");
			}
		}
		
		
		if(radiology.getId() == null) {
			radiology.setCreatedBy(userService.getUser(request).getId());
			radiology.setCreatedOn(dayService.getDay().getId());
			radiology.setCreatedAt(dayService.getTimeStamp());
			
			radiology.setOrderedby(userService.getUser(request).getId());
			radiology.setOrderedOn(dayService.getDay().getId());
			radiology.setOrderedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_radiology").toUriString());
		return ResponseEntity.created(uri).body(patientService.saveRadiology(radiology, consultation_, nonConsultation_, adm, request));
	}
	
	@PostMapping("/patients/radiologies/save_reason_for_rejection")
	public void saveRejectComment(
			@RequestBody Radiology radiology,
			HttpServletRequest request){
		Optional<Radiology> r = radiologyRepository.findById(radiology.getId());
		if(r.isEmpty()) {
			throw new NotFoundException("Radiology not found");
		}
		if(r.get().getStatus().equals("REJECTED")) {
			r.get().setRejectComment(radiology.getRejectComment());
			radiologyRepository.save(r.get());
		}else {
			throw new InvalidOperationException("Could not save. Only allowed for rejected tests");
		}
	}
	
	@PostMapping("/patients/lab_tests/save_reason_for_rejection")
	public void saveLabTestRejectComment(
			@RequestBody LabTest test,
			HttpServletRequest request){
		Optional<LabTest> r = labTestRepository.findById(test.getId());
		if(r.isEmpty()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(r.get().getStatus().equals("REJECTED")) {
			r.get().setRejectComment(test.getRejectComment());
			labTestRepository.save(r.get());
		}else {
			throw new InvalidOperationException("Could not save. Only allowed for rejected tests");
		}
	}
	
	@PostMapping("/patients/save_procedure")
	public ResponseEntity<Procedure>saveProcedure(
			@RequestBody Procedure procedure,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			HttpServletRequest request){
		Optional<Consultation> consultation_ = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nonConsultation_ = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		
		Optional<ProcedureType> lt = procedureTypeRepository.findById(procedure.getProcedureType().getId());
		
		if(consultation_.isPresent()) {
			if(!consultation_.get().getPatient().getType().equals("OUTPATIENT")) {
				throw new InvalidOperationException("Patient is not an outpatient. Please reload patient to continue with this operation.");
			}
			if(procedureRepository.existsByConsultationAndProcedureType(consultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Procedure Types is not allowed");
			}
		}else if(nonConsultation_.isPresent()) {
			if(!nonConsultation_.get().getPatient().getType().equals("OUTSIDER")) {
				throw new InvalidOperationException("Patient is not an outsider. Please reload patient to continue with this operation.");
			}
			if(procedureRepository.existsByNonConsultationAndProcedureType(nonConsultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate Procedure Types is not allowed");
			}
		}
		
		if(procedure.getId() == null) {
			procedure.setCreatedBy(userService.getUser(request).getId());
			procedure.setCreatedOn(dayService.getDay().getId());
			procedure.setCreatedAt(dayService.getTimeStamp());
			
			procedure.setOrderedby(userService.getUser(request).getId());
			procedure.setOrderedOn(dayService.getDay().getId());
			procedure.setOrderedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_procedure").toUriString());
		return ResponseEntity.created(uri).body(patientService.saveProcedure(procedure, consultation_, nonConsultation_, adm, request));
	}
	
	@PostMapping("/patients/save_prescription")
	public ResponseEntity<Prescription>savePrescription(
			@RequestBody Prescription prescription,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id, 
			@RequestParam Long admission_id,
			HttpServletRequest request){
		Optional<Consultation> consultation_ = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nonConsultation_ = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		
		Optional<Medicine> lt = medicineRepository.findById(prescription.getMedicine().getId());
		if(consultation_.isPresent()) {
			if(!consultation_.get().getPatient().getType().equals("OUTPATIENT")) {
				throw new InvalidOperationException("Patient is not an outpatient. Please reload patient to continue with this operation.");
			}
			if(prescriptionRepository.existsByConsultationAndMedicine(consultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate drug is not allowed. Consider editing qty");
			}
		}else if(nonConsultation_.isPresent()) {
			if(!nonConsultation_.get().getPatient().getType().equals("OUTSIDER")) {
				throw new InvalidOperationException("Patient is not an outsider. Please reload patient to continue with this operation.");
			}
			if(prescriptionRepository.existsByConsultationAndMedicine(consultation_.get(), lt.get())) {
				throw new InvalidOperationException("Duplicate drug is not allowed. Consider editing qty");
			}
		}
		
		if(prescription.getId() == null) {
			prescription.setCreatedBy(userService.getUser(request).getId());
			prescription.setCreatedOn(dayService.getDay().getId());
			prescription.setCreatedAt(dayService.getTimeStamp());
			
			prescription.setOrderedby(userService.getUser(request).getId());
			prescription.setOrderedOn(dayService.getDay().getId());
			prescription.setOrderedAt(dayService.getTimeStamp());
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_prescription").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePrescription(prescription, consultation_, nonConsultation_, adm, request));
	}
	
	@PostMapping("/patients/save_patient_dressing_chart")
	public ResponseEntity<PatientDressingChart>savePatientDressingChart(
			@RequestBody PatientDressingChart chart,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_dressing_chart").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientDressingChart(chart, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_consumable_chart")
	public ResponseEntity<PatientConsumableChart>savePatientConssumableChart(
			@RequestBody PatientConsumableChart chart,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_consumable").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientConsumableChart(chart, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_observation_chart")
	public ResponseEntity<PatientObservationChart>savePatientObservationChart(
			@RequestBody PatientObservationChart chart,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_observation_chart").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientObservationChart(chart, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_prescription_chart")
	public ResponseEntity<PatientPrescriptionChart>savePatientPrescriptionChart(
			@RequestBody PatientPrescriptionChart chart,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_prescription_chart").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientPrescriptionChart(chart, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_nursing_chart")
	public ResponseEntity<PatientNursingChart>savePatientNursingChart(
			@RequestBody PatientNursingChart chart,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_nursing_chart").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientNursingChart(chart, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_nursing_progress_note")
	public ResponseEntity<PatientNursingProgressNote>savePatientNursingProgressNote(
			@RequestBody PatientNursingProgressNote note,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_nursing_progress_note").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientNursingProgressNote(note, c, nc, adm, n, request));
	}
	
	@PostMapping("/patients/save_patient_nursing_care_plan")
	public ResponseEntity<PatientNursingCarePlan>savePatientNursingCarePlan(
			@RequestBody PatientNursingCarePlan plan,
			@RequestParam Long consultation_id, 
			@RequestParam Long non_consultation_id,
			@RequestParam Long admission_id,
			@RequestParam Long nurse_id,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultation_id);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(non_consultation_id);
		Optional<Admission> adm = admissionRepository.findById(admission_id);
		Optional<Nurse> n = nurseRepository.findById(nurse_id);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_patient_nursing_care_plan").toUriString());
		return ResponseEntity.created(uri).body(patientService.savePatientNursingCarePlan(plan, c, nc, adm, n, request));
	}
	
	@GetMapping("/patients/load_lab_tests") 
	public ResponseEntity<List<LabTestModel>> loadLabTests(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_lab_test").toUriString());
		Patient patient = new Patient();
		List<LabTest> labTests = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			labTests = labTestRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){		
			patient = nc.get().getPatient();
			labTests = labTestRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){
			patient = adm.get().getPatient();
			labTests = labTestRepository.findAllByAdmission(adm.get());
		}
		List<LabTestModel> models = new ArrayList<>();
		for(LabTest l : labTests) {
			LabTestModel model= new LabTestModel();
			model.setId(l.getId());
			model.setResult(l.getResult());
			model.setReport(l.getReport());
			model.setDescription(l.getDescription());
			model.setLabTestType(l.getLabTestType());
			model.setPatientBill(l.getPatientBill());
			model.setRange(l.getRange());
			model.setLevel(l.getLevel());
			model.setUnit(l.getUnit());
			model.setStatus(l.getStatus());
			
			
			List<LabTestAttachmentModel> labTestAttachmentModels = new ArrayList<>();
			for(LabTestAttachment labTestAttachment : l.getLabTestAttachments()) {
				LabTestAttachmentModel attachmentModel = new LabTestAttachmentModel();
				attachmentModel.setId(labTestAttachment.getId());
				attachmentModel.setFileName(labTestAttachment.getFileName());
				attachmentModel.setName(labTestAttachment.getName());
				attachmentModel.setLabTest(l);
				
				labTestAttachmentModels.add(attachmentModel);
			}
			
			model.setLabTestAttachments(labTestAttachmentModels);
			

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedBy()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedBy()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedBy()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldBy()).getNickname());
			}else {
				model.setHeld("");
			}
			if(l.getCollectedAt() != null) {
				model.setCollected(l.getCollectedAt().toString()+" | "+userService.getUserById(l.getCollectedBy()).getNickname());
			}else {
				model.setCollected("");
			}
			
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedBy()).getNickname());
			}else {
				model.setVerified("");
			}
			
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/load_radiologies") 
	public ResponseEntity<List<RadiologyModel>> loadRadiologies(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_radiologies").toUriString());
		Patient patient = new Patient();
		List<Radiology> radiologies = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			radiologies = radiologyRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			radiologies = radiologyRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			radiologies = radiologyRepository.findAllByAdmission(adm.get());
		}
		List<RadiologyModel> models = new ArrayList<>();
		for(Radiology r : radiologies) {
			RadiologyModel model= new RadiologyModel();
			model.setId(r.getId());
			model.setResult(r.getResult());
			model.setReport(r.getReport());
			model.setRadiologyType(r.getRadiologyType());
			model.setDescription(r.getDescription());
			model.setDiagnosisType(r.getDiagnosisType());			
			model.setPatientBill(r.getPatientBill());
			model.setAttachment(r.getAttachment());
			
			
			List<RadiologyAttachmentModel> radiologyAttachmentModels = new ArrayList<>();
			for(RadiologyAttachment radiologyAttachment : r.getRadiologyAttachments()) {
				RadiologyAttachmentModel attachmentModel = new RadiologyAttachmentModel();
				attachmentModel.setId(radiologyAttachment.getId());
				attachmentModel.setFileName(radiologyAttachment.getFileName());
				attachmentModel.setName(radiologyAttachment.getName());
				attachmentModel.setRadiology(r);
				
				radiologyAttachmentModels.add(attachmentModel);
			}
			
			model.setRadiologyAttachments(radiologyAttachmentModels);
			
			
			if(r.getCreatedAt() != null) {
				model.setCreated(r.getCreatedAt().toString()+" | "+userService.getUserById(r.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(r.getRejectedAt() != null) {
				model.setRejected(r.getRejectedAt().toString()+" | "+userService.getUserById(r.getRejectedby()).getNickname());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(r.getRejectComment());			
			if(r.getAcceptedAt() != null) {
				model.setAccepted(r.getAcceptedAt().toString()+" | "+userService.getUserById(r.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(r.getOrderedAt() != null) {
				model.setOrdered(r.getOrderedAt().toString()+" | "+userService.getUserById(r.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(r.getVerifiedAt() != null) {
				model.setVerified(r.getVerifiedAt().toString()+" | "+userService.getUserById(r.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			model.setStatus(r.getStatus());
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/load_procedures") 
	public ResponseEntity<List<ProcedureModel>> loadProcedures(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_procedures").toUriString());
		Patient patient = new Patient();
		List<Procedure> procedures = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			procedures = procedureRepository.findAllByConsultation(c.get());
			
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			procedures = procedureRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			procedures = procedureRepository.findAllByAdmission(adm.get());
		}
		List<ProcedureModel> models = new ArrayList<>();
		for(Procedure l : procedures) {
			ProcedureModel model= new ProcedureModel();
			model.setId(l.getId());
			model.setProcedureType(l.getProcedureType());
			model.setPatientBill(l.getPatientBill());
			model.setDiagnosisType(l.getDiagnosisType());
			model.setType(l.getType());
			model.setTime(l.getTime());
			model.setDate(l.getDate());
			model.setHours(l.getHours());
			model.setMinutes(l.getMinutes());
			model.setStatus(l.getStatus());
			model.setNote(l.getNote());
			model.setTheatre(l.getTheatre());

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedby()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldby()).getNickname());
			}else {
				model.setHeld("");
			}		
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/load_prescriptions")
	public ResponseEntity<List<PrescriptionModel>> loadPrescriptions(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_prescriptions").toUriString());
		Patient patient = new Patient();
		List<Prescription> prescriptions = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			prescriptions = prescriptionRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			prescriptions = prescriptionRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			prescriptions = prescriptionRepository.findAllByAdmission(adm.get());
		}
		List<PrescriptionModel> models = new ArrayList<>();
		for(Prescription l : prescriptions) {
			PrescriptionModel model= new PrescriptionModel();
			model.setId(l.getId());
			model.setMedicine(l.getMedicine());
			model.setDosage(l.getDosage());
			model.setFrequency(l.getFrequency());			
			model.setRoute(l.getRoute());
			model.setDays(l.getDays());
			model.setQty(l.getQty());
			model.setIssued(l.getIssued());
			model.setBalance(l.getBalance());
			model.setPatientBill(l.getPatientBill());
			model.setStatus(l.getStatus());

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedby()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldby()).getNickname());
			}else {
				model.setHeld("");
			}		
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			
			if(l.getApprovedAt() != null) {
				model.setApproved(l.getApprovedAt().toString()+" | "+userService.getUserById(l.getApprovedBy()).getNickname());
			}else {
				model.setApproved("");
			}
			
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/dressing_charts") 
	public ResponseEntity<List<PatientDressingChartModel>> loadPatientDressingCharts(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/dressing_charts").toUriString());
		Patient patient = new Patient();
		List<PatientDressingChart> patientDressingCharts = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientDressingCharts = patientDressingChartRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientDressingCharts = patientDressingChartRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientDressingCharts = patientDressingChartRepository.findAllByAdmission(adm.get());
		}
		List<PatientDressingChartModel> models = new ArrayList<>();
		for(PatientDressingChart l : patientDressingCharts) {
			PatientDressingChartModel model= new PatientDressingChartModel();
			model.setId(l.getId());
			model.setProcedureType(l.getProcedureType());
			model.setPatientBill(l.getPatientBill());
			model.setClinician(l.getClinician());
			model.setNurse(l.getNurse());
			model.setQty(l.getQty());			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/consumable_charts") 
	public ResponseEntity<List<PatientConsumableChartModel>> loadPatientConsumableCharts(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/consumable_charts").toUriString());
		Patient patient = new Patient();
		List<PatientConsumableChart> patientConsumableCharts = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientConsumableCharts = patientConsumableChartRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientConsumableCharts = patientConsumableChartRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientConsumableCharts = patientConsumableChartRepository.findAllByAdmission(adm.get());
		}
		List<PatientConsumableChartModel> models = new ArrayList<>();
		for(PatientConsumableChart l : patientConsumableCharts) {
			PatientConsumableChartModel model= new PatientConsumableChartModel();
			model.setId(l.getId());
			model.setMedicine(l.getMedicine());
			model.setPatientBill(l.getPatientBill());
			model.setClinician(l.getClinician());
			model.setNurse(l.getNurse());
			model.setQty(l.getQty());	
			model.setStatus(l.getStatus());
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/observation_charts") 
	public ResponseEntity<List<PatientObservationChartModel>> loadPatientObservationCharts(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/observation_charts").toUriString());
		Patient patient = new Patient();
		List<PatientObservationChart> patientObservationCharts = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientObservationCharts = patientObservationChartRepository.findAllByConsultation(c.get());
			
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientObservationCharts = patientObservationChartRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientObservationCharts = patientObservationChartRepository.findAllByAdmission(adm.get());
		}
		List<PatientObservationChartModel> models = new ArrayList<>();
		for(PatientObservationChart l : patientObservationCharts) {
			PatientObservationChartModel model= new PatientObservationChartModel();
			model.setId(l.getId());
			model.setClinician(l.getClinician());
			model.setNurse(l.getNurse());
			model.setBloodPressure(l.getBloodPressure());
			model.setMeanArterialPressure(l.getMeanArterialPressure());
			model.setPressure(l.getPressure());
			model.setTemperature(l.getTemperature());
			model.setRespiratoryRate(l.getRespiratoryRate());
			model.setSaturationOxygen(l.getSaturationOxygen());
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/prescription_charts") /////not to be used!!!
	public ResponseEntity<List<PatientPrescriptionChartModel>> loadPatientPrescriptionCharts(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/prescription_charts").toUriString());
		Patient patient = new Patient();
		List<PatientPrescriptionChart> patientPrescriptionCharts = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientPrescriptionCharts = patientPrescriptionChartRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientPrescriptionCharts = patientPrescriptionChartRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientPrescriptionCharts = patientPrescriptionChartRepository.findAllByAdmission(adm.get());
		}
		List<PatientPrescriptionChartModel> models = new ArrayList<>();
		for(PatientPrescriptionChart l : patientPrescriptionCharts) {
			PatientPrescriptionChartModel model= new PatientPrescriptionChartModel();
			model.setId(l.getId());
			model.setClinician(l.getClinician());
			model.setNurse(l.getNurse());
			model.setDosage(l.getDosage());
			model.setPrescription(l.getPrescription());
			model.setOutput(l.getOutput());
			model.setRemark(l.getRemark());			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/nursing_charts") 
	public ResponseEntity<List<PatientNursingChartModel>> loadPatientNursingCharts(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/nursing_charts").toUriString());
		Patient patient = new Patient();
		List<PatientNursingChart> patientNursingCharts = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientNursingCharts = patientNursingChartRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientNursingCharts = patientNursingChartRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientNursingCharts = patientNursingChartRepository.findAllByAdmission(adm.get());
		}
		List<PatientNursingChartModel> models = new ArrayList<>();
		for(PatientNursingChart l : patientNursingCharts) {
			PatientNursingChartModel model= new PatientNursingChartModel();
			model.setId(l.getId());
			model.setNurse(l.getNurse());
			model.setFeeding(l.getFeeding());
			model.setChangingPosition(l.getChangingPosition());
			model.setBedBathing(l.getBedBathing());
			model.setRandomBloodSugar(l.getRandomBloodSugar());
			model.setFullBloodSugar(l.getFullBloodSugar());
			model.setDrainageOutput(l.getDrainageOutput());
			model.setFluidIntake(l.getFluidIntake());
			model.setUrineOutput(l.getUrineOutput());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/nursing_progress_notes") 
	public ResponseEntity<List<PatientNursingProgressNoteModel>> loadPatientNursingProgressNotes(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/nursing_progress_notes").toUriString());
		Patient patient = new Patient();
		List<PatientNursingProgressNote> patientNursingProgressNotes = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientNursingProgressNotes = patientNursingProgressNoteRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientNursingProgressNotes = patientNursingProgressNoteRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientNursingProgressNotes = patientNursingProgressNoteRepository.findAllByAdmission(adm.get());
		}
		List<PatientNursingProgressNoteModel> models = new ArrayList<>();
		for(PatientNursingProgressNote l : patientNursingProgressNotes) {
			PatientNursingProgressNoteModel model= new PatientNursingProgressNoteModel();
			model.setId(l.getId());
			model.setNurse(l.getNurse());
			model.setNote(l.getNote());			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/nursing_care_plans") 
	public ResponseEntity<List<PatientNursingCarePlanModel>> loadPatientNursingCarePlans(
			@RequestParam(name = "consultation_id") Long consultationId,
			@RequestParam(name = "non_consultation_id") Long nonConsultationId,
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		Optional<NonConsultation> nc = nonConsultationRepository.findById(nonConsultationId);
		Optional<Admission> adm = admissionRepository.findById(admissionId);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/nursing_care_plans").toUriString());
		Patient patient = new Patient();
		List<PatientNursingCarePlan> patientNursingCarePlans = new ArrayList<>();
		if(c.isPresent()) {
			patient = c.get().getPatient();
			patientNursingCarePlans = patientNursingCarePlanRepository.findAllByConsultation(c.get());
		}else if(nc.isPresent()){	
			patient = nc.get().getPatient();
			patientNursingCarePlans = patientNursingCarePlanRepository.findAllByNonConsultation(nc.get());
		}else if(adm.isPresent()){	
			patient = adm.get().getPatient();
			patientNursingCarePlans = patientNursingCarePlanRepository.findAllByAdmission(adm.get());
		}
		List<PatientNursingCarePlanModel> models = new ArrayList<>();
		for(PatientNursingCarePlan l : patientNursingCarePlans) {
			PatientNursingCarePlanModel model= new PatientNursingCarePlanModel();
			model.setId(l.getId());
			model.setNurse(l.getNurse());
			model.setNursingDiagnosis(l.getNursingDiagnosis());
			model.setExpectedOutcome(l.getExpectedOutcome());
			model.setImplementation(l.getImplementation());
			model.setEvaluation(l.getEvaluation());
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/prescription_charts_by_id") 
	public ResponseEntity<List<PatientPrescriptionChartModel>> loadPatientPrescriptionChartsByPrescriptionId(
			@RequestParam(name = "prescription_id") Long prescriptionId,
			HttpServletRequest request){
		Optional<Prescription> pre = prescriptionRepository.findById(prescriptionId);
		if(pre.isEmpty()) {
			throw new NotFoundException("Prescription not found");
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/prescription_charts").toUriString());		
		List<PatientPrescriptionChart> patientPrescriptionCharts = new ArrayList<>();
		patientPrescriptionCharts = patientPrescriptionChartRepository.findAllByPrescription(pre.get());
		
		List<PatientPrescriptionChartModel> models = new ArrayList<>();
		for(PatientPrescriptionChart l : patientPrescriptionCharts) {
			PatientPrescriptionChartModel model= new PatientPrescriptionChartModel();
			model.setId(l.getId());
			model.setNurse(l.getNurse());
			model.setDosage(l.getDosage());
			model.setOutput(l.getOutput());
			model.setRemark(l.getRemark());
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}		
			models.add(model);
		}
		return ResponseEntity.created(uri).body(models);
	}
	
	@PostMapping("/patients/delete_lab_test")
	public ResponseEntity<Boolean>deleteLabTest(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(id);
		if(!t.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not delete, only a PENDING lab test can be deleted");
		}
		LabTest labTest = t.get();
		
		PatientBill patientBill = patientBillRepository.findById(t.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled lab test");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first
			//throw new InvalidOperationException("Can not delete a paid lab test, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		labTestRepository.delete(labTest);
		patientBillRepository.delete(patientBill);		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_lab_test").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_dressing_chart")
	public ResponseEntity<Boolean>deleteDressingChart(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<PatientDressingChart> t = patientDressingChartRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}
		
		
		PatientBill patientBill = patientBillRepository.findById(t.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled lab test");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first
			//throw new InvalidOperationException("Can not delete a paid lab test, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		patientDressingChartRepository.delete(t.get());
		patientBillRepository.delete(patientBill);		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_dressing_chart").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_consumable_chart")
	public ResponseEntity<Boolean>deleteConsumableChart(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<PatientConsumableChart> t = patientConsumableChartRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		if(t.get().getStatus() != null) {
			if(t.get().getStatus().equals("GIVEN")) {
				throw new InvalidOperationException("Could not delete. Consumable already given to patient");
			}
		}
		
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}
		
		PatientBill patientBill = patientBillRepository.findById(t.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled lab test");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first
			//throw new InvalidOperationException("Can not delete a paid lab test, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		patientConsumableChartRepository.delete(t.get());
		patientBillRepository.delete(patientBill);		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_dressing_chart").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	
	@PostMapping("/patients/delete_observation_chart")
	public ResponseEntity<Boolean>deleteObservationChart(
			@RequestParam(name = "id") Long id, 
			HttpServletRequest request){
		Optional<PatientObservationChart> t = patientObservationChartRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}		
		patientObservationChartRepository.delete(t.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_observation_chart").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_prescription_chart")
	public ResponseEntity<Boolean>deletePrescriptionChart(
			@RequestParam(name = "id") Long id, 
			HttpServletRequest request){
		Optional<PatientPrescriptionChart> t = patientPrescriptionChartRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}		
		patientPrescriptionChartRepository.delete(t.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_prescription_chart").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_nursing_chart")
	public ResponseEntity<Boolean>deleteNursingChart(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<PatientNursingChart> t = patientNursingChartRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}		
		patientNursingChartRepository.delete(t.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_nursing_chart").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_nursing_progress_note")
	public ResponseEntity<Boolean>deleteNursingProgressNote(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<PatientNursingProgressNote> t = patientNursingProgressNoteRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}		
		patientNursingProgressNoteRepository.delete(t.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_nursing_progress_note").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_nursing_care_plan")
	public ResponseEntity<Boolean>deleteNursingCarePlan(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<PatientNursingCarePlan> t = patientNursingCarePlanRepository.findById(id);
		if(t.isEmpty()) {
			throw new NotFoundException("Record not found");
		}
		long difference = ChronoUnit.HOURS.between(t.get().getCreatedAt(), dayService.getTimeStamp());
		if(difference >= 24) {
			throw new InvalidOperationException("Could not delete record. only records not exceeding 24 hours can be deleted");
		}		
		patientNursingCarePlanRepository.delete(t.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_nursing_care_plan").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	
	
	@PostMapping("/patients/radiologies/add_report")
	public void addReport(
			@RequestBody Radiology radiology,
			HttpServletRequest request){
		Optional<Radiology> r = radiologyRepository.findById(radiology.getId());
		if(r.isEmpty()) {
			throw new NotFoundException("Radiology not found");
		}
		if(r.get().getPatientBill().getStatus().equals("PAID") || r.get().getPatientBill().getStatus().equals("COVERED") || r.get().getPatientBill().getStatus().equals("VERIFIED")) {
			r.get().setReport(radiology.getReport());
			radiologyRepository.save(r.get());
		}else {
			throw new InvalidOperationException("Could not add report. Payment not verified");
		}		
	}
	
	@PostMapping("/patients/issue_medicine")
	public boolean issueMedicine(
			@RequestBody List<Prescription> prescriptions,
			@RequestParam(name = "pharmacy_id") Long pharmacyId, 
			HttpServletRequest request) {
		
		boolean success = true;
		
		Optional<Pharmacy> pharmacy_ = pharmacyRepository.findById(pharmacyId);
		if(pharmacy_.isEmpty()) {
			throw new NotFoundException("Pharmacy not found");
		}
		
		for(Prescription prescription : prescriptions) {
			Optional<Prescription> prescription_ = prescriptionRepository.findById(prescription.getId());
			if(prescription_.isEmpty()) {
				throw new NotFoundException("Prescription with id "+prescription.getId().toString()+" not found in database");
			}
			if(!prescription_.get().getStatus().equals("NOT-GIVEN")) {
				success = false;
				throw new InvalidOperationException("Could not issue medicine. Prescription with id "+prescription.getId().toString()+" is not a pending prescription");
			}
			if(prescription_.get().getBalance() > prescription.getIssued() || prescription.getIssued() <= 0) {
				throw new InvalidOperationException("Invalid issue value in "+prescription.getMedicine().getName());
			}
			if(prescription.getIssued() != prescription_.get().getQty()) {
				throw new InvalidOperationException("You can only issue the prescribed qty");
			}
			prescription_.get().setIssued(prescription.getIssued());
			prescription_.get().setBalance(prescription_.get().getBalance() - prescription.getIssued());
						
			prescription_.get().setStatus("GIVEN");
			prescription_.get().setIssuePharmacy(pharmacy_.get());
			
			prescription_.get().setApprovedBy(userService.getUser(request).getId());
			prescription_.get().setApprovedOn(dayService.getDay().getId());
			prescription_.get().setApprovedAt(dayService.getTimeStamp());
			
			prescriptionRepository.save(prescription_.get());
			
			PharmacyMedicine pharmacyMedicine = pharmacyMedicineRepository.findByPharmacyAndMedicine(pharmacy_.get(), prescription_.get().getMedicine()).get();
			
			Pharmacy pharmacy = pharmacyMedicine.getPharmacy();
			Medicine medicine = pharmacyMedicine.getMedicine();
			if(pharmacyMedicine.getStock() < prescription_.get().getQty()) {
				throw new InvalidOperationException("Can not issue, insufficient stock in : "+prescription_.get().getMedicine().getName());
			}
			
			double newStock = pharmacyMedicine.getStock() - prescription_.get().getQty();
			
			pharmacyMedicine.setStock(newStock);
			pharmacyMedicineRepository.save(pharmacyMedicine);
			
			PharmacyStockCard pharmacyStockCard = new PharmacyStockCard();
			pharmacyStockCard.setMedicine(medicine);
			pharmacyStockCard.setPharmacy(pharmacy);
			pharmacyStockCard.setQtyIn(0);
			pharmacyStockCard.setQtyOut(prescription.getIssued());
			pharmacyStockCard.setBalance(newStock);
			pharmacyStockCard.setReference("Issued in prescription: id " + prescription_.get().getId().toString());
			
			pharmacyStockCard.setCreatedBy(userService.getUserId(request));
			pharmacyStockCard.setCreatedOn(dayService.getDayId());
			pharmacyStockCard.setCreatedAt(dayService.getTimeStamp());
			
			pharmacyStockCardRepository.save(pharmacyStockCard);
			
			
			
			
			/*for(StoreToPharmacyBatch batch : detail.getStoreToPharmacyBatches()) {
				
				PharmacyMedicineBatch pharmacyMedicineBatch = new PharmacyMedicineBatch();
				pharmacyMedicineBatch.setNo(batch.getNo());
				pharmacyMedicineBatch.setMedicine(batch.getStoreToPharmacyRNDetail().getMedicine());
				pharmacyMedicineBatch.setManufacturedDate(batch.getManufacturedDate());
				pharmacyMedicineBatch.setExpiryDate(batch.getExpiryDate());
				pharmacyMedicineBatch.setQty(batch.getPharmacySKUQty());
				pharmacyMedicineBatch.setPharmacy(receiveNote.getPharmacy());
				
				pharmacyMedicineBatchRepository.save(pharmacyMedicineBatch);
			}*/
			
			
			
			
			
			List<PharmacyMedicineBatch> pharmacyMedicineBatches = pharmacyMedicineBatchRepository.findAllByPharmacyAndMedicineAndQtyGreaterThan(pharmacy, medicine, 0);
			
			deductBatch(pharmacyMedicineBatches, prescription_.get(), prescription.getIssued());
			
		}
		
		return success;
	}
	
	
	void deductBatch(List<PharmacyMedicineBatch> pharmacyMedicineBatches, Prescription prescription, double qty){
		
		try {
			PharmacyMedicineBatch batch = getEarlierBatch(pharmacyMedicineBatches);
			pharmacyMedicineBatches.remove(batch);
			if(qty <= batch.getQty()) {
				double toDeduct = batch.getQty() - qty;
				batch.setQty(toDeduct);
				pharmacyMedicineBatchRepository.save(batch);
				
				PrescriptionBatch prescriptionBatch = new PrescriptionBatch();
				prescriptionBatch.setNo(batch.getNo());
				prescriptionBatch.setManufacturedDate(batch.getManufacturedDate());
				prescriptionBatch.setExpiryDate(batch.getExpiryDate());
				prescriptionBatch.setQty(qty);
				prescriptionBatch.setPrescription(prescription);
				
				prescriptionBatchRepository.save(prescriptionBatch);
				
			}else if(qty > batch.getQty()) {
				double newToDeduct = qty - batch.getQty();
				double qtyIssued = batch.getQty();
				batch.setQty(0);
				pharmacyMedicineBatchRepository.save(batch);
				pharmacyMedicineBatches.remove(batch);
				deductBatch(pharmacyMedicineBatches, prescription, newToDeduct);
				
				PrescriptionBatch prescriptionBatch = new PrescriptionBatch();
				prescriptionBatch.setNo(batch.getNo());
				prescriptionBatch.setManufacturedDate(batch.getManufacturedDate());
				prescriptionBatch.setExpiryDate(batch.getExpiryDate());
				prescriptionBatch.setQty(qtyIssued);
				
				prescriptionBatchRepository.save(prescriptionBatch);
			}	
		}catch(Exception e) {
			//do nothing
		}
		
			
	}
	
	PharmacyMedicineBatch getEarlierBatch(List<PharmacyMedicineBatch> pharmacyMedicineBatches) {
		List<PharmacyMedicineBatch> newBatchList = new ArrayList<>();
		boolean hasExpiry = false;
		PharmacyMedicineBatch selectedMedicineBatch = null;
		
		for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
			if(pharmacyMedicineBatch.getExpiryDate() != null) {
				hasExpiry = true;
				newBatchList.add(pharmacyMedicineBatch);
			}
		}
		if(hasExpiry == true) {
			pharmacyMedicineBatches = newBatchList;
			LocalDate expiryDate = null;
			for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
				if(expiryDate == null) {
					expiryDate = pharmacyMedicineBatch.getExpiryDate();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}else if(expiryDate.isAfter(pharmacyMedicineBatch.getExpiryDate())) {
					expiryDate = pharmacyMedicineBatch.getExpiryDate();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}
			}
		}
		
		if(hasExpiry == false) {
			Long id = null;
			for(PharmacyMedicineBatch pharmacyMedicineBatch : pharmacyMedicineBatches) {
				if(id == null) {
					id = pharmacyMedicineBatch.getId();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}else if(id > pharmacyMedicineBatch.getId()) {
					id = pharmacyMedicineBatch.getId();
					selectedMedicineBatch = pharmacyMedicineBatch;
				}
			}
		}
		return selectedMedicineBatch;
	}
	
	
	
	
	@PostMapping("/patients/lab_tests/add_report")
	public void addLabTestReport(
			@RequestBody LabTest labTest,
			HttpServletRequest request){
		Optional<LabTest> r = labTestRepository.findById(labTest.getId());
		if(r.isEmpty()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(r.get().getPatientBill().getStatus().equals("PAID") || r.get().getPatientBill().getStatus().equals("COVERED") || r.get().getPatientBill().getStatus().equals("VERIFIED")) {
			r.get().setReport(labTest.getReport());
			labTestRepository.save(r.get());
		}else {
			throw new InvalidOperationException("Could not add report. Payment not verified");
		}		
	}
	
	@PostMapping("/patients/procedures/add_note")
	public void addProcedureNote(
			@RequestBody Procedure procedure,
			HttpServletRequest request){
		Optional<Procedure> r = procedureRepository.findById(procedure.getId());
		if(r.isEmpty()) {
			throw new NotFoundException("Procedure not found");
		}
		if(procedure.getNote().equals("")){
			throw new InvalidEntryException("Could not add empty procedure note");
		}
		if(r.get().getPatientBill().getStatus().equals("PAID") || r.get().getPatientBill().getStatus().equals("COVERED") || r.get().getPatientBill().getStatus().equals("VERIFIED")) {
			r.get().setNote(procedure.getNote());
			r.get().setStatus("VERIFIED");
			
			procedureRepository.save(r.get());
		}else {
			throw new InvalidOperationException("Could not add procedure note. Payment not verified");
		}		
	}
	
	@PostMapping("/patients/delete_radiology")
	public ResponseEntity<Boolean>deleteRadiology(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<Radiology> r = radiologyRepository.findById(id);
		if(!r.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not delete, only a PENDING radiology can be deleted");
		}
		Radiology radiology = r.get();
		
		PatientBill patientBill = patientBillRepository.findById(r.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled radiology");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first, in the mean time
			//throw new InvalidOperationException("Can not delete a paid radiology, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		radiologyRepository.delete(radiology);
		patientBillRepository.delete(patientBill);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_radiology").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_procedure")
	public ResponseEntity<Boolean>deleteProcedure(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<Procedure> pr = procedureRepository.findById(id);
		if(!pr.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not delete, only a PENDING procedure can be deleted");
		}
		Procedure procedure = pr.get();
		
		PatientBill patientBill = patientBillRepository.findById(pr.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("RECEIVED")) {
			//disable deleting a paid procedure first, in the mean time
			//throw new InvalidOperationException("Can not delete a paid procedure, please contact system administrator");
			
			/*PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(patientBill.getPatient());
			patientCreditNote.setReference("Procedure canceled");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo("NA");
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
			patientCreditNote.setNo(patientCreditNote.getId().toString());
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);*/
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled procedure");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first, in the mean time
			//throw new InvalidOperationException("Can not delete a paid procedure, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		
		procedureRepository.delete(procedure);
		patientBillRepository.delete(patientBill);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_procedure").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@PostMapping("/patients/delete_prescription")
	public ResponseEntity<Boolean>deletePrescription(
			@RequestParam Long id, 
			HttpServletRequest request){
		Optional<Prescription> pr = prescriptionRepository.findById(id);
		if(!(pr.get().getStatus().equals("PENDING") || pr.get().getStatus().equals("NOT-GIVEN"))) {
			throw new InvalidOperationException("Could not delete, only a PENDING prescription can be deleted");
		}
		Prescription prescription = pr.get();
		
		PatientBill patientBill = patientBillRepository.findById(pr.get().getPatientBill().getId()).get();
		
		Optional<PatientPaymentDetail> pd = patientPaymentDetailRepository.findByPatientBill(patientBill);
		if(pd.isPresent() && pd.get().getStatus().equals("GIVEN")) {
			//disable deleting a paid test first, in the mean time
			//throw new InvalidOperationException("Can not delete a paid prescription, please contact system administrator");
			
			/*PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(patientBill.getPatient());
			patientCreditNote.setReference("Prescription canceled");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo("NA");
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
			patientCreditNote.setNo(patientCreditNote.getId().toString());
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);*/
			
			PatientCreditNote patientCreditNote = new PatientCreditNote();
			patientCreditNote.setAmount(pd.get().getPatientBill().getAmount());
			patientCreditNote.setPatient(pd.get().getPatientBill().getPatient());
			patientCreditNote.setReference("Canceled prescription");
			patientCreditNote.setStatus("PENDING");
			patientCreditNote.setNo(patientCreditNoteService.requestPatientCreditNoteNo().getNo());
			
			patientCreditNote.setCreatedBy(userService.getUserId(request));
			patientCreditNote.setCreatedOn(dayService.getDayId());
			patientCreditNote.setCreatedAt(dayService.getTimeStamp());
			
			patientCreditNote = patientCreditNoteRepository.save(patientCreditNote);
		}
		Optional<PatientInvoiceDetail> i = patientInvoiceDetailRepository.findByPatientBill(patientBill);
		/**
		 * If there is a patientInvoice detail associated with this patientBill, delete it
		 */
		if(i.isPresent()) {			
			patientInvoiceDetailRepository.delete(i.get());
			PatientInvoice patientInvoice = i.get().getPatientInvoice();
			int j = 0;
			for(PatientInvoiceDetail d : patientInvoice.getPatientInvoiceDetails()) {
				j = j++;
			}
			if(j == 0) {
				patientInvoiceRepository.delete(patientInvoice);
			}			
		}
		if(pd.isPresent()) {
			//disable deleting a paid test first, in the mean time
			//throw new InvalidOperationException("Can not delete a paid prescription, please contact system administrator");
			patientPaymentDetailRepository.delete(pd.get());
		}
		
		prescriptionRepository.delete(prescription);
		patientBillRepository.delete(patientBill);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/delete_prescription").toUriString());
		return ResponseEntity.created(uri).body(true);
	}
	
	@GetMapping("/patients/get_doctor_inpatient_list") 
	public ResponseEntity<List<Admission>> getDoctorInpatientList(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("IN-PROCESS");
		statuses.add("STOPPED");
		
		List<Admission> admissions = admissionRepository.findAllByStatusIn(statuses);
		
		//HashSet<Admission> h = new HashSet<Admission>(admissions);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_doctor_inpatient_list").toUriString());
		return ResponseEntity.created(uri).body(admissions);
	}
	
	@GetMapping("/patients/get_nurse_inpatient_list") 
	public ResponseEntity<List<Admission>> getNurseInpatientList(
			HttpServletRequest request){
		
		List<Admission> admissions = admissionRepository.findAllByStatus("IN-PROCESS");
		
		//HashSet<Admission> h = new HashSet<Admission>(admissions);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_nurse_inpatient_list").toUriString());
		return ResponseEntity.created(uri).body(admissions);
	}
	
	@GetMapping("/patients/get_nurse_outpatient_list") 
	public ResponseEntity<List<Consultation>> getNurseOutpatientList(
			HttpServletRequest request){
		List<String> statuses = new ArrayList<>();
		statuses.add("IN-PROCESS");
		statuses.add("PENDING");
		List<Consultation> consultations = consultationRepository.findAllByStatusIn(statuses);
		
		List<Consultation> consultationsToShow = new ArrayList<>();
		for(Consultation c : consultations) {
			if(c.getPatientBill().getStatus().equals("PAID") || c.getPatientBill().getStatus().equals("COVERED") || c.getPatientBill().getStatus().equals("VERIFIED")) {
				consultationsToShow.add(c);
			}
		}
		
		//HashSet<Admission> h = new HashSet<Admission>(admissions);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_nurse_outpatient_list").toUriString());
		return ResponseEntity.created(uri).body(consultationsToShow);
	}
	
	@GetMapping("/patients/get_nurse_outsider_list")
	public ResponseEntity<List<NonConsultation>> getNurseOutsidertList(
			HttpServletRequest request){
		
		List<NonConsultation> nonConsultations = nonConsultationRepository.findAllByStatus("IN-PROCESS");
	
		//HashSet<Admission> h = new HashSet<Admission>(admissions);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_nurse_outsider_list").toUriString());
		return ResponseEntity.created(uri).body(nonConsultations);
	}
	
	
	@GetMapping("/patients/get_lab_outpatient_list") 
	public ResponseEntity<List<Patient>> getLabOutpatientList(
			HttpServletRequest request){
		
		List<Consultation> cs = consultationRepository.findAllByStatus("IN-PROCESS");
		List<LabTest> labTests = labTestRepository.findAllByConsultationIn(cs);			
		List<Patient> patients = new ArrayList<>();		
		for(LabTest t : labTests) {
			if(t.getPatient().getType().equals("OUTPATIENT") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_outpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_lab_inpatient_list") 
	public ResponseEntity<List<Patient>> getLabInpatientList(
			HttpServletRequest request){
		
		List<Admission> a = admissionRepository.findAllByStatus("IN-PROCESS");
		List<LabTest> labTests = labTestRepository.findAllByAdmissionIn(a);			
		List<Patient> patients = new ArrayList<>();		
		for(LabTest t : labTests) {
			if(t.getPatient().getType().equals("INPATIENT") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED") || t.getPatientBill().getStatus().equals("VERIFIED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_inpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_lab_outsider_list") 
	public ResponseEntity<List<Patient>> getLabOutsiderList(
			HttpServletRequest request){
		
		List<NonConsultation> ncs = nonConsultationRepository.findAllByStatus("IN-PROCESS");
		List<LabTest> labTests = labTestRepository.findAllByNonConsultationIn(ncs);			
		List<Patient> patients = new ArrayList<>();		
		for(LabTest t : labTests) {
			if(t.getPatient().getType().equals("OUTSIDER") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_outsider_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_radiology_outsider_list") 
	public ResponseEntity<List<Patient>> getRadiologyOutsiderList(
			HttpServletRequest request){
		
		List<NonConsultation> ncs = nonConsultationRepository.findAllByStatus("IN-PROCESS");
		List<Radiology> radiologies = radiologyRepository.findAllByNonConsultationIn(ncs);			
		List<Patient> patients = new ArrayList<>();		
		for(Radiology r : radiologies) {
			if(r.getPatient().getType().equals("OUTSIDER") && (r.getPatientBill().getStatus().equals("PAID") || r.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(r.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_radiology_outsider_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_radiology_outpatient_list") 
	public ResponseEntity<List<Patient>> getRadiologyOutpatientList(
			HttpServletRequest request){
		
		List<Consultation> cs = consultationRepository.findAllByStatus("IN-PROCESS");
		List<Radiology> radiologies = radiologyRepository.findAllByConsultationIn(cs);			
		List<Patient> patients = new ArrayList<>();		
		for(Radiology r : radiologies) {
			if(r.getPatient().getType().equals("OUTPATIENT") && (r.getPatientBill().getStatus().equals("PAID") || r.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(r.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_radiology_outpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_radiology_inpatient_list") 
	public ResponseEntity<List<Patient>> getRadiologyInpatientList(
			HttpServletRequest request){
		
		List<Admission> adm = admissionRepository.findAllByStatus("IN-PROCESS");
		List<Radiology> radiologies = radiologyRepository.findAllByAdmissionIn(adm);			
		List<Patient> patients = new ArrayList<>();		
		for(Radiology r : radiologies) {
			if(r.getPatient().getType().equals("INPATIENT") && (r.getPatientBill().getStatus().equals("PAID") || r.getPatientBill().getStatus().equals("COVERED") || r.getPatientBill().getStatus().equals("VERIFIED"))) {
				patients.add(r.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_radiology_inpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_procedure_outpatient_list") 
	public ResponseEntity<List<Patient>> getProcedureOutpatientList(
			HttpServletRequest request){
		
		List<Consultation> cs = consultationRepository.findAllByStatus("IN-PROCESS");
		List<Procedure> procedures = procedureRepository.findAllByConsultationIn(cs);			
		List<Patient> patients = new ArrayList<>();		
		for(Procedure p : procedures) {
			if(p.getPatient().getType().equals("OUTPATIENT") && (p.getPatientBill().getStatus().equals("PAID") || p.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(p.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_outpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_procedure_outsider_list") 
	public ResponseEntity<List<Patient>> getProcedureOutsiderList(
			HttpServletRequest request){
		
		List<NonConsultation> ncs = nonConsultationRepository.findAllByStatus("IN-PROCESS");
		List<Procedure> procedures = procedureRepository.findAllByNonConsultationIn(ncs);			
		List<Patient> patients = new ArrayList<>();		
		for(Procedure p : procedures) {
			if(p.getPatient().getType().equals("OUTSIDER") && (p.getPatientBill().getStatus().equals("PAID") || p.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(p.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_outsider_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_lab_tests_by_patient_id") 
	public ResponseEntity<List<LabTestModel>> getLabTestByPatientId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(id);
		if(!p.isPresent()) {
			return null;
		}
		List<Consultation> cs = consultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<NonConsultation> ncs = nonConsultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Admission> adm = admissionRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<LabTest> labTests = labTestRepository.findAllByConsultationInOrNonConsultationInOrAdmissionIn(cs, ncs, adm);	
		
		List<LabTestModel> labTestsToReturn = new ArrayList<>();
		for(LabTest test : labTests) {
			
			if(test.getPatientBill().getStatus().equals("PAID") || test.getPatientBill().getStatus().equals("COVERED") || test.getPatientBill().getStatus().equals("VERIFIED")) {
				
				List<LabTestAttachmentModel> labTestAttachmentModels = new ArrayList<>();
				for(LabTestAttachment labTestAttachment : test.getLabTestAttachments()) {
					LabTestAttachmentModel attachmentModel = new LabTestAttachmentModel();
					attachmentModel.setId(labTestAttachment.getId());
					attachmentModel.setFileName(labTestAttachment.getFileName());
					attachmentModel.setName(labTestAttachment.getName());
					attachmentModel.setLabTest(test);
					
					labTestAttachmentModels.add(attachmentModel);
				}
				
				
				
				LabTestModel t = new LabTestModel();
				t.setId(test.getId());
				t.setResult(test.getResult());
				t.setRange(test.getRange());
				t.setLevel(test.getLevel());
				t.setUnit(test.getUnit());
				t.setLabTestType(test.getLabTestType());
				t.setPatient(test.getPatient());
				t.setConsultation(test.getConsultation());
				t.setNonConsultation(test.getNonConsultation());
				t.setPatientBill(test.getPatientBill());
				t.setStatus(test.getStatus());
				
				t.setLabTestAttachments(labTestAttachmentModels);
				
				if(test.getCreatedAt() != null) {
					t.setCreated(test.getCreatedAt().toString()+" | "+userService.getUserById(test.getCreatedBy()).getNickname());
				}else {
					t.setCreated("");
				}
				if(test.getOrderedAt() != null) {
					t.setOrdered(test.getOrderedAt().toString()+" | "+userService.getUserById(test.getOrderedBy()).getNickname());
				}else {
					t.setOrdered("");
				}
				if(test.getRejectedAt() != null) {
					t.setRejected(test.getRejectedAt().toString()+" | "+userService.getUserById(test.getRejectedBy()).getNickname() + " | "+test.getRejectComment());
				}else {
					t.setRejected("");
				}
				t.setRejectComment(test.getRejectComment());			
				if(test.getAcceptedAt() != null) {
					t.setAccepted(test.getAcceptedAt().toString()+" | "+userService.getUserById(test.getAcceptedBy()).getNickname());
				}else {
					t.setAccepted("");
				}
				if(test.getHeldAt() != null) {
					t.setHeld(test.getHeldAt().toString()+" | "+userService.getUserById(test.getHeldBy()).getNickname());
				}else {
					t.setHeld("");
				}		
				if(test.getVerifiedAt() != null) {
					t.setVerified(test.getVerifiedAt().toString()+" | "+userService.getUserById(test.getVerifiedBy()).getNickname());
				}else {
					t.setVerified("");
				}				
				labTestsToReturn.add(t);				
			}
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_lab_tests_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(labTestsToReturn);
	}
	
	@PostMapping("/patients/accept_lab_test") 
	public boolean acceptLabTest(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!(t.get().getStatus().equals("PENDING") || t.get().getStatus().equals("REJECTED"))) {
			throw new InvalidOperationException("Could not accept, only PENDING or REJECTED tests can be accepted");
		}
		
		t.get().setAcceptedBy(userService.getUserId(request));
		t.get().setAcceptedOn(dayService.getDayId());
		t.get().setAcceptedAt(dayService.getTimeStamp());
		
		t.get().setRejectedBy(null);
		t.get().setRejectedOn(null);
		t.get().setRejectedAt(null);
		t.get().setRejectComment("");
		
		t.get().setStatus("ACCEPTED");
		labTestRepository.save(t.get());
		return true;
	}
	
	@PostMapping("/patients/reject_lab_test") 
	public boolean rejectLabTest(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!(t.get().getStatus().equals("PENDING") || t.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not accept, only PENDING or ACCEPTED tests can be rejected");
		}
		
		t.get().setRejectedBy(userService.getUserId(request));
		t.get().setRejectedOn(dayService.getDayId());
		t.get().setRejectedAt(dayService.getTimeStamp());
		
		t.get().setAcceptedBy(null);
		t.get().setAcceptedOn(null);
		t.get().setAcceptedAt(null);
		
		t.get().setStatus("REJECTED");
		labTestRepository.save(t.get());
		return true;
	}
	
	@PostMapping("/patients/collect_lab_test") 
	public boolean collectLabTest(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!(t.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not accept, only ACCEPTED tests can be collected");
		}
		
		t.get().setCollectedBy(userService.getUserId(request));
		t.get().setCollectedOn(dayService.getDayId());
		t.get().setCollectedAt(dayService.getTimeStamp());
		
		t.get().setStatus("COLLECTED");
		labTestRepository.save(t.get());
		return true;
	}
	
	@PostMapping("/patients/verify_lab_test") 
	public boolean verifyLabTestResult(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!t.get().getStatus().equals("COLLECTED")) {
			throw new InvalidOperationException("Could not verify, only COLLECTED tests can be verified");
		}
		t.get().setResult(test.getResult());
		t.get().setLevel(test.getLevel());
		t.get().setRange(test.getRange());
		t.get().setUnit(test.getUnit());
		
		t.get().setVerifiedBy(userService.getUserId(request));
		t.get().setVerifiedOn(dayService.getDayId());
		t.get().setVerifiedAt(dayService.getTimeStamp());
		
		t.get().setStatus("VERIFIED");
		labTestRepository.save(t.get());
		return true;
	}
	
	@PostMapping("/patients/save_lab_test_results")
	public boolean saveLabTestResult(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!t.get().getStatus().equals("COLLECTED")) {
			throw new InvalidOperationException("Could not update, only COLLECTED tests can be saved/updated");
		}
		t.get().setResult(test.getResult());
		t.get().setLevel(test.getLevel());
		t.get().setRange(test.getRange());
		t.get().setUnit(test.getUnit());
		
		labTestRepository.save(t.get());
		return true;
	}
	
	@PostMapping("/patients/hold_lab_test") 
	public boolean holdLabTest(
			@RequestBody LLabTest test,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(test.getId());
		if(!t.isPresent()) {
			throw new NotFoundException("Lab Test not found");
		}
		if(!(t.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not hold, only ACCEPTED tests can be held");
		}
		
		t.get().setHeldBy(userService.getUserId(request));
		t.get().setHeldOn(dayService.getDayId());
		t.get().setHeldAt(dayService.getTimeStamp());
		
		t.get().setStatus("PENDING");
		labTestRepository.save(t.get());
		return true;
	}	
	
	@PostMapping("/patients/accept_procedure") 
	public boolean acceptProcedure(
			@RequestBody LProcedure pro,
			HttpServletRequest request){
		Optional<Procedure> p = procedureRepository.findById(pro.getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Procedure not found");
		}
		if(!(p.get().getStatus().equals("PENDING") || p.get().getStatus().equals("REJECTED"))) {
			throw new InvalidOperationException("Could not accept, only PENDING or REJECTED tests can be accepted");
		}
		
		p.get().setAcceptedby(userService.getUserId(request));
		p.get().setAcceptedOn(dayService.getDayId());
		p.get().setAcceptedAt(dayService.getTimeStamp());
		
		p.get().setRejectedby(null);
		p.get().setRejectedOn(null);
		p.get().setRejectedAt(null);
		p.get().setRejectComment("");
		
		p.get().setStatus("ACCEPTED");
		procedureRepository.save(p.get());
		return true;
	}
	
	@PostMapping("/patients/update_procedure") 
	public boolean updateProcedure(
			@RequestBody LProcedure pro,
			HttpServletRequest request){
		Optional<Procedure> p = procedureRepository.findById(pro.getId());
		if(!p.isPresent()) {
			throw new NotFoundException("Procedure not found");
		}
		if(!(p.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not update, only accepted procedure can be updated");
		}
		
		p.get().setNote(pro.getNote());
		procedureRepository.save(p.get());
		return true;
	}	
	
	@GetMapping("/patients/load_non_consultation_id")
	public ResponseEntity<Long> getNonConsultationId(
			@RequestParam(name = "patient_id") Long id,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(id);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found");
		}
		if(!p.get().getType().equals("OUTSIDER")) {
			throw new InvalidOperationException("Operation only allowed for outsiders");
		}
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		Optional<NonConsultation> nc = nonConsultationRepository.findByPatientAndStatusIn(p.get(), statuses);
		
		Visit visit = new Visit();
		visit.setPatient(p.get());
		visit.setStatus("PENDING");
		visit.setType("OUTSIDER");
		
		visit.setSequence("SUBSEQUENT");
		
		visit.setCreatedBy(userService.getUser(request).getId());
		visit.setCreatedOn(dayService.getDay().getId());
		visit.setCreatedAt(dayService.getTimeStamp());
		
		
		NonConsultation nonConsultation = new NonConsultation();
		if(nc.isEmpty()) {
			visit = visitRepository.save(visit);
			
			nonConsultation.setCreatedBy(userService.getUserId(request));
			nonConsultation.setCreatedOn(dayService.getDayId());
			nonConsultation.setCreatedAt(dayService.getTimeStamp());
			
			nonConsultation.setVisit(visit);
			nonConsultation.setPatient(p.get());
			nonConsultation.setInsurancePlan(p.get().getInsurancePlan());
			nonConsultation.setMembershipNo(p.get().getMembershipNo());
			nonConsultation.setPaymentType(p.get().getPaymentType());
			nonConsultation.setStatus("PENDING");
			
			nonConsultation = nonConsultationRepository.save(nonConsultation);
		}else {
			nonConsultation = nc.get();
		}
		return ResponseEntity.ok().body(nonConsultation.getId());
	}
	
	@GetMapping("/patients/get_radiologies_by_patient_id") 
	public ResponseEntity<List<RadiologyModel>> getRadiologyByPatientId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(id);
		if(!p.isPresent()) {
			return null;
		}
		List<Consultation> cs = consultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<NonConsultation> ncs = nonConsultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Admission> adm = admissionRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Radiology> radiologies = radiologyRepository.findAllByConsultationInOrNonConsultationInOrAdmissionIn(cs, ncs, adm);	
		
		List<RadiologyModel> radiologiesToReturn = new ArrayList<>();
		for(Radiology radiology : radiologies) {
			if(radiology.getPatientBill().getStatus().equals("PAID") || radiology.getPatientBill().getStatus().equals("COVERED") || radiology.getPatientBill().getStatus().equals("VERIFIED")) {
				RadiologyModel radio = new RadiologyModel();
				radio.setId(radiology.getId());
				radio.setResult(radiology.getResult());
				radio.setDiagnosisType(radiology.getDiagnosisType());
				radio.setDescription(radiology.getDescription());
				radio.setAttachment(radiology.getAttachment());
				radio.setRadiologyType(radiology.getRadiologyType());
				radio.setPatient(radiology.getPatient());
				radio.setPatientBill(radiology.getPatientBill());
				radio.setStatus(radiology.getStatus());
				
				
				List<RadiologyAttachmentModel> radiologyAttachmentModels = new ArrayList<>();
				for(RadiologyAttachment radiologyAttachment : radiology.getRadiologyAttachments()) {
					RadiologyAttachmentModel attachmentModel = new RadiologyAttachmentModel();
					attachmentModel.setId(radiologyAttachment.getId());
					attachmentModel.setFileName(radiologyAttachment.getFileName());
					attachmentModel.setName(radiologyAttachment.getName());
					attachmentModel.setRadiology(radiology);
					
					radiologyAttachmentModels.add(attachmentModel);
				}
				
				radio.setRadiologyAttachments(radiologyAttachmentModels);
				
				
				if(radiology.getCreatedAt() != null) {
					radio.setCreated(radiology.getCreatedAt().toString()+" | "+userService.getUserById(radiology.getCreatedBy()).getNickname());
				}else {
					radio.setCreated("");
				}
				if(radiology.getOrderedAt() != null) {
					radio.setOrdered(radiology.getOrderedAt().toString()+" | "+userService.getUserById(radiology.getOrderedby()).getNickname());
				}else {
					radio.setOrdered("");
				}
				if(radiology.getRejectedAt() != null) {
					radio.setRejected(radiology.getRejectedAt().toString()+" | "+userService.getUserById(radiology.getRejectedby()).getNickname() + " | "+radiology.getRejectComment());
				}else {
					radio.setRejected("");
				}
				radio.setRejectComment(radiology.getRejectComment());			
				if(radiology.getAcceptedAt() != null) {
					radio.setAccepted(radiology.getAcceptedAt().toString()+" | "+userService.getUserById(radiology.getAcceptedby()).getNickname());
				}else {
					radio.setAccepted("");
				}
				if(radiology.getHeldAt() != null) {
					radio.setHeld(radiology.getHeldAt().toString()+" | "+userService.getUserById(radiology.getHeldby()).getNickname());
				}else {
					radio.setHeld("");
				}		
				if(radiology.getVerifiedAt() != null) {
					radio.setVerified(radiology.getVerifiedAt().toString()+" | "+userService.getUserById(radiology.getVerifiedby()).getNickname());
				}else {
					radio.setVerified("");
				}
				
				radiologiesToReturn.add(radio);				
			}
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_radiologies_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(radiologiesToReturn);
	}
	
	@PostMapping("/patients/accept_radiology") 
	public boolean acceptRadiology(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId());
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!(radiology.get().getStatus().equals("PENDING") || radiology.get().getStatus().equals("REJECTED"))) {
			throw new InvalidOperationException("Could not accept, only PENDING or REJECTED radiologies can be accepted");
		}
		
		radiology.get().setAcceptedby(userService.getUserId(request));
		radiology.get().setAcceptedOn(dayService.getDayId());
		radiology.get().setAcceptedAt(dayService.getTimeStamp());
		
		radiology.get().setRejectedby(null);
		radiology.get().setRejectedOn(null);
		radiology.get().setRejectedAt(null);
		radiology.get().setRejectComment("");
		
		radiology.get().setStatus("ACCEPTED");
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@PostMapping("/patients/reject_radiology") 
	public boolean rejectRadiology(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId());
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!(radiology.get().getStatus().equals("PENDING") || radiology.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not accept, only PENDING or ACCEPTED radiologies can be rejected");
		}
		
		radiology.get().setRejectedby(userService.getUserId(request));
		radiology.get().setRejectedOn(dayService.getDayId());
		radiology.get().setRejectedAt(dayService.getTimeStamp());
		
		radiology.get().setAcceptedby(null);
		radiology.get().setAcceptedOn(null);
		radiology.get().setAcceptedAt(null);
		
		radiology.get().setStatus("REJECTED");		
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@PostMapping("/patients/hold_radiology") 
	public boolean holdRadiology(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId());
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!(radiology.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not hold, only ACCEPTED radiologies can be held");
		}
		
		radiology.get().setHeldby(userService.getUserId(request));
		radiology.get().setHeldOn(dayService.getDayId());
		radiology.get().setHeldAt(dayService.getTimeStamp());
		
		radiology.get().setStatus("PENDING");
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@PostMapping("/patients/verify_radiology") 
	public boolean verifyRadiologyResult(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId());
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!radiology.get().getStatus().equals("ACCEPTED")) {
			throw new InvalidOperationException("Could not verify, only ACCEPTED radiologies can be verified");
		}
		radiology.get().setResult(radio.getResult());
		//radiology.get().setDiagnosisType(radio.getDiagnosisType());
		//radiology.get().setDescription(radio.getDescription());
		radiology.get().setAttachment(radio.getAttachment());
				
		radiology.get().setVerifiedby(userService.getUserId(request));
		radiology.get().setVerifiedOn(dayService.getDayId());
		radiology.get().setVerifiedAt(dayService.getTimeStamp());
		
		radiology.get().setStatus("VERIFIED");
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@PostMapping("/patients/save_radiology_results") 
	public boolean saveRadiologyResult(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId());
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!radiology.get().getStatus().equals("ACCEPTED")) {
			throw new InvalidOperationException("Could not update, only ACCEPTED radiologies can be updated");
		}
		radiology.get().setResult(radio.getResult());
		//radiology.get().setDiagnosisType(radio.getDiagnosisType());
		//radiology.get().setDescription(radio.getDescription());
		radiology.get().setAttachment(radio.getAttachment());
				
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@PostMapping("/patients/collect_radiology111") 
	public boolean collectRadiology(
			@RequestBody LRadiology radio,
			HttpServletRequest request){
		Optional<Radiology> radiology = radiologyRepository.findById(radio.getId()); 
		if(!radiology.isPresent()) {
			throw new NotFoundException("Radiology not found");
		}
		if(!(radiology.get().getStatus().equals("ACCEPTED"))) {
			throw new InvalidOperationException("Could not accept, only ACCEPTED radiologies can be collected");
		}
		
		radiology.get().setCollectedby(userService.getUserId(request));
		radiology.get().setCollectedOn(dayService.getDayId());
		radiology.get().setCollectedAt(dayService.getTimeStamp());
		
		radiology.get().setStatus("COLLECTED");
		radiologyRepository.save(radiology.get());
		return true;
	}
	
	@GetMapping("/patients/get_pharmacy_outpatient_list") 
	public ResponseEntity<List<Patient>> getPharmacyOutpatientList(
			HttpServletRequest request){
		
		List<Consultation> cs = consultationRepository.findAllByStatus("IN-PROCESS");
		List<Prescription> prescriptions = prescriptionRepository.findAllByConsultationIn(cs);			
		List<Patient> patients = new ArrayList<>();		
		for(Prescription t : prescriptions) {
			if(t.getPatient().getType().equals("OUTPATIENT") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_pharmacy_outpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_pharmacy_outsider_list") 
	public ResponseEntity<List<Patient>> getPharmacyOutsiderList(
			HttpServletRequest request){
		
		List<NonConsultation> ncs = nonConsultationRepository.findAllByStatus("IN-PROCESS");
		List<Prescription> prescriptions = prescriptionRepository.findAllByNonConsultationIn(ncs);			
		List<Patient> patients = new ArrayList<>();		
		for(Prescription t : prescriptions) {
			if(t.getPatient().getType().equals("OUTSIDER") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_pharmacy_outsider_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_pharmacy_inpatient_list") 
	public ResponseEntity<List<Patient>> getPharmacyInpatientList(
			HttpServletRequest request){
		
		List<Admission> adm = admissionRepository.findAllByStatus("IN-PROCESS");
		List<Prescription> prescriptions = prescriptionRepository.findAllByAdmissionIn(adm);			
		List<Patient> patients = new ArrayList<>();		
		for(Prescription t : prescriptions) {
			if(t.getPatient().getType().equals("INPATIENT") && (t.getPatientBill().getStatus().equals("PAID") || t.getPatientBill().getStatus().equals("COVERED") || t.getPatientBill().getStatus().equals("VERIFIED"))) {
				patients.add(t.getPatient());
			}
		}
		HashSet<Patient> h = new HashSet<Patient>(patients);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_pharmacy_inpatient_list").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<Patient>(h));
	}
	
	@GetMapping("/patients/get_prescriptions_by_patient_id") 
	public ResponseEntity<List<PrescriptionModel>> getPrescriptionsByPatientId(
			@RequestParam(name = "patient_id") Long patientId,
			@RequestParam(name = "pharmacy_id") Long pharmacyId,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(patientId);
		if(!p.isPresent()) {
			throw new NotFoundException("Patient not found");
		}
		Optional<Pharmacy> phar = pharmacyRepository.findById(pharmacyId);
		if(!phar.isPresent()) {
			throw new NotFoundException("Pharmacy not found");
		}
		List<Consultation> cs = consultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<NonConsultation> ncs = nonConsultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Admission> adm = admissionRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Prescription> prescriptions = prescriptionRepository.findAllByConsultationInOrNonConsultationInOrAdmissionIn(cs, ncs, adm);	
		
		List<PrescriptionModel> prescriptionsToReturn = new ArrayList<>();
		for(Prescription pres : prescriptions) {
			if(pres.getPatientBill().getStatus().equals("PAID") || pres.getPatientBill().getStatus().equals("COVERED") || pres.getPatientBill().getStatus().equals("VERIFIED")) {
				PrescriptionModel m = new PrescriptionModel();
				m.setId(pres.getId());
				m.setDosage(pres.getDosage());
				m.setFrequency(pres.getFrequency());
				m.setRoute(pres.getRoute());
				m.setDays(pres.getDays());
				m.setQty(pres.getQty());
				m.setStatus(pres.getStatus());
				m.setMedicine(pres.getMedicine());
				m.setPatient(pres.getPatient());
				m.setConsultation(pres.getConsultation());
				m.setNonConsultation(pres.getNonConsultation());
				m.setPatientBill(pres.getPatientBill());
				m.setStatus(pres.getStatus());
				
				Optional<PharmacyMedicine> pm = pharmacyMedicineRepository.findByPharmacyAndMedicine(phar.get(), pres.getMedicine());
				PharmacyMedicine pharmacyMedicine = new PharmacyMedicine();
				if(pm.isEmpty()) {
					pharmacyMedicine.setPharmacy(phar.get());
					pharmacyMedicine.setMedicine(pres.getMedicine());
					pharmacyMedicine.setStock(0);
					pharmacyMedicine = pharmacyMedicineRepository.save(pharmacyMedicine);
				}else {
					pharmacyMedicine = pm.get();
				}
				
				m.setStock(pharmacyMedicine.getStock());
				
				if(pres.getCreatedAt() != null) {
					m.setCreated(pres.getCreatedAt().toString()+" | "+userService.getUserById(pres.getCreatedBy()).getNickname());
				}else {
					m.setCreated("");
				}
				if(pres.getOrderedAt() != null) {
					m.setOrdered(pres.getOrderedAt().toString()+" | "+userService.getUserById(pres.getOrderedby()).getNickname());
				}else {
					m.setOrdered("");
				}
				if(pres.getRejectedAt() != null) {
					m.setRejected(pres.getRejectedAt().toString()+" | "+userService.getUserById(pres.getRejectedby()).getNickname() + " | "+pres.getRejectComment());
				}else {
					m.setRejected("");
				}
				m.setRejectComment(pres.getRejectComment());			
				if(pres.getAcceptedAt() != null) {
					m.setAccepted(pres.getAcceptedAt().toString()+" | "+userService.getUserById(pres.getAcceptedby()).getNickname());
				}else {
					m.setAccepted("");
				}
				if(pres.getHeldAt() != null) {
					m.setHeld(pres.getHeldAt().toString()+" | "+userService.getUserById(pres.getHeldby()).getNickname());
				}else {
					m.setHeld("");
				}		
				if(pres.getVerifiedAt() != null) {
					m.setVerified(pres.getVerifiedAt().toString()+" | "+userService.getUserById(pres.getVerifiedby()).getNickname());
				}else {
					m.setVerified("");
				}
				
				prescriptionsToReturn.add(m);				
			}
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_prescriptions_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(prescriptionsToReturn);
	}
	
	@GetMapping("/patients/get_procedures_by_patient_id") 
	public ResponseEntity<List<ProcedureModel>> getProcedureByPatientId(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		Optional<Patient> p = patientRepository.findById(id);
		if(!p.isPresent()) {
			return null;
		}
		List<Consultation> cs = consultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<NonConsultation> ncs = nonConsultationRepository.findAllByPatientAndStatus(p.get(), "IN-PROCESS");
		List<Procedure> procedures = procedureRepository.findAllByConsultationInOrNonConsultationIn(cs, ncs);
		
		List<ProcedureModel> proceduresToReturn = new ArrayList<>();
		for(Procedure procedure : procedures) {
			if(procedure.getPatientBill().getStatus().equals("PAID") || procedure.getPatientBill().getStatus().equals("COVERED") || procedure.getPatientBill().getStatus().equals("VERIFIED")) {
				ProcedureModel pro = new ProcedureModel();
				pro.setId(procedure.getId());
				pro.setDiagnosisType(procedure.getDiagnosisType());
				pro.setNote(procedure.getNote());
				pro.setProcedureType(procedure.getProcedureType());
				pro.setPatient(procedure.getPatient());
				pro.setPatientBill(procedure.getPatientBill());
				pro.setStatus(procedure.getStatus());
				
				
				if(procedure.getCreatedAt() != null) {
					pro.setCreated(procedure.getCreatedAt().toString()+" | "+userService.getUserById(procedure.getCreatedBy()).getNickname());
				}else {
					pro.setCreated("");
				}
				if(procedure.getOrderedAt() != null) {
					pro.setOrdered(procedure.getOrderedAt().toString()+" | "+userService.getUserById(procedure.getOrderedby()).getNickname());
				}else {
					pro.setOrdered("");
				}
				if(procedure.getRejectedAt() != null) {
					pro.setRejected(procedure.getRejectedAt().toString()+" | "+userService.getUserById(procedure.getRejectedby()).getNickname() + " | "+procedure.getRejectComment());
				}else {
					pro.setRejected("");
				}
				pro.setRejectComment(procedure.getRejectComment());			
				if(procedure.getAcceptedAt() != null) {
					pro.setAccepted(procedure.getAcceptedAt().toString()+" | "+userService.getUserById(procedure.getAcceptedby()).getNickname());
				}else {
					pro.setAccepted("");
				}
				if(procedure.getHeldAt() != null) {
					pro.setHeld(procedure.getHeldAt().toString()+" | "+userService.getUserById(procedure.getHeldby()).getNickname());
				}else {
					pro.setHeld("");
				}		
				if(procedure.getVerifiedAt() != null) {
					pro.setVerified(procedure.getVerifiedAt().toString()+" | "+userService.getUserById(procedure.getVerifiedby()).getNickname());
				}else {
					pro.setVerified("");
				}
				
				proceduresToReturn.add(pro);				
			}
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_procedures_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(proceduresToReturn);
	}
	
	@GetMapping("/patients/get_all_clinical_notes_by_patient_id") 
	public ResponseEntity<List<ClinicalNoteModel>> getAllClinicalNotesByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}
		List<Consultation> cons = consultationRepository.findAllByPatient(p.get());
		List<NonConsultation> nonCons = nonConsultationRepository.findAllByPatient(p.get());
		List<Admission> adms = admissionRepository.findAllByPatient(p.get());
				
		List<ClinicalNote> clinicalNotes = clinicalNoteRepository.findAllByConsultationInOrNonConsultationInOrAdmissionIn(cons, nonCons, adms);
		
		HashSet<ClinicalNote> h = new HashSet<ClinicalNote>(clinicalNotes);
		
		List<ClinicalNoteModel> models = new ArrayList<>();
		for(ClinicalNote l : clinicalNotes) {
			ClinicalNoteModel model= new ClinicalNoteModel();
			model.setId(l.getId());
			
			model.setMainComplain(l.getMainComplain());
			model.setPresentIllnessHistory(l.getPresentIllnessHistory());
			model.setPastMedicalHistory(l.getPastMedicalHistory());
			model.setFamilyAndSocialHistory(l.getFamilyAndSocialHistory());
			model.setDrugsAndAllergyHistory(l.getDrugsAndAllergyHistory());
			model.setReviewOfOtherSystems(l.getReviewOfOtherSystems());
			model.setPhysicalExamination(l.getPhysicalExamination());
			model.setManagementPlan(l.getManagementPlan());
			
			model.setConsultation(null);
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			
			models.add(model);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_clinical_notes_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<ClinicalNoteModel>(models));
	}
	
	@GetMapping("/patients/get_all_general_examinations_by_patient_id") 
	public ResponseEntity<List<GeneralExaminationModel>> getAllGeneralExaminationsByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}
		List<Consultation> cons = consultationRepository.findAllByPatient(p.get());
		List<NonConsultation> nonCons = nonConsultationRepository.findAllByPatient(p.get());
		List<Admission> adms = admissionRepository.findAllByPatient(p.get());
		
		List<GeneralExamination> generalExaminations = generalExaminationRepository.findAllByConsultationInOrNonConsultationInOrAdmissionIn(cons, nonCons, adms);
		
		HashSet<GeneralExamination> h = new HashSet<GeneralExamination>(generalExaminations);
		
		List<GeneralExaminationModel> models = new ArrayList<>();
		for(GeneralExamination l : generalExaminations) {
			GeneralExaminationModel model= new GeneralExaminationModel();
			model.setId(l.getId());
			
			model.setPressure(l.getPressure());
			model.setTemperature(l.getTemperature());
			model.setPulseRate(l.getPulseRate());
			model.setWeight(l.getWeight());
			model.setHeight(l.getHeight());
			model.setBodyMassIndex(l.getBodyMassIndex());
			model.setBodyMassIndexComment(l.getBodyMassIndexComment());
			model.setBodySurfaceArea(l.getBodySurfaceArea());
			model.setSaturationOxygen(l.getSaturationOxygen());
			model.setRespiratoryRate(l.getRespiratoryRate());
			model.setDescription(l.getDescription());
			
			model.setConsultation(null);
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			
			models.add(model);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_general_examination_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<GeneralExaminationModel>(models));
	}
	
	@GetMapping("/patients/get_all_working_diagnosises_by_patient_id") 
	public ResponseEntity<List<WorkingDiagnosis>> getAllWorkingDiagnosisesByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}
		List<Consultation> cons = consultationRepository.findAllByPatient(p.get());
		
		List<WorkingDiagnosis> workingDiagnosises = workingDiagnosisRepository.findAllByConsultationIn(cons);
		
		HashSet<WorkingDiagnosis> h = new HashSet<WorkingDiagnosis>(workingDiagnosises);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_working_diagnosises_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<WorkingDiagnosis>(h));
	}
	
	@GetMapping("/patients/get_all_final_diagnosises_by_patient_id") 
	public ResponseEntity<List<FinalDiagnosisModel>> getAllFinalDiagnosisesByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}
		List<Consultation> cons = consultationRepository.findAllByPatient(p.get());
		
		List<FinalDiagnosis> finalDiagnosises = finalDiagnosisRepository.findAllByConsultationIn(cons);
		
		List<FinalDiagnosisModel> models = new ArrayList<>();
		for(FinalDiagnosis l : finalDiagnosises) {
			FinalDiagnosisModel model= new FinalDiagnosisModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setDiagnosisType(l.getDiagnosisType());
			
			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}			
			models.add(model);
		}
		
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_final_diagnosises_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(new ArrayList<FinalDiagnosisModel>(models));
	}
	
	@GetMapping("/patients/get_all_lab_tests_by_patient_id") 
	public ResponseEntity<List<LabTestModel>> getAllLabTestsByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}

		List<LabTest> labTests = labTestRepository.findAllByPatient(p.get());
		
		
		
		List<LabTestModel> models = new ArrayList<>();
		for(LabTest l : labTests) {
			LabTestModel model= new LabTestModel();
			model.setId(l.getId());
			model.setResult(l.getResult());			
			model.setReport(l.getReport());
			model.setDescription(l.getDescription());
			model.setLabTestType(l.getLabTestType());
			model.setPatientBill(l.getPatientBill());
			model.setRange(l.getRange());
			model.setLevel(l.getLevel());
			model.setUnit(l.getUnit());
			model.setStatus(l.getStatus());
			
			List<LabTestAttachmentModel> labTestAttachmentModels = new ArrayList<>();
			for(LabTestAttachment labTestAttachment : l.getLabTestAttachments()) {
				LabTestAttachmentModel labTestAttachmentModel = new LabTestAttachmentModel();
				labTestAttachmentModel.setId(labTestAttachment.getId());
				labTestAttachmentModel.setFileName(labTestAttachment.getFileName());
				labTestAttachmentModel.setName(labTestAttachment.getName());
				labTestAttachmentModels.add(labTestAttachmentModel);
			}
			model.setLabTestAttachments(labTestAttachmentModels);

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedBy()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedBy()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedBy()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldBy()).getNickname());
			}else {
				model.setHeld("");
			}
			if(l.getCollectedAt() != null) {
				model.setCollected(l.getCollectedAt().toString()+" | "+userService.getUserById(l.getCollectedBy()).getNickname());
			}else {
				model.setCollected("");
			}
			
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedBy()).getNickname());
			}else {
				model.setVerified("");
			}
			
			models.add(model);
		}
		
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_lab_tests_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/get_all_radiologies_by_patient_id") 
	public ResponseEntity<List<RadiologyModel>> getAllRadiologiesByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}

		List<Radiology> radiologies = radiologyRepository.findAllByPatient(p.get());
		
		List<RadiologyModel> models = new ArrayList<>();
		for(Radiology r : radiologies) {
			RadiologyModel model= new RadiologyModel();
			model.setId(r.getId());
			model.setResult(r.getResult());
			model.setReport(r.getReport());
			model.setRadiologyType(r.getRadiologyType());
			model.setDescription(r.getDescription());
			model.setDiagnosisType(r.getDiagnosisType());			
			model.setPatientBill(r.getPatientBill());
			model.setAttachment(r.getAttachment());
			
			
			List<RadiologyAttachmentModel> radiologyAttachmentModels = new ArrayList<>();
			for(RadiologyAttachment radiologyAttachment : r.getRadiologyAttachments()) {
				RadiologyAttachmentModel radiologyAttachmentModel = new RadiologyAttachmentModel();
				radiologyAttachmentModel.setId(radiologyAttachment.getId());
				radiologyAttachmentModel.setFileName(radiologyAttachment.getFileName());
				radiologyAttachmentModel.setName(radiologyAttachment.getName());
				radiologyAttachmentModels.add(radiologyAttachmentModel);
			}
			model.setRadiologyAttachments(radiologyAttachmentModels);
			
			
			if(r.getCreatedAt() != null) {
				model.setCreated(r.getCreatedAt().toString()+" | "+userService.getUserById(r.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(r.getRejectedAt() != null) {
				model.setRejected(r.getRejectedAt().toString()+" | "+userService.getUserById(r.getRejectedby()).getNickname());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(r.getRejectComment());			
			if(r.getAcceptedAt() != null) {
				model.setAccepted(r.getAcceptedAt().toString()+" | "+userService.getUserById(r.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(r.getOrderedAt() != null) {
				model.setOrdered(r.getOrderedAt().toString()+" | "+userService.getUserById(r.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(r.getVerifiedAt() != null) {
				model.setVerified(r.getVerifiedAt().toString()+" | "+userService.getUserById(r.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			model.setStatus(r.getStatus());models.add(model);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_radiologies_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/get_all_procedures_by_patient_id") 
	public ResponseEntity<List<ProcedureModel>> getAllProceduresByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}

		List<Procedure> procedures = procedureRepository.findAllByPatient(p.get());
		
		List<ProcedureModel> models = new ArrayList<>();
		for(Procedure l : procedures) {
			ProcedureModel model= new ProcedureModel();
			model.setId(l.getId());
			model.setProcedureType(l.getProcedureType());
			model.setPatientBill(l.getPatientBill());
			model.setDiagnosisType(l.getDiagnosisType());
			model.setType(l.getType());
			model.setTime(l.getTime());
			model.setDate(l.getDate());
			model.setHours(l.getHours());
			model.setMinutes(l.getMinutes());
			model.setStatus(l.getStatus());
			model.setNote(l.getNote());
			model.setTheatre(l.getTheatre());

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedby()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldby()).getNickname());
			}else {
				model.setHeld("");
			}		
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			
			models.add(model);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_procedures_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/get_all_prescriptions_by_patient_id") 
	public ResponseEntity<List<PrescriptionModel>> getAllPrescriptionsByPatient(
			@RequestParam(name = "patient_id") Long patientId,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(patientId);
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found in database");
		}

		List<Prescription> prescriptions = prescriptionRepository.findAllByPatient(p.get());
		
		List<PrescriptionModel> models = new ArrayList<>();
		for(Prescription l : prescriptions) {
			PrescriptionModel model= new PrescriptionModel();
			model.setId(l.getId());
			model.setMedicine(l.getMedicine());
			model.setDosage(l.getDosage());
			model.setFrequency(l.getFrequency());			
			model.setRoute(l.getRoute());
			model.setDays(l.getDays());
			model.setQty(l.getQty());
			model.setIssued(l.getIssued());
			model.setBalance(l.getBalance());
			model.setPatientBill(l.getPatientBill());
			model.setStatus(l.getStatus());

			if(l.getCreatedAt() != null) {
				model.setCreated(l.getCreatedAt().toString()+" | "+userService.getUserById(l.getCreatedBy()).getNickname());
			}else {
				model.setCreated("");
			}
			if(l.getOrderedAt() != null) {
				model.setOrdered(l.getOrderedAt().toString()+" | "+userService.getUserById(l.getOrderedby()).getNickname());
			}else {
				model.setOrdered("");
			}
			if(l.getRejectedAt() != null) {
				model.setRejected(l.getRejectedAt().toString()+" | "+userService.getUserById(l.getRejectedby()).getNickname() + " | "+l.getRejectComment());
			}else {
				model.setRejected("");
			}
			model.setRejectComment(l.getRejectComment());			
			if(l.getAcceptedAt() != null) {
				model.setAccepted(l.getAcceptedAt().toString()+" | "+userService.getUserById(l.getAcceptedby()).getNickname());
			}else {
				model.setAccepted("");
			}
			if(l.getHeldAt() != null) {
				model.setHeld(l.getHeldAt().toString()+" | "+userService.getUserById(l.getHeldby()).getNickname());
			}else {
				model.setHeld("");
			}		
			if(l.getVerifiedAt() != null) {
				model.setVerified(l.getVerifiedAt().toString()+" | "+userService.getUserById(l.getVerifiedby()).getNickname());
			}else {
				model.setVerified("");
			}
			
			if(l.getApprovedAt() != null) {
				model.setApproved(l.getApprovedAt().toString()+" | "+userService.getUserById(l.getApprovedBy()).getNickname());
			}else {
				model.setApproved("");
			}
			
			models.add(model);
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_all_prescriptions_by_patient_id").toUriString());
		return ResponseEntity.created(uri).body(models);
	}
	
	@GetMapping("/patients/get_prescription_by_id") 
	public ResponseEntity<PrescriptionModel> getPrescriptionById(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		
		Optional<Prescription> p = prescriptionRepository.findById(id);
		if(p.isEmpty()) {
			throw new NotFoundException("Prescription not found");
		}

		PrescriptionModel model= new PrescriptionModel();
		model.setId(p.get().getId());
		model.setMedicine(p.get().getMedicine());
		model.setDosage(p.get().getDosage());
		model.setFrequency(p.get().getFrequency());			
		model.setRoute(p.get().getRoute());
		model.setDays(p.get().getDays());
		model.setQty(p.get().getQty());
		model.setIssued(p.get().getIssued());
		model.setBalance(p.get().getBalance());
		model.setPatientBill(p.get().getPatientBill());
		model.setStatus(p.get().getStatus());

		if(p.get().getCreatedAt() != null) {
			model.setCreated(p.get().getCreatedAt().toString()+" | "+userService.getUserById(p.get().getCreatedBy()).getNickname());
		}else {
			model.setCreated("");
		}
		if(p.get().getOrderedAt() != null) {
			model.setOrdered(p.get().getOrderedAt().toString()+" | "+userService.getUserById(p.get().getOrderedby()).getNickname());
		}else {
			model.setOrdered("");
		}
		if(p.get().getRejectedAt() != null) {
			model.setRejected(p.get().getRejectedAt().toString()+" | "+userService.getUserById(p.get().getRejectedby()).getNickname() + " | "+p.get().getRejectComment());
		}else {
			model.setRejected("");
		}
		model.setRejectComment(p.get().getRejectComment());			
		if(p.get().getAcceptedAt() != null) {
			model.setAccepted(p.get().getAcceptedAt().toString()+" | "+userService.getUserById(p.get().getAcceptedby()).getNickname());
		}else {
			model.setAccepted("");
		}
		if(p.get().getHeldAt() != null) {
			model.setHeld(p.get().getHeldAt().toString()+" | "+userService.getUserById(p.get().getHeldby()).getNickname());
		}else {
			model.setHeld("");
		}		
		if(p.get().getVerifiedAt() != null) {
			model.setVerified(p.get().getVerifiedAt().toString()+" | "+userService.getUserById(p.get().getVerifiedby()).getNickname());
		}else {
			model.setVerified("");
		}
		
		if(p.get().getApprovedAt() != null) {
			model.setApproved(p.get().getApprovedAt().toString()+" | "+userService.getUserById(p.get().getApprovedBy()).getNickname());
		}else {
			model.setApproved("");
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/patients/get_prescription_by_id").toUriString());
		return ResponseEntity.created(uri).body(model);
	}
	
	@GetMapping("/patients/load_patients_like")
	public ResponseEntity<List<Patient>> getPatientNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Patient> patients = new ArrayList<Patient>();
		
		patients = patientRepository.findAllByNoContainingOrFirstNameContainingOrMiddleNameContainingOrLastNameContainingOrPhoneNoContaining(value, value, value, value, value);
		return ResponseEntity.ok().body(patients);
	}
	
	@GetMapping("/patients/load_patients_like_and_card")
	public ResponseEntity<List<Patient>> getPatientNameContainsWithCard(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<Patient> patients = new ArrayList<Patient>();
		
		patients = patientRepository.findAllByNoContainingOrFirstNameContainingOrMiddleNameContainingOrLastNameContainingOrPhoneNoContainingOrMembershipNoContaining(value, value, value, value, value, value);
		return ResponseEntity.ok().body(patients);
	}
	
	
	@PostMapping("/patients/do_admission")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<Admission>doAdmission(

			@RequestBody LAdmission adm, 
			HttpServletRequest request){		
		Optional<Patient> p = patientRepository.findById(adm.getPatient().getId());
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found");
		}
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		List<Admission> adms = admissionRepository.findAllByPatientAndStatusIn(p.get(), statuses);
		if(!adms.isEmpty()) {
			throw new InvalidOperationException("Could not process admission. The patient is already admitted");
		}
		
		Optional<WardBed> wb = wardBedRepository.findById(adm.getWardBed().getId());
		if(wb.isEmpty()) {
			throw new NotFoundException("Bed/Room nof found");
		}
		
		
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/do_admission").toUriString());
		return ResponseEntity.created(uri).body(patientService.doAdmission(p.get(), wb.get(), request));
	}
	
	@GetMapping("/patients/get_patient_direct_pending_invoices")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<PatientInvoice>>getPatientDirectPendingInvoices(
			HttpServletRequest request){
		
		List<PatientInvoice> invoices = patientInvoiceRepository.findAllByInsurancePlanAndStatus(null, "PENDING");
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_patient_pending_invoices").toUriString());
		return ResponseEntity.created(uri).body(invoices);
	}
	
	@GetMapping("/patients/get_patient_insurance_pending_invoices")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<PatientInvoice>>getPatientInsurancePendingInvoices(
			HttpServletRequest request){
		
		List<InsurancePlan> plans = insurancePlanRepository.findAll();
		
		List<PatientInvoice> invoices = patientInvoiceRepository.findAllByInsurancePlanInAndStatus(plans, "PENDING");
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_patient_pending_invoices").toUriString());
		return ResponseEntity.created(uri).body(invoices);
	}
	
	@GetMapping("/patients/get_patient_invoice")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<PatientInvoice>getPatientInvoice(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		
		Optional<PatientInvoice> inv = patientInvoiceRepository.findById(id);
		if(inv.isEmpty()) {
			throw new NotFoundException("Invoice not found");
		}
				
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_patient_invoice").toUriString());
		return ResponseEntity.created(uri).body(inv.get());
	}
	
	
	@PostMapping("/patients/save_discharge_plan")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DischargePlan>saveDischargePlan(

			@RequestBody DischargePlan plan, 
			HttpServletRequest request){		
		Optional<Admission> a = admissionRepository.findById(plan.getAdmission().getId());
		if(a.isEmpty()) {
			throw new NotFoundException("Admission not found");
		}
		DischargePlan dischargePlan = new DischargePlan();
		Optional<DischargePlan> p = dischargePlanRepository.findByAdmission(a.get());
		if(p.isPresent()) {
			dischargePlan = p.get();
		}else {
			Admission admission = a.get();
			admission.setStatus("STOPPED");
			admission = admissionRepository.save(admission);
			
			dischargePlan.setAdmission(admission);
			
			dischargePlan.setCreatedBy(userService.getUser(request).getId());
			dischargePlan.setCreatedOn(dayService.getDay().getId());
			dischargePlan.setCreatedAt(dayService.getTimeStamp());
		}
		dischargePlan.setHistory(plan.getHistory());
		dischargePlan.setInvestigation(plan.getInvestigation());
		dischargePlan.setManagement(plan.getManagement());
		dischargePlan.setOperationNote(plan.getOperationNote());
		dischargePlan.setIcuAdmissionNote(plan.getIcuAdmissionNote());
		dischargePlan.setGeneralRecommendation(plan.getGeneralRecommendation());
		dischargePlan.setStatus("PENDING");
		
		dischargePlan = dischargePlanRepository.save(dischargePlan);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_discharge_plan").toUriString());
		return ResponseEntity.created(uri).body(dischargePlan);
	}
	
	@GetMapping("/patients/load_discharge_plan")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DischargePlan>getDischargePlan(
			@RequestParam(name = "admission_id") Long admissionId,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_discharge_plan").toUriString());
		
		Optional<Admission> a = admissionRepository.findById(admissionId);
		if(a.isEmpty()) {
			throw new NotFoundException("Admission not found");
		}
		
		Optional<DischargePlan> p = dischargePlanRepository.findByAdmission(a.get());
		if(p.isEmpty()) {
			return ResponseEntity.created(uri).body(null);
		}				
		return ResponseEntity.created(uri).body(p.get());
	}
	
	@GetMapping("/patients/load_discharge_list")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<DischargePlan>>loadDischargeList(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("APPROVED");
		
		List<DischargePlan> plans = dischargePlanRepository.findAllByStatusIn(statuses);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_discharge_list").toUriString());
		
		return ResponseEntity.created(uri).body(plans);
	}
	
	
	@GetMapping("/patients/get_discharge_summary")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DischargePlan>getDischargeSummary(
			@RequestParam(name = "discharge_plan_id") Long dischargePlanId,
			HttpServletRequest request){
		
		Optional<DischargePlan> p = dischargePlanRepository.findById(dischargePlanId);
		if(p.isEmpty()) {
			throw new NotFoundException("Discharge plan not found");
		}
		
		Admission adm = p.get().getAdmission();
		
		List<PatientInvoice> invoices = patientInvoiceRepository.findAllByAdmission(adm);
		for(PatientInvoice invoice : invoices) {
			List<PatientInvoiceDetail> details = invoice.getPatientInvoiceDetails();
			for(PatientInvoiceDetail detail : details) {
				if(detail.getPatientBill().getStatus() != null) {
					if(detail.getPatientBill().getStatus().equals("UNPAID") || detail.getPatientBill().getStatus().equals("VERIFIED")) {
						throw new InvalidOperationException("Could not get discharge summary. Patient have uncleared bills.");
					}
				}
			}
		}
		
		for(PatientInvoice invoice : invoices) {
			invoice.setStatus("APPROVED");
			patientInvoiceRepository.save(invoice);
		}
		
		if(adm.getStatus() != null) {
			if(adm.getStatus().equals("STOPPED")) {
				adm.setStatus("SIGNED-OUT");
				adm.setDischargedBy(p.get().getCreatedBy());
				adm.setDischargedOn(p.get().getCreatedOn());
				adm.setDischargedAt(p.get().getCreatedAt());
				
				adm = admissionRepository.save(adm);
				
				WardBed wardBed = adm.getWardBed(); 
				wardBed.setStatus("EMPTY");
				wardBedRepository.save(wardBed);
				
				Patient patient = adm.getPatient();
				patient.setType("OUTPATIENT");
				patient.setPaymentType("CASH");
				patient.setInsurancePlan(null);
				patientRepository.save(patient);
				
				p.get().setStatus("APPROVED");
				p.get().setApprovedBy(p.get().getCreatedBy());
				p.get().setApprovedOn(p.get().getCreatedOn());
				p.get().setApprovedAt(dayService.getTimeStamp());	
			}
		}
		
		DischargePlan plan = dischargePlanRepository.save(p.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_discharge_summary").toUriString());
		
		return ResponseEntity.created(uri).body(plan);
	}
	
	@PostMapping("/patients/save_referral_plan")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<ReferralPlan>saveReferralPlan(

			@RequestBody ReferralPlan plan, 
			HttpServletRequest request){
		Optional<ExternalMedicalProvider> prov = externalMedicalProviderRepository.findById(plan.getExternalMedicalProvider().getId());
		if(prov.isEmpty()) {
			throw new NotFoundException("Medical Provider not found");
		}		
		if(plan.getAdmission().getId() != null && plan.getConsultation().getId() != null) {
			throw new InvalidOperationException("Could not process. Patient can be an inpatient or outpatient but not both");
		}
		if(plan.getAdmission().getId() == null && plan.getConsultation().getId() == null) {
			throw new InvalidOperationException("Could not process. Patient should either be an inpatient or outpatient");
		}
		
		Optional<Admission> a = null;
		Optional<Consultation> c = null;
		
		if(plan.getAdmission().getId() != null) {
			a = admissionRepository.findById(plan.getAdmission().getId());
		}
		
		if(plan.getConsultation().getId() != null) {
			c = consultationRepository.findById(plan.getConsultation().getId());
		}
		
		if(a != null && c != null) {
			if(a.isPresent() && c.isPresent()) {
				throw new InvalidOperationException("Could not process. Patient can be an inpatient or outpatient but not both");
			}
		}
		if(a != null && c != null) {
			if(a.isEmpty() && c.isEmpty()) {
				throw new InvalidOperationException("Could not process. Patient should either be an inpatient or outpatient");
			}
		}
		
		Admission admission = null;
		Consultation consultation = null;
		if(a != null) {
			if(a.isPresent()) {
				admission = a.get();
			}
		}
		if(c != null) {
			if(c.isPresent()) {
				consultation = c.get();
			}
		}
		
		ReferralPlan referralPlan = new ReferralPlan();
		Optional<ReferralPlan> p = null;
		if(admission != null) {
			p = referralPlanRepository.findByAdmissionAndStatus(admission, "PENDING");
			if(p.isPresent()) {
				referralPlan = p.get();
			}else {
				admission.setStatus("STOPPED");
				admission = admissionRepository.save(admission);
				referralPlan.setExternalMedicalProvider(prov.get());
				referralPlan.setAdmission(admission);
				referralPlan.setPatient(admission.getPatient());
				referralPlan.setCreatedBy(userService.getUser(request).getId());
				referralPlan.setCreatedOn(dayService.getDay().getId());
				referralPlan.setCreatedAt(dayService.getTimeStamp());
			}
		}else if(consultation != null) {
			p = referralPlanRepository.findByConsultationAndStatus(consultation, "PENDING");
			if(p.isPresent()) {
				referralPlan = p.get();
			}else {
				List<LabTest> labTests = labTestRepository.findAllByConsultation(consultation);
				for(LabTest test : labTests) {
					if(test.getPatientBill().getStatus() != null) {
						if(test.getPatientBill().getStatus().equals("UNPAID")) {
							throw new InvalidOperationException("Could not save. Patient have uncleared lab test bill(s)");
						}
					}
				}
				
				List<Radiology> radiologies = radiologyRepository.findAllByConsultation(consultation);
				for(Radiology radiology : radiologies) {
					if(radiology.getPatientBill().getStatus() != null) {
						if(radiology.getPatientBill().getStatus().equals("UNPAID")) {
							throw new InvalidOperationException("Could not save. Patient have uncleared radiology test bill(s)");
						}
					}
				}
				
				List<Procedure> procedures = procedureRepository.findAllByConsultation(consultation);
				for(Procedure test : procedures) {
					if(test.getPatientBill().getStatus() != null) {
						if(test.getPatientBill().getStatus().equals("UNPAID")) {
							throw new InvalidOperationException("Could not save. Patient have uncleared procedure bill(s)");
						}
					}
				}
				
				List<Prescription> prescriptions = prescriptionRepository.findAllByConsultation(consultation);
				for(Prescription test : prescriptions) {
					if(test.getPatientBill().getStatus() != null) {
						if(test.getPatientBill().getStatus().equals("UNPAID")) {
							throw new InvalidOperationException("Could not save. Patient have uncleared medication bill(s)");
						}
					}
				}
				
				consultation.setStatus("SIGNED-OUT");
				consultation = consultationRepository.save(consultation);
				
				Patient patient = consultation.getPatient();
				patient.setPaymentType("CASH");
				patient.setInsurancePlan(null);
				patientRepository.save(patient);
				
				referralPlan.setConsultation(consultation);
				referralPlan.setPatient(consultation.getPatient());
				referralPlan.setExternalMedicalProvider(prov.get());
				
				referralPlan.setCreatedBy(userService.getUser(request).getId());
				referralPlan.setCreatedOn(dayService.getDay().getId());
				referralPlan.setCreatedAt(dayService.getTimeStamp());
			}
		}else {
			throw new InvalidOperationException("Patient should either be inpatient or outpatient");
		}
		referralPlan.setReferringDiagnosis(plan.getReferringDiagnosis());
		referralPlan.setExternalMedicalProvider(prov.get());
		referralPlan.setHistory(plan.getHistory());
		referralPlan.setInvestigation(plan.getInvestigation());
		referralPlan.setManagement(plan.getManagement());
		referralPlan.setOperationNote(plan.getOperationNote());
		referralPlan.setIcuAdmissionNote(plan.getIcuAdmissionNote());
		referralPlan.setGeneralRecommendation(plan.getGeneralRecommendation());
		referralPlan.setStatus("PENDING");
		
		referralPlan = referralPlanRepository.save(referralPlan);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_referral_plan").toUriString());
		return ResponseEntity.created(uri).body(referralPlan);
	}
	
	@GetMapping("/patients/load_referral_plan")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<ReferralPlan>getReferralPlan(
			@RequestParam(name = "admission_id") Long admissionId,
			@RequestParam(name = "consultation_id") Long consultationId,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_referral_plan").toUriString());
		Optional<Admission> a = admissionRepository.findById(admissionId);
		Optional<Consultation> c = consultationRepository.findById(consultationId);
		if(a.isPresent()) {
			Optional<ReferralPlan> p = referralPlanRepository.findByAdmissionAndStatus(a.get(), "PENDING");
			if(p.isEmpty()) {
				return ResponseEntity.created(uri).body(null);
			}
			return ResponseEntity.created(uri).body(p.get());
		}else if(c.isPresent()) {
			Optional<ReferralPlan> p = referralPlanRepository.findByConsultationAndStatus(c.get(), "PENDING");
			if(p.isEmpty()) {
				return ResponseEntity.created(uri).body(null);
			}
			return ResponseEntity.created(uri).body(p.get());
		}
		return ResponseEntity.created(uri).body(null);
	}
	
	@GetMapping("/patients/load_referral_list")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<ReferralPlan>>loadReferralList(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("APPROVED");
		
		List<ReferralPlan> plans = referralPlanRepository.findAllByStatusIn(statuses);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_referral_list").toUriString());
		
		return ResponseEntity.created(uri).body(plans);
	}
	
	@GetMapping("/patients/get_referral_summary")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<ReferralPlan>getRefarralSummary(
			@RequestParam(name = "referral_plan_id") Long referralPlanId,
			HttpServletRequest request){
		
		Optional<ReferralPlan> p = referralPlanRepository.findById(referralPlanId);
		if(p.isEmpty()) {
			throw new NotFoundException("Referral plan not found");
		}
		
		Admission adm = p.get().getAdmission();
		Consultation con = p.get().getConsultation();
		
		if(adm != null) {
			List<PatientInvoice> invoices = patientInvoiceRepository.findAllByAdmission(adm);
			for(PatientInvoice invoice : invoices) {
				List<PatientInvoiceDetail> details = invoice.getPatientInvoiceDetails();
				for(PatientInvoiceDetail detail : details) {
					if(detail.getPatientBill().getStatus() != null) {
						if(detail.getPatientBill().getStatus().equals("UNPAID") || detail.getPatientBill().getStatus().equals("VERIFIED")) {
							throw new InvalidOperationException("Could not get discharge summary. Patient have uncleared bills.");
						}
					}
				}
			}
			
			for(PatientInvoice invoice : invoices) {
				invoice.setStatus("APPROVED");
				patientInvoiceRepository.save(invoice);
			}
			
			if(adm.getStatus() != null) {
				if(adm.getStatus().equals("STOPPED")) {
					adm.setStatus("SIGNED-OUT");
					adm.setDischargedBy(p.get().getCreatedBy());
					adm.setDischargedOn(p.get().getCreatedOn());
					adm.setDischargedAt(p.get().getCreatedAt());
					
					adm = admissionRepository.save(adm);
					
					WardBed wardBed = adm.getWardBed(); 
					wardBed.setStatus("EMPTY");
					wardBedRepository.save(wardBed);
					
					Patient patient = adm.getPatient();
					patient.setType("OUTPATIENT");
					patientRepository.save(patient);
					
					p.get().setStatus("APPROVED");
					p.get().setApprovedBy(p.get().getCreatedBy());
					p.get().setApprovedOn(p.get().getCreatedOn());
					p.get().setApprovedAt(dayService.getTimeStamp());	
				}
			}
		}else if(con != null) {
			List<LabTest> labTests = labTestRepository.findAllByConsultation(con);
			for(LabTest test : labTests) {
				if(test.getPatientBill().getStatus() != null) {
					if(test.getPatientBill().getStatus().equals("UNPAID")) {
						throw new InvalidOperationException("Could not save. Patient have uncleared lab test bill(s)");
					}
				}
			}
			
			List<Radiology> radiologies = radiologyRepository.findAllByConsultation(con);
			for(Radiology radiology : radiologies) {
				if(radiology.getPatientBill().getStatus() != null) {
					if(radiology.getPatientBill().getStatus().equals("UNPAID")) {
						throw new InvalidOperationException("Could not save. Patient have uncleared radiology test bill(s)");
					}
				}
			}
			
			List<Procedure> procedures = procedureRepository.findAllByConsultation(con);
			for(Procedure test : procedures) {
				if(test.getPatientBill().getStatus() != null) {
					if(test.getPatientBill().getStatus().equals("UNPAID")) {
						throw new InvalidOperationException("Could not save. Patient have uncleared procedure bill(s)");
					}
				}
			}
			
			List<Prescription> prescriptions = prescriptionRepository.findAllByConsultation(con);
			for(Prescription test : prescriptions) {
				if(test.getPatientBill().getStatus() != null) {
					if(test.getPatientBill().getStatus().equals("UNPAID")) {
						throw new InvalidOperationException("Could not save. Patient have uncleared medication bill(s)");
					}
				}
			}
			
			con.setStatus("SIGNED-OUT");
			con = consultationRepository.save(con);
			
			Patient patient = con.getPatient();
			patient.setType("OUTPATIENT");
			patientRepository.save(patient);
			
			p.get().setStatus("APPROVED");
			
			p.get().setApprovedBy(p.get().getCreatedBy());
			p.get().setApprovedOn(p.get().getCreatedOn());
			p.get().setApprovedAt(dayService.getTimeStamp());
		}else {
			throw new InvalidOperationException("Patient should either be inpatient or outpatient");
		}
		
		ReferralPlan plan = referralPlanRepository.save(p.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_referral_summary").toUriString());
		
		return ResponseEntity.created(uri).body(plan);
	}
	
	@PostMapping("/patients/save_deceased_note")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DeceasedNote>saveDeceasedNote(

			@RequestBody DeceasedNote note, 
			HttpServletRequest request){
		if(note.getPatientSummary().isEmpty() || note.getCauseOfDeath().isEmpty()) {
			throw new InvalidOperationException("Summary and cause of death are missing");
		}
		if(note.getAdmission().getId() == null && note.getConsultation().getId() == null) {
			throw new InvalidOperationException("Patient should be inpatient or outpatioent, but not both");
		}
		if(note.getAdmission().getId() != null && note.getConsultation().getId() != null) {
			throw new InvalidOperationException("Patient should be inpatient or outpatioent, but not both");
		}
		
		DeceasedNote deceasedNote = new DeceasedNote();
		if(note.getAdmission().getId() != null) {
			Optional<Admission> a = admissionRepository.findById(note.getAdmission().getId());
			if(a.isEmpty()) {
				throw new NotFoundException("Admission not found");
			}
			Optional<DeceasedNote> n = deceasedNoteRepository.findByAdmission(a.get());
			if(n.isPresent()) {
				deceasedNote = n.get();
			}else {
				Admission admission = a.get();
				if(admission.getStatus() == null) {
					admission.setStatus("IN-PROCESS");
					admission = admissionRepository.save(admission);
				}
				if(admission.getStatus().equals("IN-PROCESS")) {
					admission.setStatus("HELD");
					admission = admissionRepository.save(admission);
					WardBed wardBed = admission.getWardBed();
					if(wardBed != null) {
						wardBed.setStatus("EMPTY");
						wardBedRepository.save(wardBed);
					}
				}
				
				deceasedNote.setAdmission(admission);
				deceasedNote.setPatient(admission.getPatient());
				
				deceasedNote.setCreatedBy(userService.getUser(request).getId());
				deceasedNote.setCreatedOn(dayService.getDay().getId());
				deceasedNote.setCreatedAt(dayService.getTimeStamp());
			}
			
		}
		if(note.getConsultation().getId() != null) {
			Optional<Consultation> c = consultationRepository.findById(note.getConsultation().getId());
			if(c.isEmpty()) {
				throw new NotFoundException("Consultation not found");
			}
			Optional<DeceasedNote> n = deceasedNoteRepository.findByConsultation(c.get());
			if(n.isPresent()) {
				deceasedNote = n.get();
			}else {
				Consultation consultation = c.get();
				consultation.setStatus("HELD");
				consultation = consultationRepository.save(consultation);
				
				deceasedNote.setConsultation(consultation);
				deceasedNote.setPatient(consultation.getPatient());
				
				deceasedNote.setCreatedBy(userService.getUser(request).getId());
				deceasedNote.setCreatedOn(dayService.getDay().getId());
				deceasedNote.setCreatedAt(dayService.getTimeStamp());
			}
		}		
		deceasedNote.setPatientSummary(note.getPatientSummary());
		deceasedNote.setCauseOfDeath(note.getCauseOfDeath());
		deceasedNote.setStatus("PENDING");
		
		deceasedNote.setDate(note.getDate());
		deceasedNote.setTime(note.getTime());
		
		deceasedNote = deceasedNoteRepository.save(deceasedNote);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/save_deceased_note").toUriString());
		return ResponseEntity.created(uri).body(deceasedNote);
	}
	
	@GetMapping("/patients/load_deceased_note")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DeceasedNote>getDeceasedNote(
			@RequestParam(name = "admission_id") Long admissionId,
			@RequestParam(name = "consultation_id") Long consultationId,
			HttpServletRequest request){
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_deceased_note").toUriString());
		
		if(admissionId == null && consultationId == null) {
			throw new InvalidOperationException("Can only be Outpatient or inpatient");
		}
		if(admissionId != null && consultationId != null) {
			throw new InvalidOperationException("Can only be Outpatient or inpatient, but not both");
		}
		
		if(admissionId != null) {
			Optional<Admission> a = admissionRepository.findById(admissionId);
			if(a.isEmpty()) {
				throw new NotFoundException("Admission not found");
			}
			Optional<DeceasedNote> n = deceasedNoteRepository.findByAdmission(a.get());
			if(n.isEmpty()) {
				return ResponseEntity.created(uri).body(null);
			}else {
				return ResponseEntity.created(uri).body(n.get());
			}
		}
		
		if(consultationId != null) {
			Optional<Consultation> c = consultationRepository.findById(consultationId);
			if(c.isEmpty()) {
				throw new NotFoundException("Consultation not found");
			}
			Optional<DeceasedNote> n = deceasedNoteRepository.findByConsultation(c.get());
			if(n.isEmpty()) {
				return ResponseEntity.created(uri).body(null);
			}else {
				return ResponseEntity.created(uri).body(n.get());
			}
		}
		return ResponseEntity.created(uri).body(null);
	}
	
	@GetMapping("/patients/load_deceased_list")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<List<DeceasedNote>>loadDeceasedList(
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("APPROVED");
		
		List<DeceasedNote> notes = deceasedNoteRepository.findAllByStatusIn(statuses);
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/load_deceased_list").toUriString());
		
		return ResponseEntity.created(uri).body(notes);
	}
	
	@GetMapping("/patients/get_deceased_summary")
	//@PreAuthorize("hasAnyAuthority('PATIENT-A','PATIENT-C','PATIENT-U')")
	public ResponseEntity<DeceasedNote>getDeceasedSummary(
			@RequestParam(name = "deceased_note_id") Long deceasedNoteId,
			HttpServletRequest request){
		
		Optional<DeceasedNote> n = deceasedNoteRepository.findById(deceasedNoteId);
		if(n.isEmpty()) {
			throw new NotFoundException("Deceased note not found");
		}
		
		Admission adm = n.get().getAdmission();
		Consultation con = n.get().getConsultation();
		
		List<PatientInvoice> admissionInvoices = patientInvoiceRepository.findAllByAdmission(adm);
		if(adm != null) {
			for(PatientInvoice invoice : admissionInvoices) {
				List<PatientInvoiceDetail> details = invoice.getPatientInvoiceDetails();
				for(PatientInvoiceDetail detail : details) {
					if(detail.getPatientBill().getStatus() != null) {
						if(detail.getPatientBill().getStatus().equals("UNPAID") || detail.getPatientBill().getStatus().equals("VERIFIED")) {
							throw new InvalidOperationException("Could not get deceased summary. Patient have uncleared bills.");
						}
					}
				}
			}
		}
		
		for(PatientInvoice invoice : admissionInvoices) {
			invoice.setStatus("APPROVED");
			patientInvoiceRepository.save(invoice);
		}
		
		List<PatientInvoice> consultationInvoices = patientInvoiceRepository.findAllByConsultation(con);
		if(con != null) {
			for(PatientInvoice invoice : consultationInvoices) {
				List<PatientInvoiceDetail> details = invoice.getPatientInvoiceDetails();
				for(PatientInvoiceDetail detail : details) {
					if(detail.getPatientBill().getStatus() != null) {
						if(detail.getPatientBill().getStatus().equals("UNPAID") || detail.getPatientBill().getStatus().equals("VERIFIED")) {
							throw new InvalidOperationException("Could not get deceased summary. Patient have uncleared bills.");
						}
					}
				}
			}
		}
		
		for(PatientInvoice invoice : consultationInvoices) {
			invoice.setStatus("APPROVED");
			patientInvoiceRepository.save(invoice);
		}
		
		if(adm != null) {
			if(adm.getStatus() != null) {
				if(adm.getStatus().equals("HELD")) {
					adm.setStatus("SIGNED-OUT");
					
					adm = admissionRepository.save(adm);
					
					WardBed wardBed = adm.getWardBed(); 
					wardBed.setStatus("EMPTY");
					wardBedRepository.save(wardBed);
					
					Patient patient = adm.getPatient();
					patient.setType("DECEASED");
					patientRepository.save(patient);
					
					n.get().setStatus("APPROVED");
					n.get().setApprovedBy(n.get().getCreatedBy());
					n.get().setApprovedOn(n.get().getCreatedOn());
					n.get().setApprovedAt(dayService.getTimeStamp());	
				}
			}
		}
		
		if(con != null) {
			if(con.getStatus() != null) {
				if(con.getStatus().equals("HELD")) {
					con.setStatus("SIGNED-OUT");
					
					con = consultationRepository.save(con);
					
					Patient patient = con.getPatient();
					patient.setType("DECEASED");
					patientRepository.save(patient);
					
					n.get().setStatus("APPROVED");
					n.get().setApprovedBy(n.get().getCreatedBy());
					n.get().setApprovedOn(n.get().getCreatedOn());
					n.get().setApprovedAt(dayService.getTimeStamp());	
				}
			}
		}
		
		DeceasedNote note = deceasedNoteRepository.save(n.get());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/get_deceased_summary").toUriString());
		
		return ResponseEntity.created(uri).body(note);
	}
	
	
	
	
	@PostMapping("/patients/upload_lab_test_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<ResponseEntity<Map<String, String>>> saveLabTestAttachment(
			@RequestPart("file") MultipartFile file,
			@RequestParam(name = "lab_test_id") Long labTestId,
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		Optional<LabTest> t = labTestRepository.findById(labTestId);
		
		if(t.isEmpty()) {
			throw new NotFoundException("Lab test not found");
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/upload_lab_test_attachment").toUriString());
		return ResponseEntity.created(uri).body(patientService.saveLabTestAttachment(t.get(), file, name, request));
	}
	
	
	
	@GetMapping("/patients/download_lab_test_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public void getLabTestAttachment(
			@RequestParam(name = "file_name") String fileName,
			HttpServletResponse response,
			HttpServletRequest request) throws ResourceNotFoundException, IOException {
		
		List<CompanyProfile> comps = companyProfileRepository.findAll();
	    CompanyProfile companyProfile = null;
	    for(CompanyProfile comp : comps) {
	    	companyProfile = comp;
	    }
	    
	  if(companyProfile == null) {
    	  throw new NotFoundException("Company Profile not found");
      }
      if(companyProfile.getPublicPath() == null) {
    	  throw new NotFoundException("Driver not found. Contact System Administrator");
      }
      if(companyProfile.getPublicPath().equals("")) {
    	  throw new NotFoundException("Driver not found. Contact System Administrator");
      }
      
      final Path path = Paths.get(companyProfile.getPublicPath());
      
      String fileLocation = companyProfile.getPublicPath();
      
      File downloadFile= new File(fileLocation + fileName);

      byte[] isr = Files.readAllBytes(downloadFile.toPath());
      ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
      out.write(isr, 0, isr.length);

      response.setContentType("application/*");
      // Use 'inline' for preview and 'attachement' for download in browser.
      response.addHeader("Content-Disposition", "inline; filename=" + fileName);

      OutputStream os = null;
      try {
          os = response.getOutputStream();
          out.writeTo(os);
          os.flush();
          os.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
      
	}
	
	
	@PostMapping("/patients/delete_lab_test_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public void deleteLabTestAttachment(
			@RequestParam(name = "attachment_id") Long attachmentId,
			HttpServletResponse response,
			HttpServletRequest request) throws ResourceNotFoundException, IOException {
		
		Optional<LabTestAttachment> labTestAttachment = labTestAttachmentRepository.findById(attachmentId);
		if(labTestAttachment.isEmpty()) {
			throw new NotFoundException("Attachment not found");
		}else {
			if(labTestAttachment.get().getLabTest().getStatus().equals("VERIFIED")) {
				throw new InvalidOperationException("Could not delete. Lab Test already verified");
			}
			labTestAttachmentRepository.deleteById(attachmentId);
		}
		
		
		//List<CompanyProfile> comps = companyProfileRepository.findAll();
	    //CompanyProfile companyProfile = null;
	    //for(CompanyProfile comp : comps) {
	    	//companyProfile = comp;
	    //}
	    
	  //if(companyProfile == null) {
    	  //throw new NotFoundException("Company Profile not found");
      //}
      //if(companyProfile.getPublicPath() == null) {
    	  //throw new NotFoundException("Driver not found. Contact Administrator");
      //}
      //if(companyProfile.getPublicPath().equals("")) {
    	  //throw new NotFoundException("Driver not found. Contact System Administrator");
      //}
      
      //final Path path = Paths.get(companyProfile.getPublicPath());
      
      //String fileLocation = companyProfile.getPublicPath();
      
      //File downloadFile= new File(fileLocation + fileName);

      //byte[] isr = Files.readAllBytes(downloadFile.toPath());
      //ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
      //out.write(isr, 0, isr.length);

      //response.setContentType("application/*");
     // // Use 'inline' for preview and 'attachement' for download in browser.
      //response.addHeader("Content-Disposition", "inline; filename=" + fileName);

      //OutputStream os = null;
      //try {
          //os = response.getOutputStream();
          //out.writeTo(os);
          //os.flush();
          //os.close();
      //} catch (IOException e) {
          //e.printStackTrace();
      //}
      
	}
	
	
	
	
	
	@PostMapping("/patients/upload_radiology_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public ResponseEntity<ResponseEntity<Map<String, String>>> saveRadiologyAttachment(
			@RequestPart("file") MultipartFile file,
			@RequestParam(name = "radiology_id") Long radiologyId,
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		Optional<Radiology> r = radiologyRepository.findById(radiologyId);
		
		if(r.isEmpty()) {
			throw new NotFoundException("Radiology not found");
		}
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/patients/upload_radiology_attachment").toUriString());
		return ResponseEntity.created(uri).body(patientService.saveRadiologyAttachment(r.get(), file, name, request));
	}
	
	
	
	@GetMapping("/patients/download_radiology_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public void getRadiologyAttachment(
			@RequestParam(name = "file_name") String fileName,
			HttpServletResponse response,
			HttpServletRequest request) throws ResourceNotFoundException, IOException {
		
		List<CompanyProfile> comps = companyProfileRepository.findAll();
	    CompanyProfile companyProfile = null;
	    for(CompanyProfile comp : comps) {
	    	companyProfile = comp;
	    }
	    
	  if(companyProfile == null) {
    	  throw new NotFoundException("Company Profile not found");
      }
      if(companyProfile.getPublicPath() == null) {
    	  throw new NotFoundException("Driver not found. Contact System Administrator");
      }
      if(companyProfile.getPublicPath().equals("")) {
    	  throw new NotFoundException("Driver not found. Contact System Administrator");
      }
      
      final Path path = Paths.get(companyProfile.getPublicPath());
      
      String fileLocation = companyProfile.getPublicPath();
      
      File downloadFile= new File(fileLocation + fileName);

      byte[] isr = Files.readAllBytes(downloadFile.toPath());
      ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
      out.write(isr, 0, isr.length);

      response.setContentType("application/*");
      // Use 'inline' for preview and 'attachement' for download in browser.
      response.addHeader("Content-Disposition", "inline; filename=" + fileName);

      OutputStream os = null;
      try {
          os = response.getOutputStream();
          out.writeTo(os);
          os.flush();
          os.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
      
	}
	
	
	@PostMapping("/patients/delete_radiology_attachment")
	//@PreAuthorize("hasAnyAuthority('PRODUCT-CREATE')")
	public void deleteRadiologyAttachment(
			@RequestParam(name = "attachment_id") Long attachmentId,
			HttpServletResponse response,
			HttpServletRequest request) throws ResourceNotFoundException, IOException {
		
		Optional<RadiologyAttachment> radiologyAttachment = radiologyAttachmentRepository.findById(attachmentId);
		if(radiologyAttachment.isEmpty()) {
			throw new NotFoundException("Attachment not found");
		}else {
			if(radiologyAttachment.get().getRadiology().getStatus().equals("VERIFIED")) {
				throw new InvalidOperationException("Could not delete. Radiology already verified");
			}
			radiologyAttachmentRepository.deleteById(attachmentId);
		}
		
		
		//List<CompanyProfile> comps = companyProfileRepository.findAll();
	    //CompanyProfile companyProfile = null;
	    //for(CompanyProfile comp : comps) {
	    	//companyProfile = comp;
	    //}
	    
	  //if(companyProfile == null) {
    	  //throw new NotFoundException("Company Profile not found");
      //}
      //if(companyProfile.getPublicPath() == null) {
    	  //throw new NotFoundException("Driver not found. Contact Administrator");
      //}
      //if(companyProfile.getPublicPath().equals("")) {
    	  //throw new NotFoundException("Driver not found. Contact System Administrator");
      //}
      
      //final Path path = Paths.get(companyProfile.getPublicPath());
      
      //String fileLocation = companyProfile.getPublicPath();
      
      //File downloadFile= new File(fileLocation + fileName);

      //byte[] isr = Files.readAllBytes(downloadFile.toPath());
      //ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
      //out.write(isr, 0, isr.length);

      //response.setContentType("application/*");
     // // Use 'inline' for preview and 'attachement' for download in browser.
      //response.addHeader("Content-Disposition", "inline; filename=" + fileName);

      //OutputStream os = null;
      //try {
          //os = response.getOutputStream();
          //out.writeTo(os);
          //os.flush();
          //os.close();
      //} catch (IOException e) {
          //e.printStackTrace();
      //}
      
	}
	
	
}

@Data
class LAdmission{
	private Patient patient;
	private WardBed wardBed;
}

@Data
class CG {
	private ClinicalNote clinicalNote;
	private GeneralExamination generalExamination;
}

@Data
class LLabTest {
	private Long id;
	private String result;
	private String range;
	private String level;
	private String unit;
	
}
@Data
class LRadiology {
	private Long id;
	private String result;
	private String diagnosis;
	private String description;
	private Byte[] attachment;
	private DiagnosisType diagnosisType;
}

@Data
class LProcedure {
	private Long id;
	private String note;
}

