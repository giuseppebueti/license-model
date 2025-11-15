package com.license.management.repository;

import com.license.management.entity.LicenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {
    
    List<LicenseHistory> findByLicenseIdOrderByTimestampDesc(Long licenseId);
    
    List<LicenseHistory> findByUserIdOrderByTimestampDesc(Long userId);
    
    List<LicenseHistory> findByGroupIdOrderByTimestampDesc(Long groupId);
    
    List<LicenseHistory> findByActionTypeOrderByTimestampDesc(LicenseHistory.ActionType actionType);
    
    List<LicenseHistory> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    
    List<LicenseHistory> findTop50ByOrderByTimestampDesc();
}
