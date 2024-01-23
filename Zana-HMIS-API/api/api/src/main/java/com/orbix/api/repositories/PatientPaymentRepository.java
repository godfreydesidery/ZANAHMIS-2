/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PatientPayment;

/**
 * @author Godfrey
 *
 */
public interface PatientPaymentRepository extends JpaRepository<PatientPayment, Long> {

}
