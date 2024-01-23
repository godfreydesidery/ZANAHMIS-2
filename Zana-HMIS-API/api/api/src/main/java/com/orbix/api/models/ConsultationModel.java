/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Clinic;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Patient;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class ConsultationModel {
	private double id;
	private String paymentType;
	private Patient patient;
	private Clinician clinician;
	private Clinic clinic;
}
