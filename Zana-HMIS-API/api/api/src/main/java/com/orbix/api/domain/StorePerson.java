/**
 * 
 */
package com.orbix.api.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "store_persons")
public class StorePerson {
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
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Collection<Store> stores = new ArrayList<>();	
	
	@Column(name = "created_by_user_id", nullable = false , updatable = false)
    private Long createdBy;	
	@Column(name = "created_on_day_id", nullable = false , updatable = false)
    private Long createdOn;
	private LocalDateTime createdAt = LocalDateTime.now();
}
