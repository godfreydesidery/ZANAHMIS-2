/**
 * 
 */
package com.orbix.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.InsurancePlanRepository;
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
public class PatientDressingChartServiceImpl implements PatientDressingChartService {

}
