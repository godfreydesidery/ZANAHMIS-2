package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Cashier;

public interface CashierService {
	Cashier save(Cashier cashier, HttpServletRequest request);	
	List<Cashier>getCashiers(HttpServletRequest request); // return all the clinics
	List<Cashier>getActiveCashiers(HttpServletRequest request);
	Cashier getCashierByName(String name, HttpServletRequest request);
	Cashier getCashierById(Long id, HttpServletRequest request);
	boolean deleteCashier(Cashier cashier, HttpServletRequest request);
}
