package com.orbix.api.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Admission;
import com.orbix.api.domain.Cashier;
import com.orbix.api.domain.ClinicianPerformance;
import com.orbix.api.domain.Consultation;
import com.orbix.api.domain.NonConsultation;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.repositories.CashierRepository;
import com.orbix.api.repositories.ClinicianPerformanceRepository;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClinicianPerformanceServiceImpl implements ClinicianPerformanceService {
	
	private final ClinicianPerformanceRepository clinicianPerformanceRepository;
	
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final ClinicianRepository clinicianRepository;
	
	
	@Override
	public boolean check(ClinicianPerformance clinicianPerformance, HttpServletRequest request) {
		
		Consultation consultation = clinicianPerformance.getConsultation();
		NonConsultation nonConsultation = clinicianPerformance.getNonConsultation();
		Admission admission = clinicianPerformance.getAdmission();
		
		if(!(consultation != null ^ nonConsultation != null ^ admission != null)) {
			throw new InvalidOperationException("Invalid operation");//put appropriate message
		}
		
		if(consultation != null) {
			Optional<ClinicianPerformance> _clinicianPerformance = clinicianPerformanceRepository.findByConsultationAndClinicianAndCheckDate(consultation, clinicianPerformance.getClinician(), clinicianPerformance.getCheckDate());
			if(_clinicianPerformance.isEmpty()) {
				ClinicianPerformance performance = new ClinicianPerformance();
				performance.setCheckDate(LocalDate.now());
				performance.setConsultation(consultation);
				performance.setClinician(clinicianPerformance.getClinician());
				
				performance.setCreatedBy(userService.getUser(request).getId());
				performance.setCreatedOn(dayService.getDay().getId());
				performance.setCreatedAt(dayService.getTimeStamp());
				
				clinicianPerformanceRepository.save(performance);
			}
		}
		
		if(admission != null) {
			Optional<ClinicianPerformance> _clinicianPerformance = clinicianPerformanceRepository.findByAdmissionAndClinicianAndCheckDate(admission, clinicianPerformance.getClinician(), clinicianPerformance.getCheckDate());
			if(_clinicianPerformance.isEmpty()) {
				ClinicianPerformance performance = new ClinicianPerformance();
				performance.setCheckDate(LocalDate.now());
				performance.setAdmission(admission);
				performance.setClinician(clinicianPerformance.getClinician());
				
				performance.setCreatedBy(userService.getUser(request).getId());
				performance.setCreatedOn(dayService.getDay().getId());
				performance.setCreatedAt(dayService.getTimeStamp());
				
				clinicianPerformanceRepository.save(performance);
			}
		}
		
		return true;
	}
}
