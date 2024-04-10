package com.orbix.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Radiology;
import com.orbix.api.domain.RadiologyAttachment;

public interface RadiologyAttachmentRepository extends JpaRepository<RadiologyAttachment, Long> {

	List<RadiologyAttachment> findAllByRadiology(Radiology radiology);

}
