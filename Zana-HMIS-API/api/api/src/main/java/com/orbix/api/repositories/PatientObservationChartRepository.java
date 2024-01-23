/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientObservationChart;

/**
 * @author Godfrey
 *
 */
public interface PatientObservationChartRepository extends JpaRepository<PatientObservationChart, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	List<PatientObservationChart> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<PatientObservationChart> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param admission
	 * @return
	 */
	List<PatientObservationChart> findAllByAdmission(Admission admission);

}
