package com.orbix.api.models;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PayrollModel {
	Long id;
	String no;
	String name;
	String description;	
	LocalDate startDate;
	LocalDate endDate;	
	String status;
	String statusDescription;	
	String comments;	
	String created;
	String verified;
	String approved;
	List<PayrollDetailModel> payrollDetailModels;
}
