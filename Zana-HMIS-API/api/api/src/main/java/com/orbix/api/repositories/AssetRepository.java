package com.orbix.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbix.api.domain.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {

}
