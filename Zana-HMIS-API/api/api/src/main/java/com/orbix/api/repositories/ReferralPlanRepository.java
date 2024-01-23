/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.ReferralPlan;

/**
 * @author Godfrey
 *
 */
public interface ReferralPlanRepository extends JpaRepository<ReferralPlan, Long> {

	/**
	 * @param statuses
	 * @return
	 */
	List<ReferralPlan> findAllByStatusIn(List<String> statuses);

	/**
	 * @param admission
	 * @param string
	 * @return
	 */
	Optional<ReferralPlan> findByAdmissionAndStatus(Admission admission, String string);

	/**
	 * @param consultation
	 * @param string
	 * @return
	 */
	Optional<ReferralPlan> findByConsultationAndStatus(Consultation consultation, String string);

	/**
	 * @param string
	 * @return
	 */
	List<ReferralPlan> findAllByStatus(String string);

}
