package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PharmacySaleOrder;
import com.orbix.api.domain.PharmacySaleOrderDetail;

public interface PharmacySaleOrderDetailRepository extends JpaRepository<PharmacySaleOrderDetail, Long> {

	List<PharmacySaleOrderDetail> findAllByPharmacySaleOrder(PharmacySaleOrder pharmacySaleOrder);

}
