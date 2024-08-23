package com.orbix.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.PatientBill;
import com.orbix.api.domain.PharmacySaleOrder;
import com.orbix.api.domain.PharmacySaleOrderDetail;

public interface PharmacySaleOrderDetailRepository extends JpaRepository<PharmacySaleOrderDetail, Long> {

	List<PharmacySaleOrderDetail> findAllByPharmacySaleOrder(PharmacySaleOrder pharmacySaleOrder);

	Optional<PharmacySaleOrderDetail> findByPatientBill(PatientBill bill);

	List<PharmacySaleOrderDetail> findAllByPayStatusAndCreatedAtBetween(String string, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

}
