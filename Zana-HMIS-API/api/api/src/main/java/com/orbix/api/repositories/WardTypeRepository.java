/**
 * 
 */
package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.WardType;

/**
 * @author Godfrey
 *
 */
public interface WardTypeRepository extends JpaRepository<WardType, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<WardType> findByName(String name);

}
