package com.license.management.repository;

import com.license.management.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    
    Optional<License> findByLicenseKey(String licenseKey);
    
    List<License> findBySoftwareName(String softwareName);
    
    List<License> findByActiveTrue();
    
    List<License> findByActiveFalse();
    
    List<License> findByExpirationDateBefore(LocalDateTime date);
    
    List<License> findByExpirationDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT l FROM License l WHERE l.usedSeats >= l.totalSeats")
    List<License> findFullyUtilizedLicenses();
    
    @Query("SELECT l FROM License l WHERE l.usedSeats < l.totalSeats")
    List<License> findAvailableLicenses();
    
    boolean existsByLicenseKey(String licenseKey);
}
