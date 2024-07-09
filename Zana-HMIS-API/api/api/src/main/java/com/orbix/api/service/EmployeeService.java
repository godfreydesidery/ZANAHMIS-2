package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Employee;

public interface EmployeeService {
	Employee save(Employee employee, HttpServletRequest request);	
	List<Employee>getEmployees(HttpServletRequest request); // return all the clinics
	List<Employee>getActiveEmployees(HttpServletRequest request);
	Employee getEmployeeById(Long id, HttpServletRequest request);
}
