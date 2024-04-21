/**
 * 
 */
package com.orbix.api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.AdmissionBed;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ConsultationTransfer;
import com.orbix.api.domain.DeceasedNote;
import com.orbix.api.domain.DischargePlan;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientInvoice;
import com.orbix.api.domain.PatientInvoiceDetail;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.ReferralPlan;
import com.orbix.api.domain.WardBed;
import com.orbix.api.domain.WardTypeInsurancePlan;
import com.orbix.api.repositories.AdmissionBedRepository;
import com.orbix.api.repositories.AdmissionRepository;
import com.orbix.api.repositories.ConsultationRepository;
import com.orbix.api.repositories.ConsultationTransferRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DeceasedNoteRepository;
import com.orbix.api.repositories.DischargePlanRepository;
import com.orbix.api.repositories.LabTestRepository;
import com.orbix.api.repositories.MedicineInsurancePlanRepository;
import com.orbix.api.repositories.NonConsultationRepository;
import com.orbix.api.repositories.PatientBillRepository;
import com.orbix.api.repositories.PatientInvoiceDetailRepository;
import com.orbix.api.repositories.PatientInvoiceRepository;
import com.orbix.api.repositories.PatientRepository;
import com.orbix.api.repositories.PrescriptionRepository;
import com.orbix.api.repositories.ProcedureRepository;
import com.orbix.api.repositories.RadiologyRepository;
import com.orbix.api.repositories.ReferralPlanRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.repositories.WardTypeInsurancePlanRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.MedicinePlanServiceImpl;
import com.orbix.api.service.UserService;

import lombok.Data;
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
@Data
public class UpdatePatient implements Runnable{

	private final ConsultationRepository consultationRepository;
	private final NonConsultationRepository nonConsultationRepository;
	private final AdmissionRepository admissionRepository;
	private final PatientBillRepository patientBillRepository;
	
	private final LabTestRepository labTestRepository;
	private final RadiologyRepository radiologyRepository;
	private final ProcedureRepository procedureRepository;
	private final PrescriptionRepository prescriptionRepository;
	
	private final ConsultationTransferRepository consultationTransferRepository;
	
	private final AdmissionBedRepository admissionBedRepository;
	
	private final DayService dayService;
	
	private final WardTypeInsurancePlanRepository wardTypeInsurancePlanRepository;
	private final PatientInvoiceRepository patientInvoiceRepository;
	private final PatientInvoiceDetailRepository patientInvoiceDetailRepository;
	
	private final DischargePlanRepository dischargePlanRepository;
	private final DeceasedNoteRepository deceasedNoteRepository;
	private final ReferralPlanRepository referralPlanRepository;
	
	private final PatientRepository patientRepository;
	
	
	
	@Override
	public void run() {
		
		while(true) {
			try {
				try {
					//Thread.sleep(6000);
					Thread.sleep(300000);///waits for 5 minutes
					//Thread.sleep(5000);//to use for testing
				}catch(Exception e) {}				
				/**
				 * Update consultations
				 */
				List<String> conStatuses = new ArrayList<>();
				conStatuses.add("PENDING");
				conStatuses.add("IN-PROCESS");
				conStatuses.add("TRANSFERED");
				List<Consultation> cs = consultationRepository.findAllByStatusIn(conStatuses);
				
				for(Consultation c : cs) {
					long difference = ChronoUnit.HOURS.between(c.getCreatedAt(), dayService.getTimeStamp());
					if(difference >= 24) {
						//
						c.setStatus("SIGNED-OUT");
						consultationRepository.save(c);
						List<LabTest> labTests = labTestRepository.findByConsultation(c);
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
						List<Radiology> radiologies = radiologyRepository.findByConsultation(c);
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
						List<Procedure> procedures = procedureRepository.findByConsultation(c);
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
						
						List<Prescription> prescriptions = prescriptionRepository.findByConsultation(c);
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
						
						Patient patient = c.getPatient();
						patient.setPaymentType("CASH");
						patient.setInsurancePlan(null);
						patientRepository.save(patient);
					}
				}
				
				List<NonConsultation> ncs = nonConsultationRepository.findAllByStatusIn(conStatuses);
				
				for(NonConsultation c : ncs) {
					long difference = ChronoUnit.HOURS.between(c.getCreatedAt(), dayService.getTimeStamp());
					if(difference >= 24) {
						//
						c.setStatus("SIGNED-OUT");
						nonConsultationRepository.save(c);
						List<LabTest> labTests = labTestRepository.findByNonConsultation(c);
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
						List<Radiology> radiologies = radiologyRepository.findByNonConsultation(c);
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
						List<Procedure> procedures = procedureRepository.findByNonConsultation(c);
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
						
						List<Prescription> prescriptions = prescriptionRepository.findByNonConsultation(c);
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
						Patient patient = c.getPatient();
						patient.setPaymentType("CASH");
						patient.setInsurancePlan(null);
						patientRepository.save(patient);
					}
				}
				
				List<String> admissionStatuses = new ArrayList<>();
				admissionStatuses.add("IN-PROCESS");
				admissionStatuses.add("STOPPED");
				
				List<Admission> adms = admissionRepository.findAllByStatusIn(admissionStatuses);
				
				for(Admission adm : adms) {
					WardBed wardBed = adm.getWardBed();
					
					List<AdmissionBed> admissionBeds = admissionBedRepository.findAllByAdmissionAndStatus(adm, "OPENED");
					int no = 0;
					for(AdmissionBed b : admissionBeds) {
						no = no + 1;
					}
					if(no > 1) {
						int i = 0;
						for(AdmissionBed b : admissionBeds) {
							if(i < no) {
								b.setClosedAt(dayService.getTimeStamp());
								b.setStatus("CLOSED");
								b =  admissionBedRepository.save(b);
								i = i + 1;
							}
						}
						continue;
					}
					
					Optional<AdmissionBed> admBed = admissionBedRepository.findByAdmissionAndStatus(adm, "OPENED");
					
					if(admBed.isEmpty()) {
						continue;
					}
									
					long difference = ChronoUnit.HOURS.between(admBed.get().getOpenedAt(), dayService.getTimeStamp());					
					if(difference >= 24) {
						
						AdmissionBed admissionBed = new AdmissionBed();
						admissionBed = admBed.get();
						
						admissionBed.setClosedAt(dayService.getTimeStamp());
						admissionBed.setStatus("CLOSED");
						admissionBed =  admissionBedRepository.save(admissionBed);
						
						
						
						
						PatientBill wardBedBill = new PatientBill();
						wardBedBill.setAmount(wardBed.getWard().getWardType().getPrice());
						wardBedBill.setPaid(0);
						wardBedBill.setBalance(wardBed.getWard().getWardType().getPrice());
						wardBedBill.setQty(1);
						wardBedBill.setDescription("Ward Bed / Room");
						wardBedBill.setStatus("VERIFIED");
						/**
						 * Add forensic data to registration patientBill
						 */
						wardBedBill.setCreatedBy(admissionBed.getPatientBill().getCreatedBy());
						wardBedBill.setCreatedOn(dayService.getDay().getId());
						wardBedBill.setCreatedAt(dayService.getTimeStamp());
						/**
						 * Assign patient to consultation patientBill
						 */
						wardBedBill.setPatient(adm.getPatient());
						/**
						 * Save Registration patientBill
						 */
						wardBedBill = patientBillRepository.save(wardBedBill);
						
						admissionBed = new AdmissionBed();
						admissionBed.setAdmission(adm);
						admissionBed.setPatient(adm.getPatient());
						admissionBed.setWardBed(wardBed);
						admissionBed.setPatientBill(wardBedBill);
						admissionBed.setStatus("OPENED");
						admissionBed.setOpenedAt(dayService.getTimeStamp());
						admissionBed = admissionBedRepository.save(admissionBed);
						
						
						
						
						
						if(adm.getPatient().getPaymentType().equals("INSURANCE")) {
							
							WardTypeInsurancePlan eligiblePlan = null;
							
							List<WardTypeInsurancePlan> wardTypePricePlans = wardTypeInsurancePlanRepository.findByInsurancePlanAndCovered(adm.getPatient().getInsurancePlan(), true);
							
							double eligiblePrice = 0;
							
							for(WardTypeInsurancePlan plan : wardTypePricePlans) {
								if(plan.getPrice() > eligiblePrice || plan.getInsurancePlan().getId() == adm.getPatient().getInsurancePlan().getId()) {
									eligiblePrice = plan.getPrice();
									eligiblePlan = plan;
									if(plan.getInsurancePlan().getId() == adm.getPatient().getInsurancePlan().getId()) {
										break;
									}
								}
							}	
							
							if(eligiblePlan != null) {
								wardBedBill.setAmount(eligiblePlan.getPrice());
								wardBedBill.setPaid(eligiblePlan.getPrice());
								wardBedBill.setBalance(0);
								wardBedBill.setPaymentType("INSURANCE");
								wardBedBill.setMembershipNo(adm.getPatient().getMembershipNo());
								wardBedBill.setStatus("COVERED");				
								wardBedBill = patientBillRepository.save(wardBedBill);
								
								Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(adm.getPatient(), adm.getPatient().getInsurancePlan(),"PENDING");
								if(!inv.isPresent()) {
									/**
									 * If no pending patientInvoice
									 */
									PatientInvoice patientInvoice = new PatientInvoice();
									patientInvoice.setNo("NA");
									patientInvoice.setPatient(adm.getPatient());
									patientInvoice.setAdmission(adm);
									patientInvoice.setInsurancePlan(adm.getPatient().getInsurancePlan());
									patientInvoice.setStatus("PENDING");
									
									patientInvoice.setCreatedBy(wardBedBill.getCreatedBy());
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
									
									patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
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
									
									patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
									patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
									patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
									
									patientInvoiceDetailRepository.save(patientInvoiceDetail);
								}
								
								if(eligiblePlan.getInsurancePlan().getId() != adm.getPatient().getInsurancePlan().getId() && (wardBed.getWard().getWardType().getPrice() - eligiblePlan.getPrice() > 0)) {
									PatientBill supplementaryWardBedBill = new PatientBill();
														
									supplementaryWardBedBill.setAmount(wardBed.getWard().getWardType().getPrice() - eligiblePlan.getPrice());
									supplementaryWardBedBill.setPaid(0);
									supplementaryWardBedBill.setBalance(wardBed.getWard().getWardType().getPrice() - eligiblePlan.getPrice());
									supplementaryWardBedBill.setStatus("VERIFIED");
									supplementaryWardBedBill.setDescription("Ward Bed / Room (Topup)");
									supplementaryWardBedBill.setPrincipalPatientBill(wardBedBill);
									
									supplementaryWardBedBill.setCreatedBy(wardBedBill.getCreatedBy());
									supplementaryWardBedBill.setCreatedOn(dayService.getDay().getId());
									supplementaryWardBedBill.setCreatedAt(dayService.getTimeStamp());
									
									supplementaryWardBedBill = patientBillRepository.save(supplementaryWardBedBill);					
									wardBedBill.setSupplementaryPatientBill(supplementaryWardBedBill);
									wardBedBill = patientBillRepository.save(wardBedBill);
									
									Optional<PatientInvoice> supInv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(adm.getPatient(), null,"PENDING");
									if(!supInv.isPresent()) {
										/**
										 * If no pending patientInvoice
										 */
										PatientInvoice patientInvoice = new PatientInvoice();
										patientInvoice.setNo("NAA");
										patientInvoice.setPatient(adm.getPatient());
										patientInvoice.setAdmission(adm);
										patientInvoice.setInsurancePlan(null);
										patientInvoice.setStatus("PENDING");
										
										patientInvoice.setCreatedBy(wardBedBill.getCreatedBy());
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
										
										patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
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
										
										patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
										patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
										patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
										
										patientInvoiceDetailRepository.save(patientInvoiceDetail);
									}					
								}
							}
						}else {
							Optional<PatientInvoice> inv = patientInvoiceRepository.findByPatientAndInsurancePlanAndStatus(adm.getPatient(), null,"PENDING");
							if(!inv.isPresent()) {
								/**
								 * If no pending patientInvoice
								 */
								PatientInvoice patientInvoice = new PatientInvoice();
								patientInvoice.setNo("NA");
								patientInvoice.setPatient(adm.getPatient());
								patientInvoice.setAdmission(adm);
								patientInvoice.setInsurancePlan(null);
								patientInvoice.setStatus("PENDING");
								
								patientInvoice.setCreatedBy(wardBedBill.getCreatedBy());
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
								
								patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
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
								
								patientInvoiceDetail.setCreatedBy(wardBedBill.getCreatedBy());
								patientInvoiceDetail.setCreatedOn(dayService.getDay().getId());
								patientInvoiceDetail.setCreatedAt(dayService.getTimeStamp());
								
								patientInvoiceDetailRepository.save(patientInvoiceDetail);
							}
						}
						
					}
				}
				
				List<ConsultationTransfer> cts = consultationTransferRepository.findAllByStatus("PENDING");				
				for(ConsultationTransfer ct : cts) {
					long difference = ChronoUnit.HOURS.between(ct.getCreatedAt(), dayService.getTimeStamp());
					if(difference >= 24) {
						ct.setStatus("CANCELED");
						consultationTransferRepository.save(ct);
					}
				}
				
				List<DischargePlan> dischargePlans = dischargePlanRepository.findAllByStatus("APPROVED");
				for(DischargePlan dischargePlan : dischargePlans) {
					if(dischargePlan.getApprovedAt() != null) {
						long difference = ChronoUnit.HOURS.between(dischargePlan.getApprovedAt(), dayService.getTimeStamp());
						if(difference >= 48) {
							dischargePlan.setStatus("ARCHIVED");
							dischargePlanRepository.save(dischargePlan);
						}
					}else {
						dischargePlan.setStatus("ARCHIVED");
						dischargePlanRepository.save(dischargePlan);
					}
				}
				
				List<DeceasedNote> deceasedNotes = deceasedNoteRepository.findAllByStatus("APPROVED");
				for(DeceasedNote deceasedNote : deceasedNotes) {
					if(deceasedNote.getApprovedAt() != null) {
						long difference = ChronoUnit.HOURS.between(deceasedNote.getApprovedAt(), dayService.getTimeStamp());
						if(difference >= 48) {
							deceasedNote.setStatus("ARCHIVED");
							deceasedNoteRepository.save(deceasedNote);
						}
					}else {
						deceasedNote.setStatus("ARCHIVED");
						deceasedNoteRepository.save(deceasedNote);
					}
				}
				
				List<ReferralPlan> referralPlans = referralPlanRepository.findAllByStatus("APPROVED");
				for(ReferralPlan referralPlan : referralPlans) {
					if(referralPlan.getApprovedAt() != null) {
						long difference = ChronoUnit.HOURS.between(referralPlan.getApprovedAt(), dayService.getTimeStamp());
						if(difference >= 48) {
							referralPlan.setStatus("ARCHIVED");
							referralPlanRepository.save(referralPlan);
						}
					}else {
						referralPlan.setStatus("ARCHIVED");
						referralPlanRepository.save(referralPlan);
					}
				}
				
			}catch(Exception e) {}
		}
	}
}
