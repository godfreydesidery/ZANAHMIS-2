/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Dressing;
import com.orbix.api.domain.ProcedureType;

/**
 * @author Godfrey
 *
 */
public interface DressingRepository extends JpaRepository<Dressing, Long> {

	/**
	 * @param procedureType
	 * @return
	 */
	List<Dressing> findAllByProcedureType(ProcedureType procedureType);

	/**
	 * @param procedureType
	 * @return
	 */
	Dressing findByProcedureType(ProcedureType procedureType);
}
