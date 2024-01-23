/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.DischargePlan;

/**
 * @author Godfrey
 *
 */
public interface DischargePlanRepository extends JpaRepository<DischargePlan, Long> {

	/**
	 * @param admission
	 * @return
	 */
	Optional<DischargePlan> findByAdmission(Admission admission);

	/**
	 * @param string
	 * @return
	 */
	List<DischargePlan> findAllByStatus(String string);

	/**
	 * @param statuses
	 * @return
	 */
	List<DischargePlan> findAllByStatusIn(List<String> statuses);


}
