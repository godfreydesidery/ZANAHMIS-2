package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacySaleOrder;

public interface PharmacySaleOrderRepository extends JpaRepository<PharmacySaleOrder, Long> {

}
