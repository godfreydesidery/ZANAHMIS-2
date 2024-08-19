package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacyCustomer;

public interface PharmacyCustomerRepository extends JpaRepository<PharmacyCustomer, Long> {

	List<PharmacyCustomer> findAllByNameContaining(String value);

}
