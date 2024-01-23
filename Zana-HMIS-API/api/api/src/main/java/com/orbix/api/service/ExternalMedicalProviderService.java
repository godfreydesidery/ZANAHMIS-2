/**
 * 
 */
package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.ExternalMedicalProvider;

/**
 * @author Godfrey
 *
 */
public interface ExternalMedicalProviderService {
	ExternalMedicalProvider save(ExternalMedicalProvider externalMedicalProvider, HttpServletRequest request);	
	List<ExternalMedicalProvider>getExternalMedicalProviders(HttpServletRequest request); // return all the externalMedicalProviders
	ExternalMedicalProvider getExternalMedicalProviderByName(String name, HttpServletRequest request);
	ExternalMedicalProvider getExternalMedicalProviderById(Long id, HttpServletRequest request);
	boolean deleteExternalMedicalProvider(ExternalMedicalProvider externalMedicalProvider, HttpServletRequest request);
}
