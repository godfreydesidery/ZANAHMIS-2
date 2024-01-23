/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.DiagnosisTypeRepository;
import com.orbix.api.repositories.PatientCreditNoteRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Godfrey
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PatientCreditNoteServiceImpl implements PatientCreditNoteService {
	
	private final PatientCreditNoteRepository patientCreditNoteRepository;

	@Override
	public RecordModel requestPatientCreditNoteNo() {
		Long id = 1L;
		try {
			id = patientCreditNoteRepository.getLastId() + 1;
		}catch(Exception e) {}
		RecordModel model = new RecordModel();
		model.setNo(Formater.formatWithCurrentDate("PCN",id.toString()));
		return model;
	}	
}
