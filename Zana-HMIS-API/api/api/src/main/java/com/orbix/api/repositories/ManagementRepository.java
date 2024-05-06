package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Management;
import com.orbix.api.domain.User;

public interface ManagementRepository extends JpaRepository<Management, Long> {

	List<Management> findAllByActive(boolean b);

	Optional<Management> findByNickname(String name);

	Optional<Management> findByUser(User user);

}
