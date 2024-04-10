/**
 * 
 */
package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.LabTest;
import com.orbix.api.domain.LabTestAttachment;

/**
 * @author Godfrey
 *
 */
public interface LabTestAttachmentRepository extends JpaRepository<LabTestAttachment, Long> {

	List<LabTestAttachment> findAllByLabTest(LabTest labTest);

}
