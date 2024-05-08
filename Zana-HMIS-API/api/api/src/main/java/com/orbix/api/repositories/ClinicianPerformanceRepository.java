package com.orbix.api.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.ClinicianPerformance;
import com.orbix.api.domain.Consultation;

public interface ClinicianPerformanceRepository extends JpaRepository<ClinicianPerformance, Long> {

	Optional<ClinicianPerformance> findByConsultationAndClinicianAndCheckDate(Consultation consultation,
			Clinician clinician, LocalDate checkDate);

	Optional<ClinicianPerformance> findByAdmissionAndClinicianAndCheckDate(Admission admission, Clinician clinician,
			LocalDate checkDate);


}
