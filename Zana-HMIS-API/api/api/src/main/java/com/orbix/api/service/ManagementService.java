package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Management;

public interface ManagementService {
	Management save(Management management, HttpServletRequest request);	
	List<Management>getManagements(HttpServletRequest request); // return all the clinics
	List<Management>getActiveManagements(HttpServletRequest request);
	Management getManagementByName(String name, HttpServletRequest request);
	Management getManagementById(Long id, HttpServletRequest request);
	boolean deleteManagement(Management management, HttpServletRequest request);
}
