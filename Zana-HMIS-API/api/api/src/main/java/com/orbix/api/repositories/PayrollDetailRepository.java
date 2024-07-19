package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.Payroll;
import com.orbix.api.domain.PayrollDetail;

public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Long> {

	List<PayrollDetail> findAllByPayrollAndEmployee(Payroll payroll, Employee employee);

	List<PayrollDetail> findByPayroll(Payroll payroll);

}
