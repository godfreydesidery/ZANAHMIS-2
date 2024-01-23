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
import com.orbix.api.domain.Clinician;
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
import com.orbix.api.domain.StoreStockCard;
import com.orbix.api.domain.Supplier;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.LabTestModel;
import com.orbix.api.models.PharmacyStockCardModel;
import com.orbix.api.models.PrescriptionModel;
import com.orbix.api.models.StoreStockCardModel;
import com.orbix.api.reports.FastMovingDrugs;
import com.orbix.api.reports.models.CollectionReport;
import com.orbix.api.reports.models.GrnReport;
import com.orbix.api.reports.models.LabTestTypeReport;
import com.orbix.api.reports.models.LpoReport;
import com.orbix.api.reports.service.LocalPurchaseOrderReportService;
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
import com.orbix.api.repositories.StoreStockCardRepository;
import com.orbix.api.repositories.UserRepository;
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
	private final ProcedureRepository procedureRepository;
	private final RadiologyRepository radiologyRepository;
	private final ClinicianRepository clinicianRepository;
	private final LabTestRepository labTestRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	private final PatientRepository patientRepository;
	private final PatientBillRepository patientBillRepository;
	private final LabTestTypeRepository labTestTypeRepository;
	
	private final LocalPurchaseOrderRepository localPurchaseOrderRepository;
	private final GoodsReceivedNoteRepository goodsReceivedNoteRepository;
	
	private final PrescriptionRepository prescriptionRepository;
	private final MedicineRepository medicineRepository;
	
	private final StoreStockCardRepository storeStockCardRepository;
	private final PharmacyStockCardRepository pharmacyStockCardRepository;
	
	private final CollectionRepository collectionRepository;
	
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
				prescriptionModel.setCreated(prescription.getCreatedAt().toString()+" | "+userService.getUserById(prescription.getCreatedby()).getNickname());
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
			User user = userRepository.findById(radiology.getCreatedby()).get();
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
		
		if(args.user.getId() != null) {
			return ResponseEntity.ok().body(collectionRepository.getCollectionReportByCashier(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1), args.user.getId()));
		}else {
			return ResponseEntity.ok().body(collectionRepository.getCollectionReportGeneral(args.getFrom().atStartOfDay(), args.getTo().atStartOfDay().plusDays(1)));
		}
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
