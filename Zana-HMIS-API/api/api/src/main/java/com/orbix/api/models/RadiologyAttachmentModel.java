package com.orbix.api.models;

import com.orbix.api.domain.Radiology;

import lombok.Data;

@Data
public class RadiologyAttachmentModel {
	public Long id;
	public String name;
	private String fileName;
    private Radiology radiology = null;
}
