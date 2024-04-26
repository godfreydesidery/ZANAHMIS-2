package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.domain.PatientVital;

public interface PatientVitalRepository extends JpaRepository<PatientVital, Long> {

	List<PatientVital> findAllByConsultation(Consultation consultation);

	List<PatientVital> findAllByAdmission(Admission admission);

	List<PatientVital> findAllByNonConsultation(NonConsultation nonConsultation);

	Optional<PatientVital> findByConsultation(Consultation consultation);

}
