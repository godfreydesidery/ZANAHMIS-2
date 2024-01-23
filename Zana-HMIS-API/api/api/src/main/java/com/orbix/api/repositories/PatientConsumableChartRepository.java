/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientConsumableChart;

/**
 * @author Godfrey
 *
 */
public interface PatientConsumableChartRepository extends JpaRepository<PatientConsumableChart, Long> {

	/**
	 * @param consultation
	 * @return
	 */
	List<PatientConsumableChart> findAllByConsultation(Consultation consultation);

	/**
	 * @param nonConsultation
	 * @return
	 */
	List<PatientConsumableChart> findAllByNonConsultation(NonConsultation nonConsultation);

	/**
	 * @param admission
	 * @return
	 */
	List<PatientConsumableChart> findAllByAdmission(Admission admission);

}
