/**
 * 
 */
package com.orbix.api.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orbix.api.accessories.Formater;
import com.orbix.api.domain.Clinician;
import com.orbix.api.domain.Nurse;
import com.orbix.api.domain.Pharmacist;
import com.orbix.api.domain.Privilege;
import com.orbix.api.domain.Role;
import com.orbix.api.domain.Shortcut;
import com.orbix.api.domain.StorePerson;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.DuplicateEntryException;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.MissingInformationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.models.RecordModel;
import com.orbix.api.repositories.ClinicianRepository;
import com.orbix.api.repositories.NurseRepository;
import com.orbix.api.repositories.PharmacistRepository;
import com.orbix.api.repositories.PrivilegeRepository;
import com.orbix.api.repositories.RoleRepository;
import com.orbix.api.repositories.ShortcutRepository;
import com.orbix.api.repositories.StorePersonRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.security.Object_;
import com.orbix.api.security.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GODFREY
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;
	private final PasswordEncoder passwordEncoder;
	private final ShortcutRepository shortcutRepository;
	
	//private final UserService userService; //do not use this here
	private final DayService dayService;
	
	private final ClinicianRepository clinicianRepository;
	private final PharmacistRepository pharmacistRepository;
	private final NurseRepository nurseRepository;
	private final StorePersonRepository storePersonRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		Optional<User> u = userRepository.findByUsername(username);
		System.out.println(username);
		if(u.isEmpty()) {
			log.error("User not found in the database");
			throw new NotFoundException("User not found in database");
		}else {
			log.info("User found in database: {}", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		u.get().getRoles().forEach(role -> {
			role.getPrivileges().forEach(privilege -> {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
			});
		});
		return new org.springframework.security.core.userdetails.User(u.get().getUsername(), u.get().getPassword(), authorities);
	}
	
	@Override
	public User saveUser(User user, HttpServletRequest request) {
		validateUser(user);
		log.info("Saving user to the database");
		if(user.getId() == null) {
			if(user.getUsername().equalsIgnoreCase("root")) {
				Optional<User> u = userRepository.findByUsername("root");
				if(u.isPresent()) {
					throw new InvalidOperationException("root already exist");
				}
			}
			user.setCode(this.requestUserCode().getCode());
			user.setPassword(passwordEncoder.encode(user.getPassword()));			
		}else {
			User userToUpdate = userRepository.findById(user.getId()).get();
			if(!userToUpdate.getCode().equals(user.getCode())) {
				throw new InvalidOperationException("Changing user code is not allowed");
			}
			if(user.getPassword().equals("") || user.getPassword().equals(null)) {
				user.setPassword(userToUpdate.getPassword());
			}else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			user.setActive(true);// use this in the mean time before implementing actiavate and deactivate user
		}
		user = userRepository.saveAndFlush(user);
		
		/**
		 * First get user roles
		 */
		Collection<Role> roleCollection = user.getRoles();		
		List<Role> roles = List.copyOf(roleCollection);
		/**
		 * Check the roles for specific roles, these roles are special for they carry out critical operations
		 */
		for(Role role : roles) {
			if(role.getName().equals("CLINICIAN")) {
				Optional<Clinician> cl = clinicianRepository.findByUser(user);
				Clinician clinician;
				if(cl.isEmpty()) {
					clinician = new Clinician();
					clinician.setUser(user);
					clinician.setCode(user.getCode());
					clinician.setFirstName(user.getFirstName());
					clinician.setMiddleName(user.getMiddleName());
					clinician.setLastName(user.getLastName());
					clinician.setNickname(user.getNickname());
					
					clinician.setActive(true);
					
					clinician.setCreatedby(getUser(request).getId());
					clinician.setCreatedOn(dayService.getDay().getId());
					clinician.setCreatedAt(dayService.getTimeStamp());
					
					clinicianRepository.save(clinician);
				}else {
					clinician = cl.get();
					clinician.setActive(true);
					clinicianRepository.save(clinician);
				}
			}
			
			if(role.getName().equals("PHARMACIST")) {
				Optional<Pharmacist> cl = pharmacistRepository.findByUser(user);
				Pharmacist pharmacist;
				if(cl.isEmpty()) {
					pharmacist = new Pharmacist();
					pharmacist.setUser(user);
					pharmacist.setCode(user.getCode());
					pharmacist.setFirstName(user.getFirstName());
					pharmacist.setMiddleName(user.getMiddleName());
					pharmacist.setLastName(user.getLastName());
					pharmacist.setNickname(user.getNickname());
					
					pharmacist.setActive(true);
					
					pharmacist.setCreatedby(getUser(request).getId());
					pharmacist.setCreatedOn(dayService.getDay().getId());
					pharmacist.setCreatedAt(dayService.getTimeStamp());
					
					pharmacistRepository.save(pharmacist);
				}else {
					pharmacist = cl.get();
					pharmacist.setActive(true);
					pharmacistRepository.save(pharmacist);
				}
			}
			
			if(role.getName().equals("NURSE")) {
				Optional<Nurse> cl = nurseRepository.findByUser(user);
				Nurse nurse;
				if(cl.isEmpty()) {
					nurse = new Nurse();
					nurse.setUser(user);
					nurse.setCode(user.getCode());
					nurse.setFirstName(user.getFirstName());
					nurse.setMiddleName(user.getMiddleName());
					nurse.setLastName(user.getLastName());
					nurse.setNickname(user.getNickname());
					
					nurse.setActive(true);
					
					nurse.setCreatedby(getUser(request).getId());
					nurse.setCreatedOn(dayService.getDay().getId());
					nurse.setCreatedAt(dayService.getTimeStamp());
					
					nurseRepository.save(nurse);
				}else {
					nurse = cl.get();
					nurse.setActive(true);
					nurseRepository.save(nurse);
				}
			}
			
			if(role.getName().equals("STORE-PERSON")) {
				Optional<StorePerson> storePerson_ = storePersonRepository.findByUser(user);
				StorePerson storePerson;
				if(storePerson_.isEmpty()) {
					storePerson = new StorePerson();
					storePerson.setUser(user);
					storePerson.setCode(user.getCode());
					storePerson.setFirstName(user.getFirstName());
					storePerson.setMiddleName(user.getMiddleName());
					storePerson.setLastName(user.getLastName());
					storePerson.setNickname(user.getNickname());
					
					storePerson.setActive(true);
					
					storePerson.setCreatedBy(getUser(request).getId());
					storePerson.setCreatedOn(dayService.getDay().getId());
					storePerson.setCreatedAt(dayService.getTimeStamp());
					
					storePersonRepository.save(storePerson);
				}else {
					storePerson = storePerson_.get();
					storePerson.setActive(true);
					storePersonRepository.save(storePerson);
				}
			}
		}
		
		//check for presence of a particular personnel
		boolean isClinicianRolePresent = false;
		for(Role role : roles) {
			if(role.getName().equals("CLINICIAN")) {
				isClinicianRolePresent = true;
				break;
			}
		}
		if(isClinicianRolePresent == false) {
			Optional<Clinician> cl = clinicianRepository.findByUser(user);
			if(cl.isPresent()) {
				cl.get().setActive(false);
				clinicianRepository.save(cl.get());
			}
		}
		
		//check for presence of a particular personnel
		boolean isPharmacistRolePresent = false;
		for(Role role : roles) {
			if(role.getName().equals("PHARMACIST")) {
				isPharmacistRolePresent = true;
				break;
			}
		}
		if(isPharmacistRolePresent == false) {
			Optional<Pharmacist> cl = pharmacistRepository.findByUser(user);
			if(cl.isPresent()) {
				cl.get().setActive(false);
				pharmacistRepository.save(cl.get());
			}
		}
		
		//check for presence of a particular personnel
		boolean isNurseRolePresent = false;
		for(Role role : roles) {
			if(role.getName().equals("NURSE")) {
				isNurseRolePresent = true;
				break;
			}
		}
		if(isNurseRolePresent == false) {
			Optional<Nurse> cl = nurseRepository.findByUser(user);
			if(cl.isPresent()) {
				cl.get().setActive(false);
				nurseRepository.save(cl.get());
			}
		}
		
		boolean isStorePersonRolePresent = false;
		for(Role role : roles) {
			if(role.getName().equals("STORE-PERSON")) {
				isStorePersonRolePresent = true;
				break;
			}
		}
		if(isStorePersonRolePresent == false) {
			Optional<StorePerson> storePerson_ = storePersonRepository.findByUser(user);
			if(storePerson_.isPresent()) {
				storePerson_.get().setActive(false);
				storePersonRepository.save(storePerson_.get());
			}
		}
		
		return user;
	}
	
	private boolean validateUser(User user) {
		/**
		 * Validate Username, username should be >=6 and <=16 in length
		 */
		if((user.getUsername().length() < 3 || user.getUsername().length() > 50) && !user.getUsername().equalsIgnoreCase("root")) {
			throw new InvalidEntryException("Invalid length in username, length should be between 3 and 50");
		}
		/**
		 * Validate password, password should have a valid length
		 */
		if(user.getId() == null) {
			if(user.getPassword().equals("")) {
				throw new MissingInformationException("The password field is required");
			}else if(user.getPassword().length() < 4 || user.getPassword().length() >50) {
				throw new InvalidEntryException("Password length should be more than 5 and less than 51");
			}
		}else {
			if(user.getPassword().length() > 0 && (user.getPassword().length() < 6 || user.getPassword().length() > 50)){
				throw new InvalidEntryException("Password length should be more than 3 and less than 51");
			}
		}
		/**
		 * Validate names, first name and last name should be present
		 */
		if(user.getFirstName().equals("") || user.getLastName().equals("")) {
			throw new MissingInformationException("First name or Last name fields are missing");
		}
		/**
		 * Validate alias, alias field should be present
		 * Alias is a unique flag name that visually identifies a user in the system, also identified as Nickname
		 */
		if(user.getNickname().equals("")) {
			throw new MissingInformationException("The nickname field is missing");
		}		
		return true;
	}

	@Override
	public Role saveRole(Role role, HttpServletRequest request) {		
		if(role.getName().equalsIgnoreCase("ROOT")) {
			Optional<Role> r = roleRepository.findByName("ROOT");
			if(r.isPresent()) {
				throw new InvalidOperationException("Can not modify the ROOT role");
			}
		}
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename, HttpServletRequest request) {
		User user = userRepository.findByUsername(username).get();
		Role role = roleRepository.findByName(rolename).get();
		try {
			if(!user.getRoles().contains(role)) {
				user.getRoles().add(role);
			}
		}catch(Exception e) {
			log.info(e.getMessage());
		}					
	}

	@Override
	public User getUser(String username) {
		return userRepository.findByUsername(username).get();
	}

	@Override
	public List<User> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll();
	}

	@Override
	public Privilege savePrivilege(Privilege privilege, HttpServletRequest request) {
		
		Optional<Privilege> p = privilegeRepository.findByName(privilege.getName());
		if(p.isPresent()) {
			throw new InvalidOperationException("Privilege already exist");
		}
		
		log.info("Saving new privilege to the database");
		return privilegeRepository.save(privilege);
	}

	@Override
	public void addPrivilegeToRole(String roleName, String privilegeName) {
		Role role = roleRepository.findByName(roleName).get();
		Optional<Privilege> p = privilegeRepository.findByName(privilegeName);
		
		try {
			if(!role.getPrivileges().contains(p.get())) {
				role.getPrivileges().add(p.get());
			}			
		}catch(Exception e) {
			log.info(e.getMessage());
		}			
	}
	
	@Override
	public void removePrivilegeFromRole(String roleName, String privilegeName) {
		Role role = roleRepository.findByName(roleName).get();
		Optional<Privilege> p = privilegeRepository.findByName(privilegeName);
		
		try {
			role.getPrivileges().remove(p.get());			
		}catch(Exception e) {
			log.info(e.getMessage());
		}			
	}

	@Override
	public List<Role> getRoles() {
		log.info("Fetching all roles");
		return roleRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public boolean deleteUser(User user) {
		/**
		 * Delete a user if a user is deletable
		 */
		if(allowDeleteUser(user) == false) {
			throw new InvalidOperationException("Deleting this user is not allowed");
		}
		userRepository.delete(user);
		return true;
	}
	
	private boolean allowDeleteUser(User user) {
		
		return false;
	}

	@Override
	public Role getRole(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public List<String> getOperations() {
		List<String> operations = new ArrayList<String>();
		for(Field field : Operation.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(Modifier.isStatic(modifiers)) {
				String value = "";
				try {
					value = Operation.class.getDeclaredField(field.getName()).get(null).toString();
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!value.equals("")) {
					operations.add(value);
				}			
			}
		}
		return operations;
	}

	@Override
	public List<String> getObjects() {
		List<String> objects = new ArrayList<String>();
		for(Field field : Object_.class.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if(Modifier.isStatic(modifiers)) {
				String value = "";
				try {
					value = Object_.class.getDeclaredField(field.getName()).get(null).toString();
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!value.equals("")) {
					if(value.contains("-")) {
						objects.add(value.substring(0, value.indexOf("-")));
					}else {
						objects.add(value);
					}
					
				}			
			}
		}
		List<String> subList = objects.subList(0, objects.size());
		Collections.sort(subList);
		return subList;
		//return objects;
	}

	@Override
	public List<String> getPrivileges(String roleName) {
		Collection<Privilege> privileges = roleRepository.findByName(roleName).get().getPrivileges();
		List<String> privilegesList = new ArrayList<String>();
		for(Privilege privilege : privileges) {
			privilegesList.add(privilege.getName());
		}
		return privilegesList;
	}

	@Override
	public Role getRoleById(Long id) {
		return roleRepository.findById(id).get();
	}

	@Override
	public boolean deleteRole(Role role) {
		/**
		 * Delete a role if a role is deletable
		 */
		if(allowDeleteRole(role) == false) {
			throw new InvalidOperationException("Deleting this role is not allowed");
		}
		roleRepository.delete(role);
		return true;
	}
	
	private boolean allowDeleteRole(Role role) {
		/**
		 * Code to check if a role is deletable
		 * Returns false if not
		 */
		return false;
	}

	@Override
	public boolean createShortcut(String username, String name, String link, HttpServletRequest request) {	
		Shortcut shortcut = new Shortcut();
		
		try {
			User user = userRepository.findByUsername(username).get();
			shortcut.setUser(user);
			shortcut.setName(name);
			shortcut.setLink(link);
			Optional<User> userExist = shortcutRepository.findByLinkAndUser(link, user);
			if(!userExist.isPresent()) {
				shortcutRepository.save(shortcut);
			}else {
				throw new DuplicateEntryException("Could not save shortcut, shortcut already exist");
			}
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean removeShortcut(String username, String name) {			
		try {
			User user = userRepository.findByUsername(username).get();
			
			Optional<Shortcut> shortcut = shortcutRepository.findByNameAndUser(name, user);
			if(shortcut.isPresent()) {
				shortcutRepository.delete(shortcut.get());
			}else {
				throw new DuplicateEntryException("Could not remove shortcut, shortcut does not exist");
			}
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<Shortcut> loadShortcuts(String username) {
		try {
			User user = userRepository.findByUsername(username).get();
			return shortcutRepository.findByUser(user);
		}catch(Exception e) {
			throw new InvalidOperationException("Could not load shortcuts");
		}
	}

	@Override
	public Long getUserId(HttpServletRequest request) {
		return userRepository.findByUsername(request.getUserPrincipal().getName()).get().getId();
	}
	
	@Override
	public User getUser(HttpServletRequest request) {
		return userRepository.findByUsername(request.getUserPrincipal().getName()).get();
	}
	
	public RecordModel requestUserCode() {
		Long id = 1L;
		try {
			id = userRepository.getLastId() + 1;
		}catch(Exception e) {
			
		}
		RecordModel model = new RecordModel();
		model.setCode("USR-"+Formater.formatSix(id.toString()));
		model.setNo("USR-"+Formater.formatSix(id.toString()));
		return model;
	}	
}
