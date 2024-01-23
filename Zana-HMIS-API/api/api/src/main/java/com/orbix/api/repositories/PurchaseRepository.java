/**
 * 
 */
package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Purchase;

/**
 * @author Godfrey
 *
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
