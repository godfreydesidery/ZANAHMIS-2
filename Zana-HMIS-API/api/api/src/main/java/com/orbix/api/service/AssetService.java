package com.orbix.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orbix.api.domain.Asset;

public interface AssetService {
	Asset save(Asset asset, HttpServletRequest request);	
	List<Asset>getAssets(HttpServletRequest request); // return all the clinics
	//List<Asset>getActiveAssets(HttpServletRequest request);
	Asset getAssetById(Long id, HttpServletRequest request);
}
