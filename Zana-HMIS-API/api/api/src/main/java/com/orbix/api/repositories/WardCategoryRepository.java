/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.WardCategory;

/**
 * @author Godfrey
 *
 */
public interface WardCategoryRepository extends JpaRepository <WardCategory, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<WardCategory> findByName(String name);

}
