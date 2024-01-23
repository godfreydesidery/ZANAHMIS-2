/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "nurses")
public class Nurse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String code;
	private String type;
	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	@NotBlank
	@Column(unique = true)
	private String nickname;
	private boolean active = false;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true , updatable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;
		
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdby;	
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
