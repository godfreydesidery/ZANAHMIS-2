/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientNursingChart;

/**
 * @author Godfrey
 *
 */
public interface PatientNursingChartRepository extends JpaRepository<PatientNursingChart, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	List<PatientNursingChart> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<PatientNursingChart> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param admission
	 * @return
	 */
	List<PatientNursingChart> findAllByAdmission(Admission admission);

}
