package com.license.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "license_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long licenseId;
    
    @Column
    private Long userId;
    
    @Column
    private Long groupId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
    @Column(length = 2000)
    private String details;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column
    private String performedBy;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    public enum ActionType {
        LICENSE_CREATED,
        LICENSE_UPDATED,
        LICENSE_DELETED,
        LICENSE_ASSIGNED_TO_USER,
        LICENSE_REVOKED_FROM_USER,
        LICENSE_ASSIGNED_TO_GROUP,
        LICENSE_REVOKED_FROM_GROUP,
        GROUP_ALLOCATION_INCREASED,
        GROUP_ALLOCATION_DECREASED,
        LICENSE_EXPIRED,
        LICENSE_RENEWED,
        SEATS_INCREASED,
        SEATS_DECREASED
    }
}
