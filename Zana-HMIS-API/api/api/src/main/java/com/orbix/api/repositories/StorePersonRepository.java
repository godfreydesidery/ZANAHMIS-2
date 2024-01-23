/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.StorePerson;
import com.orbix.api.domain.User;

/**
 * @author Godfrey
 *
 */
public interface StorePersonRepository extends JpaRepository<StorePerson, Long> {

	/**
	 * @param b
	 * @return
	 */
	List<StorePerson> findAllByActive(boolean b);

	/**
	 * @param name
	 * @return
	 */
	Optional<StorePerson> findByNickname(String name);

	/**
	 * @param value
	 * @param value2
	 * @param value3
	 * @return
	 */
	List<StorePerson> findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(String value,
			String value2, String value3);

	/**
	 * @param user
	 * @return
	 */
	Optional<StorePerson> findByUser(User user);

}
