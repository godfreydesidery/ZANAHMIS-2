package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacySaleOrder;

public interface PharmacySaleOrderRepository extends JpaRepository<PharmacySaleOrder, Long> {

	List<PharmacySaleOrder> findAllByStatusIn(List<String> statuses);

}
