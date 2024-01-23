/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Ward;
import com.orbix.api.domain.WardCategory;
import com.orbix.api.domain.WardType;

/**
 * @author Godfrey
 *
 */
public interface WardRepository extends JpaRepository <Ward, Long>{

	/**
	 * @param name
	 * @return
	 */
	Optional<Ward> findByName(String name);

	/**
	 * @param wardCategory
	 * @param wardType
	 * @return
	 */
	List<Ward> findAllByWardCategoryAndWardType(WardCategory wardCategory, WardType wardType);

}
