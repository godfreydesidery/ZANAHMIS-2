/**
 * 
 */
package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.RadiologyType;

/**
 * @author Godfrey
 *
 */
public interface RadiologyRepository extends JpaRepository<Radiology, Long> {

	/**
	 * @param consultation
	 * @param statuses
	 * @return
	 */
	List<Radiology> findAllByConsultationAndStatusIn(Consultation consultation, List<String> statuses);

	/**
	 * @param nonConsultation
	 * @param statuses
	 * @return
	 */
	List<Radiology> findAllByNonConsultationAndStatusIn(NonConsultation nonConsultation, List<String> statuses);

	/**
	 * @param admission
	 * @param statuses
	 * @return
	 */
	List<Radiology> findAllByAdmissionAndStatusIn(Admission admission, List<String> statuses);

	/**
	 * @param consultation
	 * @return
	 */
	List<Radiology> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<Radiology> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param consultation
	 * @param radiologyType
	 * @return
	 */
	boolean existsByConsultationAndRadiologyType(Consultation consultation, RadiologyType radiologyType);

	/**
	 * @param nonConsultation
	 * @param radiologyType
	 * @return
	 */
	boolean existsByNonConsultationAndRadiologyType(NonConsultation nonConsultation, RadiologyType radiologyType);

	/**
	 * @param ncs
	 * @return
	 */
	List<Radiology> findAllByNonConsultationIn(List<NonConsultation> ncs);

	/**
	 * @param cs
	 * @return
	 */
	List<Radiology> findAllByConsultationIn(List<Consultation> cs);

	/**
	 * @param cs
	 * @param ncs
	 * @return
	 */
	List<Radiology> findAllByConsultationInOrNonConsultationIn(List<Consultation> cs, List<NonConsultation> ncs);

	/**
	 * @param patient
	 * @return
	 */
	List<Radiology> findAllByPatient(Patient patient);

	/**
	 * @param admission
	 * @return
	 */
	List<Radiology> findAllByAdmission(Admission admission);

	/**
	 * @param adm
	 * @return
	 */
	List<Radiology> findAllByAdmissionIn(List<Admission> adm);

	/**
	 * @param cs
	 * @param ncs
	 * @param adm
	 * @return
	 */
	List<Radiology> findAllByConsultationInOrNonConsultationInOrAdmissionIn(List<Consultation> cs,
			List<NonConsultation> ncs, List<Admission> adm);

	/**
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Radiology> findAllByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime plusDays);

	/**
	 * @param c
	 * @return
	 */
	List<Radiology> findByConsultation(Consultation c);

	/**
	 * @param c
	 * @return
	 */
	List<Radiology> findByNonConsultation(NonConsultation c);

	/**
	 * @param clinician
	 * @param atStartOfDay
	 * @param plusDays
	 * @return
	 */
	List<Radiology> findAllByClinicianAndCreatedAtBetween(Clinician clinician, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

}
