package com.license.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "licenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Software name is required")
    @Column(nullable = false)
    private String softwareName;
    
    @NotBlank(message = "License key is required")
    @Column(unique = true, nullable = false)
    private String licenseKey;
    
    @Positive(message = "Total seats must be positive")
    @Column(nullable = false)
    private Integer totalSeats;
    
    @Column(nullable = false)
    private Integer usedSeats = 0;
    
    @Column
    private LocalDateTime expirationDate;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // One-to-Many relationship with UserLicense
    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserLicense> userLicenses = new HashSet<>();
    
    // One-to-Many relationship with GroupLicense
    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<GroupLicense> groupLicenses = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Transient
    public Integer getAvailableSeats() {
        return totalSeats - usedSeats;
    }
}
