/**
 * 
 */
package com.orbix.api.models;

import com.orbix.api.domain.Consultation;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class GeneralExaminationModel {

	public Long id;	
	public String pressure = "";
	public String temperature = "";
	public String pulseRate = "";
	public String weight = "";
	public String height = "";
	public String bodyMassIndex = "";
	public String bodyMassIndexComment = "";
	public String bodySurfaceArea = "";
	public String saturationOxygen = "";
	public String respiratoryRate = "";
	public String description = "";
	
	public Consultation consultation = null;
	
	public String created = "";
}
