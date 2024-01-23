/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.User;

/**
 * @author Godfrey
 *
 */
public interface NurseRepository extends JpaRepository<Nurse, Long> {

	/**
	 * @param name
	 * @return
	 */
	Optional<Nurse> findByNickname(String name);

	/**
	 * @param user
	 * @return
	 */
	Optional<Nurse> findByUser(User user);

	/**
	 * @param b
	 * @return
	 */
	List<Nurse> findAllByActive(boolean b);
}
