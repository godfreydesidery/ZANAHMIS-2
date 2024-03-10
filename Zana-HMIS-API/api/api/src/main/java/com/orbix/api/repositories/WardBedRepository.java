/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.Ward;
import com.orbix.api.domain.WardBed;

/**
 * @author Godfrey
 *
 */
public interface WardBedRepository extends JpaRepository<WardBed, Long> {

	/**
	 * @param no
	 * @param ward
	 * @return
	 */
	Optional<WardBed> findByNoAndWard(String no, Ward ward);

	/**
	 * @param no
	 * @param ward
	 * @return
	 */
	boolean existsByNoAndWard(String no, Ward ward);

	/**
	 * @param ward
	 * @return
	 */
	List<WardBed> findAllByWard(Ward ward);

	/**
	 * @param no
	 * @return
	 */
	Optional<WardBed> findByNo(String no);

	/**
	 * @param ward
	 * @param string
	 * @param b
	 * @return
	 */
	List<WardBed> findAllByWardAndStatusAndActive(Ward ward, String string, boolean b);

}
