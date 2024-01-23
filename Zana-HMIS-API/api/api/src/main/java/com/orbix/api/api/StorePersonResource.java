/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orbix.api.domain.Store;
import com.orbix.api.domain.StorePerson;
import com.orbix.api.domain.User;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.StoreRepository;
import com.orbix.api.repositories.StorePersonRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.StoreService;
import com.orbix.api.service.StorePersonService;
import com.orbix.api.service.DayService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author Godfrey
 *
 */
@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class StorePersonResource {

	private final StorePersonRepository storePersonRepository;
	private final StoreRepository storeRepository;
	private final StorePersonService storePersonService;
	private final StoreService storeService;
	private final UserRepository userRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/store_persons")
	public ResponseEntity<List<StorePerson>>getStorePersons(HttpServletRequest request){
		return ResponseEntity.ok().body(storePersonService.getStorePersons(request));
	}
	
	@GetMapping("/store_persons/get_all_active")
	public ResponseEntity<List<StorePerson>>getActiveStorePersons(HttpServletRequest request){
		return ResponseEntity.ok().body(storePersonService.getActiveStorePersons(request));
	}
	
	@GetMapping("/store_persons/get")
	public ResponseEntity<StorePerson> getStorePerson(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(storePersonService.getStorePersonById(id, request));
	}
	
	@PostMapping("/store_persons/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<StorePerson>save(
			@RequestBody StorePerson storePerson,
			HttpServletRequest request){
		
		Optional<User> u = userRepository.findByCode(storePerson.getCode());
		if(u.isEmpty()) {
			throw new NotFoundException("Could not find user with the given user code");
		}
		
		//storePerson.setFirstName(Sanitizer.sanitizeString(storePerson.getFirstName()));
		//storePerson.setMiddleName(Sanitizer.sanitizeString(storePerson.getMiddleName()));
		//storePerson.setLastName(Sanitizer.sanitizeString(storePerson.getLastName()));
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/store_persons/save").toUriString());
		return ResponseEntity.created(uri).body(storePersonService.save(storePerson, request));
	}
	
	@GetMapping("/store_persons/load_store_persons_like")
	public ResponseEntity<List<StorePerson>> getStorePersonNameContains(
			@RequestParam(name = "name_like") String value,
			HttpServletRequest request){
		List<StorePerson> storePersons = new ArrayList<StorePerson>();
		storePersons = storePersonRepository.findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(value, value, value);
		return ResponseEntity.ok().body(storePersons);
	}
	
	@GetMapping("/store_persons/get_by_store_id")    // to do later
	public ResponseEntity<List<StorePerson>> getStorePersonByStoreId(
			@RequestParam(name = "store_id") Long storeId,
			HttpServletRequest request){
		Store d = storeRepository.findById(storeId).get();
		List<StorePerson> cs = storePersonRepository.findAll();
		List<StorePerson> cst = new ArrayList<>();
		for(StorePerson c : cs) {
			Collection<Store> cls = c.getStores();
			for(Store cl : cls) {
				if(cl.getName().equals(d.getName())) {
					cst.add(c);
				}
			}
		}
		
		return ResponseEntity.ok().body(cst);
	}
	
	@GetMapping("/store_persons/get_by_store_name")    // to do later
	public ResponseEntity<List<StorePerson>> getStorePersonByStoreName(
			@RequestParam(name = "store_name") String storeName,
			HttpServletRequest request){
		Store d = storeRepository.findByName(storeName).get();
		List<StorePerson> cs = storePersonRepository.findAll();
		List<StorePerson> cst = new ArrayList<>();
		for(StorePerson c : cs) {
			Collection<Store> cls = c.getStores();
			for(Store cl : cls) {
				if(cl.getName().equals(d.getName())) {
					if(c.isActive()) {
						cst.add(c);
					}					
				}
			}
		}
		
		
		return ResponseEntity.ok().body(cst);
	}
	
	@GetMapping("/store_persons/load_storePerson_by_username")    // to do later
	public ResponseEntity<Long> loadStorePersonByUsername(
			@RequestParam(name = "username") String username,
			HttpServletRequest request){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new NotFoundException("User Not found");
		}
		
		Optional<StorePerson> c = storePersonRepository.findByUser(user.get());
		if(!c.isPresent()) {
			throw new NotFoundException("User Account not associated with storePerson");
		}		
		return ResponseEntity.ok().body(c.get().getId());
	}
	
	@PostMapping("/store_persons/assign_user_profile")    // to do later
	public ResponseEntity<StorePerson> assignUserProfile(
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "code") String code,
			HttpServletRequest request){
		Optional<StorePerson> c = storePersonRepository.findById(id);
		if(!c.isPresent()) {
			throw new NotFoundException("StorePerson not found in database");
		}
		Optional<User> u = userRepository.findByCode(code);
		if(!u.isPresent()) {
			throw new NotFoundException("User not found in database");
		}
		Optional<StorePerson> cu = storePersonRepository.findByUser(u.get());
		if(cu.isPresent()) {
			throw new NotFoundException("Can not aasign user account to multiple store_persons");
		}
		c.get().setUser(u.get());
		return ResponseEntity.ok().body(storePersonRepository.save(c.get()));
	}
}
