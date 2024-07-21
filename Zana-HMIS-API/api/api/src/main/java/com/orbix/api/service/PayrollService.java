package com.orbix.api.service;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Payroll;
import com.orbix.api.domain.PayrollDetail;
import com.orbix.api.models.PayrollModel;
import com.orbix.api.models.RecordModel;

public interface PayrollService {
	PayrollModel save(Payroll payroll, HttpServletRequest request);
	boolean saveDetail(PayrollDetail payrollDetail, HttpServletRequest request);
	
	PayrollModel verify(Payroll payroll, HttpServletRequest request);
	PayrollModel approve(Payroll payroll, HttpServletRequest request);
	PayrollModel cancel(Payroll payroll, HttpServletRequest request);
	PayrollModel submit(Payroll payroll, HttpServletRequest request);
	
	PayrollModel _return(Payroll payroll, HttpServletRequest request);
	PayrollModel reject(Payroll payroll, HttpServletRequest request);
	
	PayrollModel get(Payroll payroll, HttpServletRequest request);
	
	boolean importEmployees(Payroll payroll, HttpServletRequest request);

	/**
	 * @return
	 */
	RecordModel requestPayrollNo();
}
