package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.ClinicianPerformance;

public interface ClinicianPerformanceService {
	boolean check(ClinicianPerformance clinicianPerformance, HttpServletRequest request);
}
