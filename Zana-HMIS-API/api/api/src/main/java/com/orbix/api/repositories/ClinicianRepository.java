/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Patient;
import com.orbix.api.domain.User;

/**
 * @author Godfrey
 *
 */
public interface ClinicianRepository extends JpaRepository<Clinician, Long> {

	/**
	 * @param nickname
	 * @return
	 */
	Optional<Clinician> findByNickname(String nickname);

	/**
	 * @param user
	 * @return
	 */
	Optional<Clinician> findByUser(User user);

	/**
	 * @param b 
	 * @return
	 */
	List<Clinician> findAllByActive(boolean b);

	/**
	 * @param value
	 * @param value2
	 * @param value3
	 * @return
	 */
	List<Clinician> findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(String value, String value2,
			String value3);

	
}
