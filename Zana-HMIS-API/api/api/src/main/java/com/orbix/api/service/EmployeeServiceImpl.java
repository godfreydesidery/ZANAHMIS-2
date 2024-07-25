package com.orbix.api.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.CompanyProfile;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final EmployeeRepository employeeRepository;
	private final CompanyProfileService companyProfileService;
	
	@Override
	public Employee save(Employee employee, HttpServletRequest request) {
		
		if(employee.getId() == null) {
			employee.setCreatedBy(userService.getUser(request).getId());
			employee.setCreatedOn(dayService.getDay().getId());
			employee.setCreatedAt(dayService.getTimeStamp());
			
			employee.setActive(true);
		}
		log.info("Saving new employee to the database");
		
		employee.setNo(String.valueOf(Math.random()));		
		employee = employeeRepository.save(employee);	
		
		try {
			CompanyProfile profile = companyProfileService.getCompanyProfile(request);		
			if(!profile.getEmployeePrefix().equals("")) {
				employee.setNo(profile.getEmployeePrefix() + "/" + employee.getId());
			}else {
				employee.setNo("EMP/" + employee.getId());
			}
		}catch(Exception e) {
			employee.setNo("EMP/" + employee.getId());
		}		
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getEmployees(HttpServletRequest request) {
		log.info("Fetching all employees");
		return employeeRepository.findAll();
	}
	
	@Override
	public List<Employee> getActiveEmployees(HttpServletRequest request) {
		log.info("Fetching all employees");
		return employeeRepository.findAllByActive(true);
	}

	@Override
	public Employee getEmployeeById(Long id, HttpServletRequest request) {
		return employeeRepository.findById(id).get();
	}

}
