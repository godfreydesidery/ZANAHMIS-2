package com.orbix.api.api;

import java.net.URI;
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

import com.orbix.api.api.accessories.Sanitizer;
import com.orbix.api.domain.Asset;
import com.orbix.api.domain.PayrollDetail;
import com.orbix.api.exceptions.InvalidOperationException;
import com.orbix.api.exceptions.NotFoundException;
import com.orbix.api.repositories.AssetRepository;
import com.orbix.api.repositories.UserRepository;
import com.orbix.api.service.DayService;
import com.orbix.api.service.AssetService;
import com.orbix.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zana-hmis-api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Transactional
public class AssetResource {
	private final AssetRepository assetRepository;
	private final AssetService assetService;
	private final UserRepository userRepository;
	
	private final UserService userService;
	private final DayService dayService;
	
	@GetMapping("/assets")
	public ResponseEntity<List<Asset>>getAssets(HttpServletRequest request){
		return ResponseEntity.ok().body(assetService.getAssets(request));
	}
	
	@GetMapping("/assets/get")
	public ResponseEntity<Asset> getAsset(
			@RequestParam(name = "id") Long id,
			HttpServletRequest request){
		return ResponseEntity.ok().body(assetService.getAssetById(id, request));
	}
	
	@PostMapping("/assets/save")
	@PreAuthorize("hasAnyAuthority('EMPLOYEE-ALL')")
	public ResponseEntity<Asset>save(
			@RequestBody Asset asset,
			HttpServletRequest request){

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/assets/save").toUriString());
		return ResponseEntity.created(uri).body(assetService.save(asset, request));
	}
	
	@PostMapping("/assets/delete")
	//@PreAuthorize("hasAnyAuthority('PAYROLL-ALL')")
	public boolean deleteAsset(
			@RequestBody Asset asset,
			HttpServletRequest request){
		
		Optional<Asset> asset_ = assetRepository.findById(asset.getId());
		if(asset_.isEmpty()) {
			throw new NotFoundException("Asset not found");
		}
		
		URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/zana-hmis-api/assets/delete").toUriString());
		assetRepository.delete(asset_.get());
		return true;
	}
	
}
