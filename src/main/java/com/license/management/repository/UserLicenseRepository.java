package com.license.management.repository;

import com.license.management.entity.UserLicense;
import com.license.management.entity.User;
import com.license.management.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLicenseRepository extends JpaRepository<UserLicense, Long> {
    
    List<UserLicense> findByUser(User user);
    
    List<UserLicense> findByLicense(License license);
    
    List<UserLicense> findByUserAndActiveTrue(User user);
    
    List<UserLicense> findByLicenseAndActiveTrue(License license);
    
    Optional<UserLicense> findByUserAndLicenseAndActiveTrue(User user, License license);
    
    boolean existsByUserAndLicenseAndActiveTrue(User user, License license);
    
    long countByLicenseAndActiveTrue(License license);
}
