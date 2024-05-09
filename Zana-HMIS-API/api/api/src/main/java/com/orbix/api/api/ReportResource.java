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
import com.orbix.api.domain.DiagnosisType;
import com.orbix.api.domain.GoodsReceivedNote;
import com.orbix.api.domain.GoodsReceivedNoteDetail;
import com.orbix.api.domain.InsurancePlan;
import com.orbix.api.domain.Item;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.LabTestType;
import com.orbix.api.domain.LocalPurchaseOrder;
import com.orbix.api.domain.LocalPurchaseOrderDetail;
import com.orbix.api.domain.Medicine;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientInvoiceDetail;
import com.orbix.api.domain.PharmacyStockCard;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.RadiologyType;
import com.orbix.api.domain.Registration;
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.User;
import com.orbix.api.domain.Visit;
import com.orbix.api.domain.WardBed;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.AdmissionBedCollectionModel;
import com.orbix.api.models.ConsultationCollectionModel;
import com.orbix.api.models.LabTestCollectionModel;
import com.orbix.api.models.LabTestModel;
import com.orbix.api.models.PharmacyStockCardModel;
import com.orbix.api.models.PrescriptionCollectionModel;
import com.orbix.api.models.PrescriptionModel;
import com.orbix.api.models.ProcedureCollectionModel;
import com.orbix.api.models.RadiologyCollectionModel;
import com.orbix.api.models.RegistrationCollectionModel;
import com.orbix.api.models.StoreStockCardModel;
import com.orbix.api.reports.FastMovingDrugs;
import com.orbix.api.reports.models.ClinicianPerformanceReport;
import com.orbix.api.reports.models.CollectionReport;
import com.orbix.api.reports.models.GrnReport;
import com.orbix.api.reports.models.LabTestTypeReport;
import com.orbix.api.reports.models.LpoReport;
import com.orbix.api.reports.service.LocalPurchaseOrderReportService;
import com.orbix.api.repositories.AdmissionBedRepository;
import com.orbix.api.repositories.AdmissionRepository;
import com.orbix.api.repositories.ClinicianPerformanceRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.CollectionRepository;
import com.orbix.api.repositories.ConsultationRepository;
import com.orbix.api.repositories.GoodsReceivedNoteRepository;
import com.orbix.api.repositories.LabTestRepository;
import com.orbix.api.repositories.LabTestTypeRepository;
import com.orbix.api.repositories.LocalPurchaseOrderRepository;
import com.orbix.api.repositories.MedicineRepository;
import com.orbix.api.repositories.PatientBillRepository;
import com.orbix.api.repositories.PatientInvoiceDetailRepository;
import com.orbix.api.repositories.PatientRepository;
import com.orbix.api.repositories.PharmacyStockCardRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.repositories.ProcedureRepository;
import com.orbix.api.repositories.RadiologyRepository;
import com.orbix.api.repositories.RegistrationRepository;
import com.orbix.api.repositories.StoreStockCardRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.repositories.VisitRepository;
import com.orbix.api.repositories.WardBedRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author GODFREY
 *
 */
@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class ReportResource {
	
	private final UserService userService;
	private final DayService dayService;
	private final UserRepository userRepository;
	
	private final ConsultationRepository consultationRepository;
	private final RegistrationRepository registrationRepository;
	private final ProcedureRepository procedureRepository;
	private final RadiologyRepository radiologyRepository;
	private final ClinicianRepository clinicianRepository;
	private final LabTestRepository labTestRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	private final PatientRepository patientRepository;
	private final PatientBillRepository patientBillRepository;
	private final LabTestTypeRepository labTestTypeRepository;
	
	private final AdmissionBedRepository admissionBedRepository;
	private final AdmissionRepository admissionRepository;
	
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	private final GoodsReceivedNoteRepository goodsReceivedNoteRepository;
	
	private final PrescriptionRepository prescriptionRepository;
	private final MedicineRepository medicineRepository;
	
	private final StoreStockCardRepository storeStockCardRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	
	private final CollectionRepository collectionRepository;
	private final VisitRepository visitRepository;
	
	private final ClinicianPerformanceRepository clinicianPerformanceRepository;
	
	@PostMapping("/reports/consultation_report")
	public ResponseEntity<List<Consultation>>getConsultationReport(
			@RequestBody ConsultationReportArgs args,
			HttpServletRequest request){
		
		Optional<Clinician> c = clinicianRepository.findById(args.getClinician().getId());
		if(c.isEmpty()) {
			throw new NotFoundException("Clinician not found");
		}
		
		List<Consultation> consultations = consultationRepository.findAllByClinicianAndCreatedAtBetween(c.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		
		for(Consultation consultation : consultations) {
			
			PatientBill patientBill = consultation.getPatientBill();
			Optional<PatientInvoiceDetail> pid = patientInvoiceDetailRepository.findByPatientBill(patientBill);
			if(pid.isPresent()) {
				consultation.setInsurancePlan(pid.get().getPatientInvoice().getInsurancePlan());
			}else {
				consultation.setInsurancePlan(null);
			}
		}
		
		return ResponseEntity.ok().body(consultations);
	}
	
	@PostMapping("/reports/procedure_report")
	public ResponseEntity<List<Procedure>>getProcedureReport(
			@RequestBody ProcedureReportArgs args,
			HttpServletRequest request){
		
		Optional<Clinician> c = clinicianRepository.findById(args.getClinician().getId());
		if(c.isEmpty()) {
			throw new NotFoundException("Clinician not found");
		}
		
		List<Procedure> procedures = procedureRepository.findAllByClinicianAndCreatedAtBetween(c.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		for(Procedure procedure : procedures) {
			
			PatientBill patientBill = procedure.getPatientBill();
			Optional<PatientInvoiceDetail> pid = patientInvoiceDetailRepository.findByPatientBill(patientBill);
			if(pid.isPresent()) {
				procedure.setInsurancePlan(pid.get().getPatientInvoice().getInsurancePlan());
			}else {
				procedure.setInsurancePlan(null);
			}
		}
		
		return ResponseEntity.ok().body(procedures);
	}
	
	@PostMapping("/reports/prescription_report")
	public ResponseEntity<List<PrescriptionModel>>getPrescriptionReport(
			@RequestBody PrescriptionReportArgs args,
			HttpServletRequest request){
		
		Optional<Medicine> tt = medicineRepository.findById(args.getMedicine().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Medicine not found");
		}
		List<String> statuses = new ArrayList<>();
		statuses.add("GIVEN");
		
		List<Prescription> prescriptions = prescriptionRepository.findAllByMedicineAndCreatedAtBetweenAndStatusIn(tt.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1), statuses);
		
		List<PrescriptionModel> prescriptionModels = new ArrayList<>();
		for(Prescription prescription : prescriptions) {
			PrescriptionModel prescriptionModel = new PrescriptionModel();
			prescriptionModel.setId(prescription.getId());
			
			if(prescription.getAcceptedAt() != null) {
				prescriptionModel.setAccepted(prescription.getAcceptedAt().toString()+" | "+userService.getUserById(prescription.getAcceptedby()).getNickname());
			}else {
				prescriptionModel.setAccepted("");
			}
			
			prescriptionModel.setAdmission(prescription.getAdmission());
			if(prescription.getApprovedAt() != null) {
				prescriptionModel.setApproved(prescription.getApprovedAt().toString()+" | "+userService.getUserById(prescription.getApprovedBy()).getNickname());
			}else {
				prescriptionModel.setApproved("");
			}
			if(prescription.getCreatedAt() != null) {
				prescriptionModel.setCreated(prescription.getCreatedAt().toString()+" | "+userService.getUserById(prescription.getCreatedBy()).getNickname());
			}else {
				prescriptionModel.setCreated("");
			}
			prescriptionModel.setBalance(prescription.getBalance());
			prescriptionModel.setConsultation(prescription.getConsultation());
			prescriptionModel.setDays(prescription.getDays());
			prescriptionModel.setDosage(prescription.getDosage());
			prescriptionModel.setFrequency(prescription.getDosage());
			//prescriptionModel.setHeld(held);
			prescriptionModel.setIssued(prescription.getIssued());
			prescriptionModel.setMedicine(prescription.getMedicine());
			prescriptionModel.setNonConsultation(prescription.getNonConsultation());
			prescriptionModel.setAdmission(prescription.getAdmission());
			//prescriptionModel.setOrdered(ordered);
			prescriptionModel.setPatient(prescription.getPatient());
			prescriptionModel.setQty(prescription.getQty());
			prescriptionModel.setReference(prescription.getReference());
			prescriptionModel.setRejectComment(prescription.getRejectComment());
			//prescriptionModel.setRejected(rejected);
			prescriptionModel.setRoute(prescription.getRoute());
			prescriptionModel.setStatus(prescription.getStatus());
			//prescriptionModel.setStock(stock);
			//prescriptionModel.setVerified(verified);
			prescriptionModel.setIssuePharmacy(prescription.getIssuePharmacy());
			
			prescriptionModels.add(prescriptionModel);
		}
		
		return ResponseEntity.ok().body(prescriptionModels);
	}
	
	@PostMapping("/reports/fast_moving_drugs_report")
	public List<FastMovingDrugs> getFastMovingDrugsReport(
			@RequestBody PrescriptionReportArgs args,
			HttpServletRequest request){
		
		return prescriptionRepository.getFastMovingDrugs(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
	}
	
	@PostMapping("/reports/slow_moving_drugs_report")
	public List<FastMovingDrugs> getSlowMovingDrugsReport(
			@RequestBody PrescriptionReportArgs args,
			HttpServletRequest request){
		
		return prescriptionRepository.getSlowMovingDrugs(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
	}
	
	@PostMapping("/reports/lab_test_report")
	public ResponseEntity<List<LabTest>>getLabTestReport(
			@RequestBody LabTestReportArgs args,
			HttpServletRequest request){
		
		Optional<LabTestType> tt = labTestTypeRepository.findById(args.getLabTestType().getId());
		if(tt.isEmpty()) {
			throw new NotFoundException("Lab Test Type not found");
		}
		
		List<LabTest> labTests = labTestRepository.findAllByLabTestTypeAndCreatedAtBetween(tt.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		return ResponseEntity.ok().body(labTests);
	}
	
	@PostMapping("/reports/get_lab_tests_by_date")
	public ResponseEntity<List<LabTestModel>>getLabTestByDateCheckLaater(
			@RequestBody LabTestReportArgs args,
			HttpServletRequest request){
		
		Optional<Patient> p = patientRepository.findById(args.getPatient().getId());
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found");
		}
		
		List<String> statuses = new ArrayList<>();
		statuses.add("VERIFIED");
		
		List<LabTest> labTests = labTestRepository.findAllByPatientAndStatusInAndCreatedAtBetween(p.get(), statuses, args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		List<LabTestModel> models = new ArrayList<>();
		for(LabTest l : labTests) {
			LabTestModel model = new LabTestModel();
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
			model.setPatient(l.getPatient());

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
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/lab_test_statistics_report")
	public ResponseEntity<List<LabTest>>getLabTestStatisticsReport(
			@RequestBody LabTestReportArgs args,
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("VERIFIED");
		
		List<LabTest> labTests = labTestRepository.findAllByStatusInAndCreatedAtBetween(statuses, args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		return ResponseEntity.ok().body(labTests);
	}
	
	@PostMapping("/reports/lab_sample_collection_report")
	public ResponseEntity<List<LabTestModel>>getLabSampleCollectionReport(
			@RequestBody LabTestTypeReportArgs args,
			HttpServletRequest request){
		
		List<LabTest> labTests = labTestRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<LabTestModel> models = new ArrayList<>();
		for(LabTest l : labTests) {
			LabTestModel model = new LabTestModel();
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
			model.setPatient(l.getPatient());

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
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/doctor_to_radiology_report")
	public ResponseEntity<List<Radiology>>getDoctorToRadiologyReport(
			@RequestBody RadiologyReportArgs args,
			HttpServletRequest request){
		
		Optional<Clinician> c = clinicianRepository.findById(args.getClinician().getId());
		if(c.isEmpty()) {
			throw new NotFoundException("Clinician not found");
		}
		
		List<Radiology> radiologies = radiologyRepository.findAllByClinicianAndCreatedAtBetween(c.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<Radiology> doctorRadiologies = new ArrayList<>();
		
		for(Radiology radiology : radiologies) {
			User user = userRepository.findById(radiology.getCreatedBy()).get();
			Optional<Clinician> cl = clinicianRepository.findByUser(user);
			
			PatientBill patientBill = radiology.getPatientBill();
			Optional<PatientInvoiceDetail> pid = patientInvoiceDetailRepository.findByPatientBill(patientBill);
			if(pid.isPresent()) {
				radiology.setInsurancePlan(pid.get().getPatientInvoice().getInsurancePlan());
			}else {
				radiology.setInsurancePlan(null);
			}
			
			
			if(cl.isPresent()) {
				doctorRadiologies.add(radiology); //deactivate this snippet later, activate the below
			}
			
			if(radiology.getClinician() != null) {
				//doctorRadiologies.add(radiology); activate this snippet latter, deactivete the above
			}			
		}
		
		return ResponseEntity.ok().body(doctorRadiologies);
	}
	
	@PostMapping("/reports/doctor_to_laboratory_report")
	public ResponseEntity<List<LabTest>>getDoctorTolabReport(
			@RequestBody LabTestReportArgs args,
			HttpServletRequest request){
		
		Optional<Clinician> c = clinicianRepository.findById(args.getClinician().getId());
		if(c.isEmpty()) {
			throw new NotFoundException("Clinician not found");
		}
		
		List<LabTest> labTests = labTestRepository.findAllByClinicianAndCreatedAtBetween(c.get(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<LabTest> doctorLabTests = new ArrayList<>();
		
		for(LabTest labTest : labTests) {
			User user = userRepository.findById(labTest.getCreatedBy()).get();
			Optional<Clinician> cl = clinicianRepository.findByUser(user);
			
			PatientBill patientBill = labTest.getPatientBill();
			Optional<PatientInvoiceDetail> pid = patientInvoiceDetailRepository.findByPatientBill(patientBill);
			if(pid.isPresent()) {
				labTest.setInsurancePlan(pid.get().getPatientInvoice().getInsurancePlan());
			}else {
				labTest.setInsurancePlan(null);
			}
			
			if(cl.isPresent()) {
				doctorLabTests.add(labTest); //deactivate this snippet later, activate the below
			}
			
		}
		
		return ResponseEntity.ok().body(doctorLabTests);
	}
	
	@PostMapping("/reports/get_patient_bills_by_date")
	public ResponseEntity<List<PatientBill>> getPatientBillsByDate(
			@RequestBody PatientBillReportArgs args,
			HttpServletRequest request){	
		Optional<Patient> p = patientRepository.findById(args.getPatient().getId());
		List<String> statuses = new ArrayList<>();
		statuses.add("PAID");
		statuses.add("UNPAID");
		statuses.add("COVERED");
		statuses.add("VERIFIED");
		if(p.isEmpty()) {
			throw new NotFoundException("Patient not found");
		}
		
		List<PatientBill> bills = new ArrayList<>();
		
		bills = patientBillRepository.findAllByPatientAndCreatedAtBetweenAndStatusIn(p.get(), args.from.atStartOfDay(), args.to.atStartOfDay().plusDays(1), statuses);
		
		
		
		return ResponseEntity.ok().body(bills);
	}
	
	@PostMapping("/reports/local_purchase_order_report")
	public ResponseEntity<List<LocalPurchaseOrderDetail>> lpoReport(
			@RequestBody LocalPurchaseOrderReportArgs args){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("SUBMITTED");
		statuses.add("RECEIVED");
		statuses.add("ARCHIVED");
		List<LocalPurchaseOrder> localPurchaseOrders = localPurchaseOrderRepository.findAllByApprovedAtBetweenAndStatusIn(args.from.atStartOfDay(), args.to.atStartOfDay().plusDays(1), statuses);
		List<LocalPurchaseOrderDetail> localPurchaseOrderDetails = new ArrayList<>();
		for(LocalPurchaseOrder order : localPurchaseOrders) {
			for(LocalPurchaseOrderDetail detail : order.getLocalPurchaseOrderDetails()) {
				localPurchaseOrderDetails.add(detail);
			}
		}
		return ResponseEntity.ok().body(localPurchaseOrderDetails);
	}
	
	@PostMapping("/reports/goods_received_note_report")
	public ResponseEntity<List<GoodsReceivedNoteDetail>> grnReport(
			@RequestBody GoodsReceivedNoteReportArgs args){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("APPROVED");
		statuses.add("RECEIVED");
		statuses.add("ARCHIVED");
		List<GoodsReceivedNote> goodsReceivedNotes = goodsReceivedNoteRepository.findAllByApprovedAtBetweenAndStatusIn(args.from.atStartOfDay(), args.to.atStartOfDay().plusDays(1), statuses);
		List<GoodsReceivedNoteDetail> goodsReceivedNoteDetails = new ArrayList<>();
		for(GoodsReceivedNote note : goodsReceivedNotes) {
			for(GoodsReceivedNoteDetail detail : note.getGoodsReceivedNoteDetails()) {
				goodsReceivedNoteDetails.add(detail);
			}
		}
		return ResponseEntity.ok().body(goodsReceivedNoteDetails);
	}
	
	@PostMapping("/reports/store_stock_card_report")
	public ResponseEntity<List<StoreStockCardModel>>getStoreStocCardReport(
			@RequestBody StoreStockCardReportArgs args,
			HttpServletRequest request){
		
		List<StoreStockCard> storeStockCards = storeStockCardRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<StoreStockCardModel> storeStockCardModels = new ArrayList<>();
		
		for(StoreStockCard storeStockCard : storeStockCards) {
			StoreStockCardModel storeStockCardModel = new StoreStockCardModel();
			storeStockCardModel.setId(storeStockCard.getId());
			storeStockCardModel.setItem(storeStockCard.getItem());
			storeStockCardModel.setStore(storeStockCard.getStore());
			storeStockCardModel.setQtyIn(storeStockCard.getQtyIn());
			storeStockCardModel.setQtyOut(storeStockCard.getQtyOut());
			storeStockCardModel.setBalance(storeStockCard.getBalance());
			storeStockCardModel.setReference(storeStockCard.getReference());
			
			if(storeStockCard.getCreatedAt() != null) {
				storeStockCardModel.setCreated(storeStockCard.getCreatedAt().toString()+" | "+userService.getUserById(storeStockCard.getCreatedBy()).getNickname());
			}else {
				storeStockCardModel.setCreated("");
			}
			
			storeStockCardModels.add(storeStockCardModel);
		}
		
		return ResponseEntity.ok().body(storeStockCardModels);
	}
	
	@PostMapping("/reports/pharmacy_stock_card_report")
	public ResponseEntity<List<PharmacyStockCardModel>>getPharmacyStocCardReport(
			@RequestBody PharmacyStockCardReportArgs args,
			HttpServletRequest request){
		
		List<PharmacyStockCard> pharmacyStockCards = pharmacyStockCardRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<PharmacyStockCardModel> pharmacyStockCardModels = new ArrayList<>();
		
		for(PharmacyStockCard pharmacyStockCard : pharmacyStockCards) {
			PharmacyStockCardModel pharmacyStockCardModel = new PharmacyStockCardModel();
			pharmacyStockCardModel.setId(pharmacyStockCard.getId());
			pharmacyStockCardModel.setMedicine(pharmacyStockCard.getMedicine());
			pharmacyStockCardModel.setPharmacy(pharmacyStockCard.getPharmacy());
			pharmacyStockCardModel.setQtyIn(pharmacyStockCard.getQtyIn());
			pharmacyStockCardModel.setQtyOut(pharmacyStockCard.getQtyOut());
			pharmacyStockCardModel.setBalance(pharmacyStockCard.getBalance());
			pharmacyStockCardModel.setReference(pharmacyStockCard.getReference());
			
			if(pharmacyStockCard.getCreatedAt() != null) {
				pharmacyStockCardModel.setCreated(pharmacyStockCard.getCreatedAt().toString()+" | "+userService.getUserById(pharmacyStockCard.getCreatedBy()).getNickname());
			}else {
				pharmacyStockCardModel.setCreated("");
			}
			
			pharmacyStockCardModels.add(pharmacyStockCardModel);
		}
		
		return ResponseEntity.ok().body(pharmacyStockCardModels);
	}	
	
	@PostMapping("/reports/collections_report")
	public ResponseEntity<List<CollectionReport>>getCollectionReportReport(
			@RequestBody CollectionReportArgs args,
			HttpServletRequest request){
		
		if(!args.user.getNickname().equals("")) {
			return ResponseEntity.ok().body(collectionRepository.getCollectionReportByCashier(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1), args.user.getNickname()));
		}else {
			return ResponseEntity.ok().body(collectionRepository.getCollectionReportGeneral(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1)));
		}
	}
	
	
	@PostMapping("/reports/lab_test_collection_report")
	public ResponseEntity<List<LabTestCollectionModel>>getLabTestCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<LabTest> labTests = labTestRepository.findAllByPatientBillIn(bills);
		
		List<LabTestCollectionModel> models = new ArrayList<>();
		for(LabTest l : labTests) {
			
			LabTestCollectionModel model = new LabTestCollectionModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setLabTestType(l.getLabTestType());
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/radiology_collection_report")
	public ResponseEntity<List<RadiologyCollectionModel>>getRadiologyCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<Radiology> radiologies = radiologyRepository.findAllByPatientBillIn(bills);
		
		List<RadiologyCollectionModel> models = new ArrayList<>();
		for(Radiology l : radiologies) {
			
			RadiologyCollectionModel model = new RadiologyCollectionModel();
			model.setId(l.getId());
			model.setDescription(l.getDescription());
			model.setRadiologyType(l.getRadiologyType());
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/procedure_collection_report")
	public ResponseEntity<List<ProcedureCollectionModel>>getProcedureCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<Procedure> procedures = procedureRepository.findAllByPatientBillIn(bills);
		
		List<ProcedureCollectionModel> models = new ArrayList<>();
		for(Procedure l : procedures) {
			
			ProcedureCollectionModel model = new ProcedureCollectionModel();
			model.setId(l.getId());
			model.setDescription(l.getType());
			model.setProcedureType(l.getProcedureType());
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/prescription_collection_report")
	public ResponseEntity<List<PrescriptionCollectionModel>>getMediationCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<Prescription> prescriptions = prescriptionRepository.findAllByPatientBillIn(bills);
		
		List<PrescriptionCollectionModel> models = new ArrayList<>();
		for(Prescription l : prescriptions) {
			
			PrescriptionCollectionModel model = new PrescriptionCollectionModel();
			model.setId(l.getId());
			model.setDescription(l.getMedicine().getName());
			model.setMedicine(l.getMedicine());
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/admission_bed_collection_report")
	public ResponseEntity<List<AdmissionBedCollectionModel>>getWardBedCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<AdmissionBed> admissionBeds = admissionBedRepository.findAllByPatientBillIn(bills);
		
		List<AdmissionBedCollectionModel> models = new ArrayList<>();
		for(AdmissionBed l : admissionBeds) {
			
			AdmissionBedCollectionModel model = new AdmissionBedCollectionModel();
			model.setId(l.getId());
			model.setDescription("Admission Bed");
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/consultation_collection_report")
	public ResponseEntity<List<ConsultationCollectionModel>>getConsultationCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<Consultation> consultations = consultationRepository.findAllByPatientBillIn(bills);
		
		List<ConsultationCollectionModel> models = new ArrayList<>();
		for(Consultation l : consultations) {
			
			ConsultationCollectionModel model = new ConsultationCollectionModel();
			model.setId(l.getId());
			model.setDescription("Consultation");
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	@PostMapping("/reports/registration_collection_report")
	public ResponseEntity<List<RegistrationCollectionModel>>getRegistrationCollectionReport(
			@RequestBody PatientBillArgs args,
			HttpServletRequest request){
		
		List<Collection> collections = new ArrayList<>();
		if(args.user.getNickname() == null || args.user.getNickname().equals("")) {
			collections = collectionRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}else {
			Optional<User> user_ = userRepository.findByNickname(args.getUser().getNickname());
			collections = collectionRepository.findAllByCreatedByAndCreatedAtBetween(user_.get().getId(), args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		}
		List<PatientBill> bills = new ArrayList<>();
		for(Collection collection : collections) {
			bills.add(collection.getPatientBill());
		}
		
		List<Registration> registrations = registrationRepository.findAllByPatientBillIn(bills);
		
		List<RegistrationCollectionModel> models = new ArrayList<>();
		for(Registration l : registrations) {
			
			RegistrationCollectionModel model = new RegistrationCollectionModel();
			model.setId(l.getId());
			model.setDescription("Registration");
			model.setPatientBill(l.getPatientBill());
			model.setPatient(l.getPatient());
			
			for(Collection collection : collections) {
				if(collection.getPatientBill() != null) {
					if(collection.getPatientBill().getId() == l.getPatientBill().getId()) {
						Optional<User> user_ = userRepository.findById(collection.getCreatedBy());
						model.setCashier(user_.get().getNickname());
						model.setDateTime(collection.getCreatedAt().toString());
					}
				}
				
			}
			
			models.add(model);
		}
		
		return ResponseEntity.ok().body(models);
	}
	
	
	
	
	
	@PostMapping("/reports/new_patients_count_by_dates")
	public int getNewPatientsCountByDates(
			@RequestBody DateRangeArgs args,
			HttpServletRequest request){		
		List<Registration> registrations = registrationRepository.findAllByCreatedAtBetween(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));		
		int count = 0;		
		for(Registration registration : registrations) {
			if(registration.getPatientBill().getStatus().equals("PAID") || registration.getPatientBill().getStatus().equals("VERIFIED") || registration.getPatientBill().getStatus().equals("COVERED")) {
				count = count + 1;
			}
		}		
		return count;
	}
	
	
	@PostMapping("/reports/existing_patients_count_by_dates")
	public int getExistingPatientsCountByDates(
			@RequestBody DateRangeArgs args,
			HttpServletRequest request){		
		List<Visit> visits = visitRepository.findAllBySequenceAndCreatedAtBetween("SUBSEQUENT", args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));		
		int count = visits.size();		
		//for(Consultation consultation : consultations) {
			//if(consultation.getPatientBill().getStatus().equals("PAID") || consultation.getPatientBill().getStatus().equals("VERIFIED") || consultation.getPatientBill().getStatus().equals("COVERED")) {
				//count = count + 1;
			//}
		//}
		// later count consultation by its satatuses, its simple
		return count;
	}
	
	
	@PostMapping("/reports/outpatients_count_by_dates")
	public int getOutpatientsCountByDates(
			@RequestBody DateRangeArgs args,
			HttpServletRequest request){
		
		List<String> statuses = new ArrayList<>();
		statuses.add("PENDING");
		statuses.add("IN-PROCESS");
		statuses.add("SIGNED-OUT");
		
		List<Consultation> consultations = consultationRepository.findAllByStatusInAndCreatedAtBetween(statuses, args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		return consultations.size();
	}
	
	
	@PostMapping("/reports/inpatients_count_by_dates")
	public int getInpatientsCountByDates(
			@RequestBody DateRangeArgs args,
			HttpServletRequest request){
		
		//get discharged/deceased admissions between from and to
		
		List<Admission> dischargedOrDeceasedAdmissions = admissionRepository.findAllByStatusAndDischargedAtBetween("SIGNED-OFF", args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		//get admissions with created date between from and to 
		
		List<Admission> existingAdmissions = admissionRepository.findAllByStatusAndAdmittedAtBetween("IN-PROCESS", args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<Admission> admissions = new ArrayList<>();
		admissions.addAll(dischargedOrDeceasedAdmissions);
		
		for(Admission existingAdmission : existingAdmissions) {
			if(!admissions.contains(existingAdmission)) {
				admissions.add(existingAdmission);
			}
		}
		
		return admissions.size();
	}
	
	
	@PostMapping("/reports/active_users")
	public int getActiveUsers(
			HttpServletRequest request){
		List<User> users = userRepository.findAllByActive(true);
		
		return users.size();
	}
	
	
	@PostMapping("/reports/clinician_performance_by_dates")
	public List<LClinicianPerformanceReport> getClinicianPerformanceOutpatientsByDates(
			@RequestBody DateRangeArgs args,
			HttpServletRequest request){
		List<ClinicianPerformanceReport> outpatientsPerformances = clinicianPerformanceRepository.getClinicianPerformanceOutpatientReport(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		List<ClinicianPerformanceReport> inpatientsPerformances = clinicianPerformanceRepository.getClinicianPerformanceInpatientReport(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1));
		
		List<LClinicianPerformanceReport> performances = new ArrayList<LClinicianPerformanceReport>();
		
		for(ClinicianPerformanceReport clinicianPerformanceReport : outpatientsPerformances) {
			LClinicianPerformanceReport performance = new LClinicianPerformanceReport();
			performance.setName(clinicianPerformanceReport.getName());
			performance.setTotal(clinicianPerformanceReport.getTotal());
			performance.setType("OUTPATIENT");
			performances.add(performance);
		}
		
		for(ClinicianPerformanceReport clinicianPerformanceReport : inpatientsPerformances) {
			LClinicianPerformanceReport performance = new LClinicianPerformanceReport();
			performance.setName(clinicianPerformanceReport.getName());
			performance.setTotal(clinicianPerformanceReport.getTotal());
			performance.setType("INPATIENT");
			performances.add(performance);
		}
		
		return performances;
	}		
}

@Data
class ConsultationReportArgs {
	LocalDate from;
	LocalDate to;
	Clinician clinician;
}

@Data
class ProcedureReportArgs {
	LocalDate from;
	LocalDate to;
	Clinician clinician;
}

@Data
class RadiologyReportArgs {
	LocalDate from;
	LocalDate to;
	Clinician clinician;
}

@Data
class LabTestReportArgs {
	LocalDate from;
	LocalDate to;
	LabTestType labTestType;
	Patient patient;
	Clinician clinician;
}

@Data
class PrescriptionReportArgs {
	LocalDate from;
	LocalDate to;
	Medicine medicine;
	Patient patient;
	Clinician clinician;
}

@Data
class LabTestTypeReportArgs {
	LocalDate from;
	LocalDate to;
}

@Data
class PatientBillReportArgs {
	Patient patient;
	LocalDate from;
	LocalDate to;
}

@Data
class LocalPurchaseOrderReportArgs {
	LocalDate from;
	LocalDate to;
	Supplier supplier;
	List<Item> items;
}

@Data
class GoodsReceivedNoteReportArgs {
	LocalDate from;
	LocalDate to;
	Supplier supplier;
	List<Item> items;
}

@Data
class StoreStockCardReportArgs {
	LocalDate from;
	LocalDate to;
}

@Data
class PharmacyStockCardReportArgs {
	LocalDate from;
	LocalDate to;
}

@Data
class CollectionReportArgs {
	LocalDate from;
	LocalDate to;
	User user;
}

@Data
class PatientBillArgs{
	LocalDate from;
	LocalDate to;
	User user;
}

@Data
class DateRangeArgs{
	LocalDate from;
	LocalDate to;
}

@Data
class LClinicianPerformanceReport{
	String name;
	int total;
	String type;
}
