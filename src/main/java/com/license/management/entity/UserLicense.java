package com.license.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_licenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLicense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id", nullable = false)
    private License license;
    
    @Column(nullable = false)
    private LocalDateTime assignedAt;
    
    @Column
    private LocalDateTime revokedAt;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(length = 500)
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }
}
