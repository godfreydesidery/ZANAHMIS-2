/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.Registration;

/**
 * @author Godfrey
 *
 */
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	/**
	 * @param patient
	 * @return
	 */
	Registration findByPatient(Patient patient);

	List<Registration> findAllByPatientBillIn(List<PatientBill> bills);

}
