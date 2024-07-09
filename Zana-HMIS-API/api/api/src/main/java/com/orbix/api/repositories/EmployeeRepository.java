package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findAllByActive(boolean b);

}
