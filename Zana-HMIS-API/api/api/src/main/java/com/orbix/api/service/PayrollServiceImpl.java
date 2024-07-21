package com.orbix.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Employee;
import com.orbix.api.domain.Payroll;
import com.orbix.api.domain.PayrollDetail;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.PayrollDetailModel;
import com.orbix.api.models.PayrollModel;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.EmployeeRepository;
import com.orbix.api.repositories.PayrollDetailRepository;
import com.orbix.api.repositories.PayrollRepository;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.repositories.SupplierRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PayrollServiceImpl implements PayrollService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final SupplierRepository supplierRepository;
	private final PayrollRepository payrollRepository;
	private final PayrollDetailRepository payrollDetailRepository;
	private final EmployeeRepository employeeRepository;
	private final StoreRepository storeRepository;
	
	@Override
	public PayrollModel save(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		if(payroll.getId() == null) {
	
			payroll.setCreatedBy(userService.getUser(request).getId());
			payroll.setCreatedOn(dayService.getDay().getId());
			payroll.setCreatedAt(dayService.getTimeStamp());
			
			payroll.setStatus("PENDING");
			//payroll.setStatusDescription("Payroll pending for verification");
			payroll.setStatusDescription("Payroll pending for approval");
			lpo = payroll;
		}else {
			Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
			if(payroll_.isEmpty()) {
				throw new NotFoundException("Payroll not found");
			}
			if(!payroll_.get().getStatus().equals("PENDING")) {
				throw new InvalidOperationException("Can not edit. Only pending payrolls can be edited");
			}
			if(!payroll.getNo().equals(payroll_.get().getNo())) {
				throw new InvalidOperationException("Editing payroll no is not allowed");
			}
			
			lpo = payroll_.get();
			
			lpo.setStartDate(payroll.getStartDate());
			lpo.setEndDate(payroll.getEndDate());
			
			lpo.setDescription(payroll.getDescription());
			lpo.setComments(payroll.getComments());
		}
		lpo = payrollRepository.save(lpo);		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel verify(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}
		if(!payroll_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not verify. Only pending payrolls can be verified");
		}
		if(payroll_.get().getPayrollDetails().isEmpty()) {
			throw new InvalidOperationException("Could not verify. Payroll has no employees");
		}
		
		payroll_.get().setStatus("VERIFIED");
		payroll_.get().setStatusDescription("Payroll awaiting for approval");
		
		payroll_.get().setVerifiedBy(userService.getUser(request).getId());
		payroll_.get().setVerifiedOn(dayService.getDay().getId());
		payroll_.get().setVerifiedAt(dayService.getTimeStamp());
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel approve(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}
		//if(!payroll_.get().getStatus().equals("VERIFIED")) {
			//throw new InvalidOperationException("Could not approve. Only verified payrolls can be approved");
		//}
		if(!payroll_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not approve. Only Pending payrolls can be approved");
		}
		if(payroll_.get().getPayrollDetails().isEmpty()) {
			throw new InvalidOperationException("Could not approve. Payroll has no employees");
		}
		
		for(PayrollDetail d : payroll_.get().getPayrollDetails()) {
			if(d.getNetSalary() != ((d.getBasicSalary() + d.getAddOns()) - d.getDeductions())) {
				throw new InvalidOperationException("Invalid payroll details. Please check " + d.getEmployee().getNo());
			}
		}
		
		payroll_.get().setStatus("APPROVED");
		payroll_.get().setStatusDescription("Payroll approved for execution");
		
		payroll_.get().setApprovedBy(userService.getUser(request).getId());
		payroll_.get().setApprovedOn(dayService.getDay().getId());
		payroll_.get().setApprovedAt(dayService.getTimeStamp());
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel cancel(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}
		//if(!payroll_.get().getStatus().equals("VERIFIED")) {
			//throw new InvalidOperationException("Could not approve. Only verified payrolls can be approved");
		//}
		if(!payroll_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Could not cancel. Only Pending payrolls can be cancelled");
		}
		
		
		payroll_.get().setStatus("CANCELED");
		payroll_.get().setName(payroll_.get().getName() + " " + payroll_.get().getNo());
		payroll_.get().setStatusDescription("Payroll canceled");
		
		payroll_.get().setApprovedBy(userService.getUser(request).getId());
		payroll_.get().setApprovedOn(dayService.getDay().getId());
		payroll_.get().setApprovedAt(dayService.getTimeStamp());
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel submit(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}
		if(!payroll_.get().getStatus().equals("APPROVED")) {
			throw new InvalidOperationException("Could not submit. Only approved payrolls can be submitted");
		}
		if(payroll_.get().getPayrollDetails().isEmpty()) {
			throw new InvalidOperationException("Could not submit. Payroll has no employees");
		}
		
		payroll_.get().setStatus("SUBMITTED");
		payroll_.get().setStatusDescription("Submited to supplier. Payroll awaiting for delivery");
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel _return(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}		
		if(!(payroll_.get().getStatus().equals("PENDING") || payroll_.get().getStatus().equals("VERIFIED"))) {
			throw new InvalidOperationException("Could not returned. Only PENDING or VERIFIED payrolls can be returned");
		}
		
		payroll_.get().setStatus("RETURNED");
		payroll_.get().setStatusDescription("Payroll returned for ammendment");
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public PayrollModel reject(Payroll payroll, HttpServletRequest request) {
		
		Payroll lpo;
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}		
		if(!(payroll_.get().getStatus().equals("PENDING") || payroll_.get().getStatus().equals("VERIFIED"))) {
			throw new InvalidOperationException("Could not reject. Only PENDING or VERIFIED payrolls can be rejected");
		}
		
		payroll_.get().setStatus("REJECTED");
		payroll_.get().setStatusDescription("Payroll rejected");
		
		lpo = payrollRepository.save(payroll_.get());
		
		return(showPayroll(lpo));
	}
	
	@Override
	public boolean saveDetail(PayrollDetail detail, HttpServletRequest request) {
		
		Optional<PayrollDetail> detail_ = payrollDetailRepository.findById(detail.getId());
		if(detail_.isEmpty()) {
			throw new NotFoundException("Detail not found");
		}
		if(detail_.get().getPayroll().getId() != detail.getPayroll().getId()) {
			throw new InvalidOperationException("Payroll do not match");			
		}
		
		if(!detail_.get().getPayroll().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Only a Pending payroll can be modified");
		}
		
		double basicSalary = detail_.get().getBasicSalary();
		double grossSalary = detail_.get().getGrossSalary();
		double netSalary = detail_.get().getNetSalary();
		
		double addOns = detail.getAddOns();
		double deductions = detail.getDeductions();
		double employerContributions = detail.getEmployerContributions();
		
		if(addOns < 0 || deductions < 0 || employerContributions < 0) {
			throw new InvalidEntryException("Invalid Input");
		}
		
		grossSalary = basicSalary + addOns;
		netSalary = grossSalary - deductions;
		
		if(netSalary < 0) {
			throw new InvalidOperationException("Negative Net Salary is not allowed");
		}
		
		detail_.get().setAddOns(addOns);
		detail_.get().setDeductions(deductions);
		detail_.get().setGrossSalary(grossSalary);
		detail_.get().setNetSalary(netSalary);
		detail_.get().setEmployerContributions(employerContributions);
		
		payrollDetailRepository.save(detail_.get());
		return true;
	}
	
	private PayrollModel showPayroll(Payroll ro) {
		PayrollModel model = new PayrollModel();
		List<PayrollDetailModel> modelDetails = new ArrayList<>();
		
		model.setId(ro.getId());
		model.setNo(ro.getNo());
		model.setName(ro.getName());
		model.setStartDate(ro.getStartDate());
		model.setEndDate(ro.getEndDate());
		model.setDescription(ro.getDescription());
		model.setStatus(ro.getStatus());
		model.setStatusDescription(ro.getStatusDescription());
		model.setComments(ro.getComments());
		if(ro.getPayrollDetails() != null) {
			for(PayrollDetail d : ro.getPayrollDetails()) {
				PayrollDetailModel modelDetail = new PayrollDetailModel();
				modelDetail.setId(d.getId());
				modelDetail.setEmployee(d.getEmployee());
				modelDetail.setBasicSalary(d.getBasicSalary());
				modelDetail.setGrossSalary(d.getGrossSalary());
				modelDetail.setNetSalary(d.getNetSalary());
				modelDetail.setAddOns(d.getAddOns());
				modelDetail.setDeductions(d.getDeductions());
				modelDetail.setEmployerContributions(d.getEmployerContributions());

				if(d.getCreatedAt() != null) {
					modelDetail.setCreated(d.getCreatedAt().toString()+" | "+userService.getUserById(d.getCreatedBy()).getNickname());
				}else {
					modelDetail.setCreated(null);
				}
				modelDetails.add(modelDetail);
			}
			model.setPayrollDetails(modelDetails);
		}
		
		if(ro.getCreatedAt() != null) {
			model.setCreated(ro.getCreatedAt().toString()+" | "+userService.getUserById(ro.getCreatedBy()).getNickname());
		}else {
			model.setCreated(null);
		}
		if(ro.getVerifiedAt() != null) {
			model.setVerified(ro.getVerifiedAt().toString()+" | "+userService.getUserById(ro.getVerifiedBy()).getNickname());
		}else {
			model.setVerified(null);
		}
		if(ro.getApprovedAt() != null) {
			model.setApproved(ro.getApprovedAt().toString()+" | "+userService.getUserById(ro.getApprovedBy()).getNickname());
		}else {
			model.setApproved(null);
		}
		
		return model;
	}
	
	@Override
	public PayrollModel get(Payroll payroll, HttpServletRequest request) {
		return showPayroll(payroll);
	}
	
	@Override
	public boolean importEmployees(Payroll payroll, HttpServletRequest request) {
		
		Optional<Payroll> payroll_ = payrollRepository.findById(payroll.getId());
		
		if(payroll_.isEmpty()) {
			throw new NotFoundException("Payroll not found");
		}
		
		if(!payroll_.get().getStatus().equals("PENDING")) {
			throw new InvalidOperationException("Only a pending payroll can be modified");
		}
		
		List<PayrollDetail> payrollDetails = payrollDetailRepository.findByPayroll(payroll_.get());
		List<Employee> employees = employeeRepository.findAllByActive(true);
		int count = 0;
		for(Employee e : employees) {
			boolean isPresent = false;
			for(PayrollDetail d : payrollDetails) {
				if(d.getEmployee().getId() == e.getId()) {
					isPresent = true;
					break;
				}
			}
			if(isPresent == false) {
				PayrollDetail payrollDetail = new PayrollDetail();
				payrollDetail.setPayroll(payroll_.get());
				payrollDetail.setEmployee(e);
				payrollDetail.setBasicSalary(e.getBasicSalary());
				payrollDetail.setStatus("PENDING");
				payrollDetail.setStatusDescription("Pending for Approval");
				
				payrollDetail.setCreatedBy(userService.getUser(request).getId());
				payrollDetail.setCreatedOn(dayService.getDay().getId());
				payrollDetail.setCreatedAt(dayService.getTimeStamp());
				
				payrollDetailRepository.save(payrollDetail);
				count = count + 1;
			}
		}
		if(count == 0) {
			throw new InvalidOperationException("Nothing to import");
		}
		return true;
	}
	
	

	@Override
	public RecordModel requestPayrollNo() {
		Long id = 1L;
		try {
			id = payrollRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PRL",id.toString()));
		return model;
	}

	
}
