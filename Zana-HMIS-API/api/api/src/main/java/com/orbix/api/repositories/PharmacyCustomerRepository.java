package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacyCustomer;

public interface PharmacyCustomerRepository extends JpaRepository<PharmacyCustomer, Long> {

}
