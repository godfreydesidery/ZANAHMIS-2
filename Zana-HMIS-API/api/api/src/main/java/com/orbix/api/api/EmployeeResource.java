package com.orbix.api.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.EmployeeService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class EmployeeResource {

	private final EmployeeRepository employeeRepository;
	private final EmployeeService employeeService;
	private final UserRepository userRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>>getEmployees(HttpServletRequest request){
		return ResponseEntity.ok().body(employeeService.getEmployees(request));
	}
	
	@GetMapping("/employees/get_all_active")
	public ResponseEntity<List<Employee>>getActiveEmployees(HttpServletRequest request){
		return ResponseEntity.ok().body(employeeService.getActiveEmployees(request));
	}
	
	@GetMapping("/employees/get")
	public ResponseEntity<Employee> getEmployee(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(employeeService.getEmployeeById(id, request));
	}
	
	@PostMapping("/employees/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Employee>save(
			@RequestBody Employee employee,
			HttpServletRequest request){
		employee.setFirstName(Sanitizer.sanitizeString(employee.getFirstName()));
		employee.setMiddleName(Sanitizer.sanitizeString(employee.getMiddleName()));
		employee.setLastName(Sanitizer.sanitizeString(employee.getLastName()));
		employee.setNationalIdNo(employee.getNationalIdNo().replace(" ", "").replace("-", ""));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/employees/save").toUriString());
		return ResponseEntity.created(uri).body(employeeService.save(employee, request));
	}
	
	
	
	
}
