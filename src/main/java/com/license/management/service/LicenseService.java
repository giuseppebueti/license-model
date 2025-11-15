package com.license.management.service;

import com.license.management.entity.*;
import com.license.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LicenseService {
    
    @Autowired
    private LicenseRepository licenseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserGroupRepository groupRepository;
    
    @Autowired
    private UserLicenseRepository userLicenseRepository;
    
    @Autowired
    private GroupLicenseRepository groupLicenseRepository;
    
    @Autowired
    private LicenseHistoryRepository historyRepository;
    
    // ===== License CRUD Operations =====
    
    public List<License> getAllLicenses() {
        return licenseRepository.findAll();
    }
    
    public Optional<License> getLicenseById(Long id) {
        return licenseRepository.findById(id);
    }
    
    public List<License> getActiveLicenses() {
        return licenseRepository.findByActiveTrue();
    }
    
    public List<License> getAvailableLicenses() {
        return licenseRepository.findAvailableLicenses();
    }
    
    public License createLicense(License license) {
        if (licenseRepository.existsByLicenseKey(license.getLicenseKey())) {
            throw new RuntimeException("License key already exists");
        }
        License savedLicense = licenseRepository.save(license);
        createHistoryEntry(savedLicense.getId(), null, null, 
            LicenseHistory.ActionType.LICENSE_CREATED,
            "License created: " + license.getSoftwareName(),
            String.format("Total seats: %d, Expiration: %s", 
                license.getTotalSeats(), license.getExpirationDate()));
        return savedLicense;
    }
    
    public License updateLicense(Long id, License licenseDetails) {
        License license = licenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + id));
        
        boolean seatsChanged = !license.getTotalSeats().equals(licenseDetails.getTotalSeats());
        Integer oldSeats = license.getTotalSeats();
        
        license.setSoftwareName(licenseDetails.getSoftwareName());
        license.setTotalSeats(licenseDetails.getTotalSeats());
        license.setExpirationDate(licenseDetails.getExpirationDate());
        license.setActive(licenseDetails.getActive());
        license.setDescription(licenseDetails.getDescription());
        
        License updatedLicense = licenseRepository.save(license);
        
        createHistoryEntry(license.getId(), null, null,
            LicenseHistory.ActionType.LICENSE_UPDATED,
            "License updated: " + license.getSoftwareName(),
            "License details modified");
        
        if (seatsChanged) {
            LicenseHistory.ActionType actionType = licenseDetails.getTotalSeats() > oldSeats ?
                LicenseHistory.ActionType.SEATS_INCREASED : LicenseHistory.ActionType.SEATS_DECREASED;
            createHistoryEntry(license.getId(), null, null, actionType,
                String.format("Total seats changed from %d to %d", oldSeats, licenseDetails.getTotalSeats()),
                null);
        }
        
        return updatedLicense;
    }
    
    public void deleteLicense(Long id) {
        License license = licenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + id));
        createHistoryEntry(license.getId(), null, null,
            LicenseHistory.ActionType.LICENSE_DELETED,
            "License deleted: " + license.getSoftwareName(),
            null);
        licenseRepository.delete(license);
    }
    
    // ===== User License Assignment =====
    
    public UserLicense assignLicenseToUser(Long licenseId, Long userId, String notes) {
        License license = licenseRepository.findById(licenseId)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + licenseId));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Check if already assigned
        if (userLicenseRepository.existsByUserAndLicenseAndActiveTrue(user, license)) {
            throw new RuntimeException("License already assigned to this user");
        }
        
        // Check available seats
        if (license.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this license");
        }
        
        UserLicense userLicense = new UserLicense();
        userLicense.setUser(user);
        userLicense.setLicense(license);
        userLicense.setNotes(notes);
        userLicense.setActive(true);
        
        UserLicense saved = userLicenseRepository.save(userLicense);
        
        // Update used seats
        license.setUsedSeats(license.getUsedSeats() + 1);
        licenseRepository.save(license);
        
        createHistoryEntry(licenseId, userId, null,
            LicenseHistory.ActionType.LICENSE_ASSIGNED_TO_USER,
            String.format("License assigned to user: %s", user.getUsername()),
            notes);
        
        return saved;
    }
    
    public void revokeLicenseFromUser(Long userLicenseId) {
        UserLicense userLicense = userLicenseRepository.findById(userLicenseId)
            .orElseThrow(() -> new RuntimeException("User license not found with id: " + userLicenseId));
        
        userLicense.setActive(false);
        userLicense.setRevokedAt(LocalDateTime.now());
        userLicenseRepository.save(userLicense);
        
        // Update used seats
        License license = userLicense.getLicense();
        license.setUsedSeats(Math.max(0, license.getUsedSeats() - 1));
        licenseRepository.save(license);
        
        createHistoryEntry(license.getId(), userLicense.getUser().getId(), null,
            LicenseHistory.ActionType.LICENSE_REVOKED_FROM_USER,
            String.format("License revoked from user: %s", userLicense.getUser().getUsername()),
            null);
    }
    
    public List<UserLicense> getUserLicenses(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return userLicenseRepository.findByUserAndActiveTrue(user);
    }
    
    public List<UserLicense> getLicenseUsers(Long licenseId) {
        License license = licenseRepository.findById(licenseId)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + licenseId));
        return userLicenseRepository.findByLicenseAndActiveTrue(license);
    }
    
    // ===== Group License Assignment =====
    
    public GroupLicense assignLicenseToGroup(Long licenseId, Long groupId, Integer allocatedSeats, String notes) {
        License license = licenseRepository.findById(licenseId)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + licenseId));
        UserGroup group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        
        // Check if already assigned
        if (groupLicenseRepository.existsByGroupAndLicenseAndActiveTrue(group, license)) {
            throw new RuntimeException("License already assigned to this group");
        }
        
        // Check available seats
        if (license.getAvailableSeats() < allocatedSeats) {
            throw new RuntimeException("Not enough available seats for this allocation");
        }
        
        GroupLicense groupLicense = new GroupLicense();
        groupLicense.setGroup(group);
        groupLicense.setLicense(license);
        groupLicense.setAllocatedSeats(allocatedSeats);
        groupLicense.setNotes(notes);
        groupLicense.setActive(true);
        
        GroupLicense saved = groupLicenseRepository.save(groupLicense);
        
        // Update used seats
        license.setUsedSeats(license.getUsedSeats() + allocatedSeats);
        licenseRepository.save(license);
        
        createHistoryEntry(licenseId, null, groupId,
            LicenseHistory.ActionType.LICENSE_ASSIGNED_TO_GROUP,
            String.format("License assigned to group: %s (%d seats)", group.getName(), allocatedSeats),
            notes);
        
        return saved;
    }
    
    public void revokeLicenseFromGroup(Long groupLicenseId) {
        GroupLicense groupLicense = groupLicenseRepository.findById(groupLicenseId)
            .orElseThrow(() -> new RuntimeException("Group license not found with id: " + groupLicenseId));
        
        Integer allocatedSeats = groupLicense.getAllocatedSeats();
        groupLicense.setActive(false);
        groupLicense.setRevokedAt(LocalDateTime.now());
        groupLicenseRepository.save(groupLicense);
        
        // Update used seats
        License license = groupLicense.getLicense();
        license.setUsedSeats(Math.max(0, license.getUsedSeats() - allocatedSeats));
        licenseRepository.save(license);
        
        createHistoryEntry(license.getId(), null, groupLicense.getGroup().getId(),
            LicenseHistory.ActionType.LICENSE_REVOKED_FROM_GROUP,
            String.format("License revoked from group: %s (%d seats freed)", 
                groupLicense.getGroup().getName(), allocatedSeats),
            null);
    }
    
    public List<GroupLicense> getGroupLicenses(Long groupId) {
        UserGroup group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        return groupLicenseRepository.findByGroupAndActiveTrue(group);
    }
    
    public List<GroupLicense> getLicenseGroups(Long licenseId) {
        License license = licenseRepository.findById(licenseId)
            .orElseThrow(() -> new RuntimeException("License not found with id: " + licenseId));
        return groupLicenseRepository.findByLicenseAndActiveTrue(license);
    }
    
    // ===== History and Audit =====
    
    public List<LicenseHistory> getLicenseHistory(Long licenseId) {
        return historyRepository.findByLicenseIdOrderByTimestampDesc(licenseId);
    }
    
    public List<LicenseHistory> getUserHistory(Long userId) {
        return historyRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    
    public List<LicenseHistory> getRecentHistory() {
        return historyRepository.findTop50ByOrderByTimestampDesc();
    }
    
    private void createHistoryEntry(Long licenseId, Long userId, Long groupId,
                                   LicenseHistory.ActionType actionType,
                                   String description, String details) {
        LicenseHistory history = new LicenseHistory();
        history.setLicenseId(licenseId);
        history.setUserId(userId);
        history.setGroupId(groupId);
        history.setActionType(actionType);
        history.setDescription(description);
        history.setDetails(details);
        history.setPerformedBy("system"); // In production, get from security context
        historyRepository.save(history);
    }
}
