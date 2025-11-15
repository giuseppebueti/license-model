package com.license.management.controller;

import com.license.management.entity.*;
import com.license.management.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/licenses")
@CrossOrigin(origins = "*")
public class LicenseController {
    
    @Autowired
    private LicenseService licenseService;
    
    // ===== License CRUD Endpoints =====
    
    @GetMapping
    public ResponseEntity<List<License>> getAllLicenses() {
        return ResponseEntity.ok(licenseService.getAllLicenses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<License> getLicenseById(@PathVariable Long id) {
        return licenseService.getLicenseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<License>> getActiveLicenses() {
        return ResponseEntity.ok(licenseService.getActiveLicenses());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<License>> getAvailableLicenses() {
        return ResponseEntity.ok(licenseService.getAvailableLicenses());
    }
    
    @PostMapping
    public ResponseEntity<License> createLicense(@Valid @RequestBody License license) {
        try {
            License created = licenseService.createLicense(license);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<License> updateLicense(@PathVariable Long id, @Valid @RequestBody License license) {
        try {
            License updated = licenseService.updateLicense(id, license);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        try {
            licenseService.deleteLicense(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ===== User License Assignment Endpoints =====
    
    @PostMapping("/{licenseId}/assign/user/{userId}")
    public ResponseEntity<UserLicense> assignLicenseToUser(
            @PathVariable Long licenseId,
            @PathVariable Long userId,
            @RequestBody(required = false) Map<String, String> request) {
        try {
            String notes = request != null ? request.get("notes") : null;
            UserLicense assignment = licenseService.assignLicenseToUser(licenseId, userId, notes);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/user-assignments/{userLicenseId}")
    public ResponseEntity<Void> revokeLicenseFromUser(@PathVariable Long userLicenseId) {
        try {
            licenseService.revokeLicenseFromUser(userLicenseId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserLicense>> getUserLicenses(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(licenseService.getUserLicenses(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{licenseId}/users")
    public ResponseEntity<List<UserLicense>> getLicenseUsers(@PathVariable Long licenseId) {
        try {
            return ResponseEntity.ok(licenseService.getLicenseUsers(licenseId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ===== Group License Assignment Endpoints =====
    
    @PostMapping("/{licenseId}/assign/group/{groupId}")
    public ResponseEntity<GroupLicense> assignLicenseToGroup(
            @PathVariable Long licenseId,
            @PathVariable Long groupId,
            @RequestBody Map<String, Object> request) {
        try {
            Integer allocatedSeats = (Integer) request.get("allocatedSeats");
            String notes = (String) request.get("notes");
            
            if (allocatedSeats == null || allocatedSeats <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            GroupLicense assignment = licenseService.assignLicenseToGroup(
                licenseId, groupId, allocatedSeats, notes);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/group-assignments/{groupLicenseId}")
    public ResponseEntity<Void> revokeLicenseFromGroup(@PathVariable Long groupLicenseId) {
        try {
            licenseService.revokeLicenseFromGroup(groupLicenseId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupLicense>> getGroupLicenses(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(licenseService.getGroupLicenses(groupId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{licenseId}/groups")
    public ResponseEntity<List<GroupLicense>> getLicenseGroups(@PathVariable Long licenseId) {
        try {
            return ResponseEntity.ok(licenseService.getLicenseGroups(licenseId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ===== History and Audit Endpoints =====
    
    @GetMapping("/{licenseId}/history")
    public ResponseEntity<List<LicenseHistory>> getLicenseHistory(@PathVariable Long licenseId) {
        return ResponseEntity.ok(licenseService.getLicenseHistory(licenseId));
    }
    
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<LicenseHistory>> getUserHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(licenseService.getUserHistory(userId));
    }
    
    @GetMapping("/history/recent")
    public ResponseEntity<List<LicenseHistory>> getRecentHistory() {
        return ResponseEntity.ok(licenseService.getRecentHistory());
    }
}
