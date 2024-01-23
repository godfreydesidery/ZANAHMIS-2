/**
 * 
 */
package com.orbix.api.reports.service;

import java.time.LocalDate;
import java.util.List;

import com.orbix.api.domain.Item;
import com.orbix.api.domain.Supplier;
import com.orbix.api.reports.models.LpoReport;

/**
 * @author Godfrey
 *
 */
public interface LocalPurchaseOrderReportService {
	
	List<LpoReport> getLocalPurchaseOrderReport(
		LocalDate from,
		LocalDate to,
		Supplier supplier,
		List<Item> items);	
}
