package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Cashier;
import com.orbix.api.domain.User;

public interface CashierRepository extends JpaRepository<Cashier, Long> {

	List<Cashier> findAllByActive(boolean b);

	Optional<Cashier> findByNickname(String name);

	Optional<Cashier> findByUser(User user);

}
