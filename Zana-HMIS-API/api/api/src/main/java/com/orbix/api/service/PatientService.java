/**
 * 
 */
package com.orbix.api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ConsultationTransfer;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientConsumableChart;
import com.orbix.api.domain.PatientDressingChart;
import com.orbix.api.domain.PatientNursingCarePlan;
import com.orbix.api.domain.PatientNursingChart;
import com.orbix.api.domain.PatientNursingProgressNote;
import com.orbix.api.domain.PatientObservationChart;
import com.orbix.api.domain.PatientPrescriptionChart;
import com.orbix.api.domain.Prescription;
import com.orbix.api.domain.Procedure;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.WardBed;

/**
 * @author Godfrey
 *
 */
public interface PatientService {
	Patient doRegister(Patient patient, HttpServletRequest request);
	Patient doConsultation(Patient p, Clinic c, Clinician cn, HttpServletRequest request);
	ConsultationTransfer createConsultationTransfer(ConsultationTransfer transfer, HttpServletRequest request);	
	Admission doAdmission(Patient p, WardBed wb, HttpServletRequest request);
	Patient update(Patient patient, HttpServletRequest request);
	List<Patient>getBySearchKey(String searchKey);
	List<Patient>getAll();
	List<String> getSearchKeys();
	Patient findBySearchKey(String code);
	
	LabTest saveLabTest(LabTest test, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request);
	
	Radiology saveRadiology(Radiology radio, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request);
	
	Procedure saveProcedure(Procedure procedure, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request);
	
	Prescription savePrescription(Prescription prescription, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, HttpServletRequest request);
	
	PatientDressingChart savePatientDressingChart(PatientDressingChart chart, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientConsumableChart savePatientConsumableChart(PatientConsumableChart chart, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientObservationChart savePatientObservationChart(PatientObservationChart chart, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientPrescriptionChart savePatientPrescriptionChart(PatientPrescriptionChart chart, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientNursingChart savePatientNursingChart(PatientNursingChart chart, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientNursingProgressNote savePatientNursingProgressNote(PatientNursingProgressNote note, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	PatientNursingCarePlan savePatientNursingCarePlan(PatientNursingCarePlan plan, Optional<Consultation> c, Optional<NonConsultation> nc, Optional<Admission> a, Optional<Nurse> n, HttpServletRequest request);
	
	ResponseEntity<Map<String, String>> saveLabTestAttachment(LabTest labTest, MultipartFile file, String name, HttpServletRequest request);
}
