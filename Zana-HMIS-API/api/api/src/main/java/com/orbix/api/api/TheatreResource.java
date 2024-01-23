/**
 * 
 */
package com.orbix.api.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Theatre;
import com.orbix.api.repositories.TheatreRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.TheatreService;
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
public class TheatreResource {

	private final TheatreRepository theatreRepository;
	private final TheatreService theatreService;
	

	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/theatres")
	public ResponseEntity<List<Theatre>>getTheatres(HttpServletRequest request){
		return ResponseEntity.ok().body(theatreService.getTheatres(request));
	}
	
	@GetMapping("/theatres/get")
	public ResponseEntity<Theatre> getTheatre(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(theatreService.getTheatreById(id, request));
	}
	
	@GetMapping("/theatres/get_by_name")
	public ResponseEntity<Theatre> getTheatreByName(
			@RequestParam(name = "name") String name,
			HttpServletRequest request){
		return ResponseEntity.ok().body(theatreService.getTheatreByName(name, request));
	}
	
	@GetMapping("/theatres/get_names")
	public ResponseEntity<List<String>> getTheatreNames(HttpServletRequest request){
		List<String> names = new ArrayList<String>();
		names = theatreService.getNames(request);
		return ResponseEntity.ok().body(names);
	}
	
	@PostMapping("/theatres/save")
	@PreAuthorize("hasAnyAuthority('ADMIN-ACCESS')")
	public ResponseEntity<Theatre>save(
			@RequestBody Theatre theatre,
			HttpServletRequest request){
		theatre.setName(theatre.getName());
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/theatres/save").toUriString());
		return ResponseEntity.created(uri).body(theatreService.save(theatre, request));
	}
	
	
}
