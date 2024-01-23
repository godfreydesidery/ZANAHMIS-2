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
public class ClinicalNoteModel {
	public Long id = null;
	public String mainComplain = "";
	public String presentIllnessHistory = "";
	public String pastMedicalHistory = "";
	public String familyAndSocialHistory = "";
	public String drugsAndAllergyHistory = "";
	public String reviewOfOtherSystems = "";
	public String physicalExamination = "";
	public String managementPlan = "";
	
	public Consultation consultation = null;
	
	public String created = "";
	
	
}
