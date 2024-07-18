package com.orbix.api.models;

import com.orbix.api.domain.Employee;
import com.orbix.api.domain.Payroll;

import lombok.Data;

@Data
public class PayrollDetailModel {
	Long id;	
	double basicSalary;
	double grossSalary;
	double netSalary;
	double addOns;
	double deductions;
	double employerContributions;	
	String status;
	String statusDescription;
    Payroll payroll = null;
    Employee employee = null;   
    String created;
    String verified;
    String approved;
}
