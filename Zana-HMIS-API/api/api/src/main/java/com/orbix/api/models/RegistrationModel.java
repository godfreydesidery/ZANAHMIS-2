package com.orbix.api.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.Patient;
import com.orbix.api.domain.PatientBill;

import lombok.Data;

@Data
public class RegistrationModel {

	Long id = null;
	String status = null;
	Patient patient = null;
	PatientBill patientBill = null;
	
	String created = "";	
}
