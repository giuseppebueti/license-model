package com.license.management.repository;

import com.license.management.entity.GroupLicense;
import com.license.management.entity.UserGroup;
import com.license.management.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupLicenseRepository extends JpaRepository<GroupLicense, Long> {
    
    List<GroupLicense> findByGroup(UserGroup group);
    
    List<GroupLicense> findByLicense(License license);
    
    List<GroupLicense> findByGroupAndActiveTrue(UserGroup group);
    
    List<GroupLicense> findByLicenseAndActiveTrue(License license);
    
    Optional<GroupLicense> findByGroupAndLicenseAndActiveTrue(UserGroup group, License license);
    
    boolean existsByGroupAndLicenseAndActiveTrue(UserGroup group, License license);
    
    @Query("SELECT SUM(gl.allocatedSeats) FROM GroupLicense gl WHERE gl.license = :license AND gl.active = true")
    Integer sumAllocatedSeatsByLicense(@Param("license") License license);
}
