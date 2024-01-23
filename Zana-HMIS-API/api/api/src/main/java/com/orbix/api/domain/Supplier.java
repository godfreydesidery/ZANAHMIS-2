/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

/**
 * @author Godfrey
 *
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Table(name = "suppliers")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank	
	@Column(unique = true)
	private String code;
	@NotBlank
	@Column(unique = true)
	private String name;
	@NotBlank
	private String contactName;
	private boolean active = true;
	private String tin;
	private String vrn;	
	private String termsOfContract;
	private String physicalAddress;
	private String postCode;
	private String postAddress;
	private String telephone;
	private String mobile;
	private String email;
	private String fax;
	private String bankAccountName;
	private String bankPhysicalAddress;
	private String bankPostCode;
	private String bankPostAddress;
	private String bankName;
	private String bankAccountNo;
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
