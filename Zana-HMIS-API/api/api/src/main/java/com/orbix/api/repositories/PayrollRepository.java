package com.orbix.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orbix.api.domain.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
	
	@Query("SELECT MAX(payroll.id) FROM Payroll payroll")
	Long getLastId();

	Optional<Payroll> findByNo(String no);

	List<Payroll> findAllByStatusIn(List<String> statuses);
}
