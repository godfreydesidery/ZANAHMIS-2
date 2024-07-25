package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "assets")
public class Asset {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String code;
	
	@NotBlank
	private String name;
	private double qty = 0;
	private double price = 0;
	
	private boolean multiple = true;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;	
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
