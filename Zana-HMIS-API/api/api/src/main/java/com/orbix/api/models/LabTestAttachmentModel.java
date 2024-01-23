/**
 * 
 */
package com.orbix.api.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.orbix.api.domain.LabTest;

import lombok.Data;

/**
 * @author Godfrey
 *
 */
@Data
public class LabTestAttachmentModel {
	public Long id;	
	public String name;
	private String fileName;	
    private LabTest labTest = null;

}
