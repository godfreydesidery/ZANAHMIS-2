package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.orbix.api.domain.Asset;
import com.orbix.api.exceptions.InvalidEntryException;
import com.orbix.api.repositories.DayRepository;
import com.orbix.api.repositories.AssetRepository;
import com.orbix.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AssetServiceImpl implements AssetService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final DayRepository dayRepository;
	private final DayService dayService;
	private final AssetRepository assetRepository;
	
	@Override
	public Asset save(Asset asset, HttpServletRequest request) {
		
		if(asset.getId() == null) {
			
			asset.setCreatedBy(userService.getUser(request).getId());
			asset.setCreatedOn(dayService.getDay().getId());
			asset.setCreatedAt(dayService.getTimeStamp());
			
		}
		log.info("Saving new asset to the database");
		
		if(asset.getQty() < 0) {
			throw new InvalidEntryException("Negative qty is not allowed");
		}
		
		if(asset.getPrice() < 0) {
			throw new InvalidEntryException("Negative price is not allowed");
		}
		
		if(asset.isMultiple() == false && asset.getQty() > 1) {
			//throw new InvalidEntryException("Non multiple must not be more than one");
		}
		
		return assetRepository.save(asset);
	}

	@Override
	public List<Asset> getAssets(HttpServletRequest request) {
		log.info("Fetching all assets");
		return assetRepository.findAll();
	}
	
	@Override
	public Asset getAssetById(Long id, HttpServletRequest request) {
		return assetRepository.findById(id).get();
	}
}
