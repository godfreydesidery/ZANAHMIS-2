/**
 * 
 */
package com.orbix.api.reports.models;

import java.time.LocalDate;

import com.orbix.api.domain.LabTestType;

/**
 * @author Godfrey
 *
 */
public interface LabTestTypeReport {
	LocalDate getDate();
	LabTestType getLabTestType();
	double getQty();
	void setQty(double qty);
}
