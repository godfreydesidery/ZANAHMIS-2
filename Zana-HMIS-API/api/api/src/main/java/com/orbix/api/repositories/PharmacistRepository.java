/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Pharmacist;
import com.orbix.api.domain.User;

/**
 * @author Godfrey
 *
 */
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

	/**
	 * @param nickname
	 * @return
	 */
	Optional<Pharmacist> findByNickname(String nickname);

	/**
	 * @param user
	 * @return
	 */
	Optional<Pharmacist> findByUser(User user);

	/**
	 * @param b
	 * @return
	 */
	List<Pharmacist> findAllByActive(boolean b);

	
}
