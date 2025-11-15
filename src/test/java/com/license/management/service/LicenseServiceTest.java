package com.license.management.service;

import com.license.management.entity.*;
import com.license.management.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LicenseService Unit Tests")
class LicenseServiceTest {

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository groupRepository;

    @Mock
    private UserLicenseRepository userLicenseRepository;

    @Mock
    private GroupLicenseRepository groupLicenseRepository;

    @Mock
    private LicenseHistoryRepository historyRepository;

    @InjectMocks
    private LicenseService licenseService;

    private License testLicense;
    private User testUser;
    private UserGroup testGroup;
    private UserLicense testUserLicense;
    private GroupLicense testGroupLicense;

    @BeforeEach
    void setUp() {
        // Setup test license
        testLicense = new License();
        testLicense.setId(1L);
        testLicense.setSoftwareName("Adobe Photoshop");
        testLicense.setLicenseKey("ABCD-EFGH-IJKL-MNOP");
        testLicense.setTotalSeats(10);
        testLicense.setUsedSeats(3);
        testLicense.setActive(true);
        testLicense.setExpirationDate(LocalDateTime.now().plusYears(1));
        testLicense.setDescription("Creative Cloud License");

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john.doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setFullName("John Doe");
        testUser.setActive(true);

        // Setup test group
        testGroup = new UserGroup();
        testGroup.setId(1L);
        testGroup.setName("Engineering Team");
        testGroup.setDescription("Software engineering team");
        testGroup.setActive(true);

        // Setup test user license
        testUserLicense = new UserLicense();
        testUserLicense.setId(1L);
        testUserLicense.setUser(testUser);
        testUserLicense.setLicense(testLicense);
        testUserLicense.setActive(true);
        testUserLicense.setNotes("Initial assignment");

        // Setup test group license
        testGroupLicense = new GroupLicense();
        testGroupLicense.setId(1L);
        testGroupLicense.setGroup(testGroup);
        testGroupLicense.setLicense(testLicense);
        testGroupLicense.setAllocatedSeats(5);
        testGroupLicense.setActive(true);
        testGroupLicense.setNotes("Team allocation");
    }

    // ===== License CRUD Operations Tests =====

    @Test
    @DisplayName("Should return all licenses")
    void testGetAllLicenses() {
        // Arrange
        List<License> licenses = Arrays.asList(testLicense, new License());
        when(licenseRepository.findAll()).thenReturn(licenses);

        // Act
        List<License> result = licenseService.getAllLicenses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(licenseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return license by ID when found")
    void testGetLicenseById_Found() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));

        // Act
        Optional<License> result = licenseService.getLicenseById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Adobe Photoshop", result.get().getSoftwareName());
        verify(licenseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when license not found")
    void testGetLicenseById_NotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<License> result = licenseService.getLicenseById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(licenseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should return only active licenses")
    void testGetActiveLicenses() {
        // Arrange
        List<License> activeLicenses = Arrays.asList(testLicense);
        when(licenseRepository.findByActiveTrue()).thenReturn(activeLicenses);

        // Act
        List<License> result = licenseService.getActiveLicenses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(licenseRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Should return only available licenses")
    void testGetAvailableLicenses() {
        // Arrange
        List<License> availableLicenses = Arrays.asList(testLicense);
        when(licenseRepository.findAvailableLicenses()).thenReturn(availableLicenses);

        // Act
        List<License> result = licenseService.getAvailableLicenses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(licenseRepository, times(1)).findAvailableLicenses();
    }

    @Test
    @DisplayName("Should create license successfully")
    void testCreateLicense_Success() {
        // Arrange
        License newLicense = new License();
        newLicense.setSoftwareName("Microsoft Office");
        newLicense.setLicenseKey("WXYZ-1234-5678-9012");
        newLicense.setTotalSeats(20);
        newLicense.setExpirationDate(LocalDateTime.now().plusYears(1));

        when(licenseRepository.existsByLicenseKey(anyString())).thenReturn(false);
        when(licenseRepository.save(any(License.class))).thenReturn(newLicense);

        // Act
        License result = licenseService.createLicense(newLicense);

        // Assert
        assertNotNull(result);
        assertEquals("Microsoft Office", result.getSoftwareName());
        verify(licenseRepository, times(1)).existsByLicenseKey(anyString());
        verify(licenseRepository, times(1)).save(any(License.class));
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when creating license with duplicate key")
    void testCreateLicense_DuplicateKey() {
        // Arrange
        when(licenseRepository.existsByLicenseKey("ABCD-EFGH-IJKL-MNOP")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.createLicense(testLicense);
        });

        assertEquals("License key already exists", exception.getMessage());
        verify(licenseRepository, times(1)).existsByLicenseKey(anyString());
        verify(licenseRepository, never()).save(any(License.class));
    }

    @Test
    @DisplayName("Should update license successfully")
    void testUpdateLicense_Success() {
        // Arrange
        License updatedDetails = new License();
        updatedDetails.setSoftwareName("Adobe Photoshop CC");
        updatedDetails.setTotalSeats(15);
        updatedDetails.setExpirationDate(LocalDateTime.now().plusYears(2));
        updatedDetails.setActive(true);
        updatedDetails.setDescription("Updated license");

        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(licenseRepository.save(any(License.class))).thenReturn(testLicense);

        // Act
        License result = licenseService.updateLicense(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Adobe Photoshop CC", result.getSoftwareName());
        assertEquals(15, result.getTotalSeats());
        verify(licenseRepository, times(1)).findById(1L);
        verify(licenseRepository, times(1)).save(any(License.class));
        verify(historyRepository, times(2)).save(any(LicenseHistory.class)); // One for update, one for seat change
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent license")
    void testUpdateLicense_NotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.updateLicense(999L, testLicense);
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
        verify(licenseRepository, never()).save(any(License.class));
    }

    @Test
    @DisplayName("Should delete license successfully")
    void testDeleteLicense_Success() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));

        // Act
        licenseService.deleteLicense(1L);

        // Assert
        verify(licenseRepository, times(1)).findById(1L);
        verify(licenseRepository, times(1)).delete(testLicense);
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent license")
    void testDeleteLicense_NotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.deleteLicense(999L);
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
        verify(licenseRepository, never()).delete(any(License.class));
    }

    // ===== User License Assignment Tests =====

    @Test
    @DisplayName("Should assign license to user successfully")
    void testAssignLicenseToUser_Success() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userLicenseRepository.existsByUserAndLicenseAndActiveTrue(testUser, testLicense)).thenReturn(false);
        when(userLicenseRepository.save(any(UserLicense.class))).thenReturn(testUserLicense);

        // Act
        UserLicense result = licenseService.assignLicenseToUser(1L, 1L, "Test assignment");

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testLicense, result.getLicense());
        assertEquals(4, testLicense.getUsedSeats()); // Should increment from 3 to 4
        verify(licenseRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userLicenseRepository, times(1)).save(any(UserLicense.class));
        verify(licenseRepository, times(1)).save(testLicense);
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when license not found during assignment")
    void testAssignLicenseToUser_LicenseNotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToUser(999L, 1L, "Test");
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
        verify(userLicenseRepository, never()).save(any(UserLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during assignment")
    void testAssignLicenseToUser_UserNotFound() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToUser(1L, 999L, "Test");
        });

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository, times(1)).findById(999L);
        verify(userLicenseRepository, never()).save(any(UserLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when license already assigned to user")
    void testAssignLicenseToUser_AlreadyAssigned() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userLicenseRepository.existsByUserAndLicenseAndActiveTrue(testUser, testLicense)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToUser(1L, 1L, "Test");
        });

        assertEquals("License already assigned to this user", exception.getMessage());
        verify(userLicenseRepository, never()).save(any(UserLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when no available seats")
    void testAssignLicenseToUser_NoSeatsAvailable() {
        // Arrange
        testLicense.setUsedSeats(10); // All seats used
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userLicenseRepository.existsByUserAndLicenseAndActiveTrue(testUser, testLicense)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToUser(1L, 1L, "Test");
        });

        assertEquals("No available seats for this license", exception.getMessage());
        verify(userLicenseRepository, never()).save(any(UserLicense.class));
    }

    @Test
    @DisplayName("Should revoke license from user successfully")
    void testRevokeLicenseFromUser_Success() {
        // Arrange
        when(userLicenseRepository.findById(1L)).thenReturn(Optional.of(testUserLicense));

        // Act
        licenseService.revokeLicenseFromUser(1L);

        // Assert
        assertFalse(testUserLicense.getActive());
        assertNotNull(testUserLicense.getRevokedAt());
        assertEquals(2, testLicense.getUsedSeats()); // Should decrement from 3 to 2
        verify(userLicenseRepository, times(1)).findById(1L);
        verify(userLicenseRepository, times(1)).save(testUserLicense);
        verify(licenseRepository, times(1)).save(testLicense);
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when revoking non-existent user license")
    void testRevokeLicenseFromUser_NotFound() {
        // Arrange
        when(userLicenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.revokeLicenseFromUser(999L);
        });

        assertEquals("User license not found with id: 999", exception.getMessage());
        verify(userLicenseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get user licenses successfully")
    void testGetUserLicenses_Success() {
        // Arrange
        List<UserLicense> userLicenses = Arrays.asList(testUserLicense);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userLicenseRepository.findByUserAndActiveTrue(testUser)).thenReturn(userLicenses);

        // Act
        List<UserLicense> result = licenseService.getUserLicenses(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserLicense, result.get(0));
        verify(userRepository, times(1)).findById(1L);
        verify(userLicenseRepository, times(1)).findByUserAndActiveTrue(testUser);
    }

    @Test
    @DisplayName("Should throw exception when getting licenses for non-existent user")
    void testGetUserLicenses_UserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.getUserLicenses(999L);
        });

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get license users successfully")
    void testGetLicenseUsers_Success() {
        // Arrange
        List<UserLicense> userLicenses = Arrays.asList(testUserLicense);
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(userLicenseRepository.findByLicenseAndActiveTrue(testLicense)).thenReturn(userLicenses);

        // Act
        List<UserLicense> result = licenseService.getLicenseUsers(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(licenseRepository, times(1)).findById(1L);
        verify(userLicenseRepository, times(1)).findByLicenseAndActiveTrue(testLicense);
    }

    @Test
    @DisplayName("Should throw exception when getting users for non-existent license")
    void testGetLicenseUsers_LicenseNotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.getLicenseUsers(999L);
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
    }

    // ===== Group License Assignment Tests =====

    @Test
    @DisplayName("Should assign license to group successfully")
    void testAssignLicenseToGroup_Success() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(groupLicenseRepository.existsByGroupAndLicenseAndActiveTrue(testGroup, testLicense)).thenReturn(false);
        when(groupLicenseRepository.save(any(GroupLicense.class))).thenReturn(testGroupLicense);

        // Act
        GroupLicense result = licenseService.assignLicenseToGroup(1L, 1L, 5, "Team allocation");

        // Assert
        assertNotNull(result);
        assertEquals(testGroup, result.getGroup());
        assertEquals(testLicense, result.getLicense());
        assertEquals(5, result.getAllocatedSeats());
        assertEquals(8, testLicense.getUsedSeats()); // Should increment from 3 to 8
        verify(licenseRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).findById(1L);
        verify(groupLicenseRepository, times(1)).save(any(GroupLicense.class));
        verify(licenseRepository, times(1)).save(testLicense);
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when license not found during group assignment")
    void testAssignLicenseToGroup_LicenseNotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToGroup(999L, 1L, 5, "Test");
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
        verify(groupLicenseRepository, never()).save(any(GroupLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when group not found during assignment")
    void testAssignLicenseToGroup_GroupNotFound() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToGroup(1L, 999L, 5, "Test");
        });

        assertEquals("Group not found with id: 999", exception.getMessage());
        verify(groupRepository, times(1)).findById(999L);
        verify(groupLicenseRepository, never()).save(any(GroupLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when license already assigned to group")
    void testAssignLicenseToGroup_AlreadyAssigned() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(groupLicenseRepository.existsByGroupAndLicenseAndActiveTrue(testGroup, testLicense)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToGroup(1L, 1L, 5, "Test");
        });

        assertEquals("License already assigned to this group", exception.getMessage());
        verify(groupLicenseRepository, never()).save(any(GroupLicense.class));
    }

    @Test
    @DisplayName("Should throw exception when not enough seats for group allocation")
    void testAssignLicenseToGroup_NotEnoughSeats() {
        // Arrange
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(groupLicenseRepository.existsByGroupAndLicenseAndActiveTrue(testGroup, testLicense)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.assignLicenseToGroup(1L, 1L, 10, "Test"); // Requesting 10 but only 7 available
        });

        assertEquals("Not enough available seats for this allocation", exception.getMessage());
        verify(groupLicenseRepository, never()).save(any(GroupLicense.class));
    }

    @Test
    @DisplayName("Should revoke license from group successfully")
    void testRevokeLicenseFromGroup_Success() {
        // Arrange
        when(groupLicenseRepository.findById(1L)).thenReturn(Optional.of(testGroupLicense));

        // Act
        licenseService.revokeLicenseFromGroup(1L);

        // Assert
        assertFalse(testGroupLicense.getActive());
        assertNotNull(testGroupLicense.getRevokedAt());
        assertEquals(0, testLicense.getUsedSeats()); // Should decrement from 3 by 5 to 0 (not negative)
        verify(groupLicenseRepository, times(1)).findById(1L);
        verify(groupLicenseRepository, times(1)).save(testGroupLicense);
        verify(licenseRepository, times(1)).save(testLicense);
        verify(historyRepository, times(1)).save(any(LicenseHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when revoking non-existent group license")
    void testRevokeLicenseFromGroup_NotFound() {
        // Arrange
        when(groupLicenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.revokeLicenseFromGroup(999L);
        });

        assertEquals("Group license not found with id: 999", exception.getMessage());
        verify(groupLicenseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get group licenses successfully")
    void testGetGroupLicenses_Success() {
        // Arrange
        List<GroupLicense> groupLicenses = Arrays.asList(testGroupLicense);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        when(groupLicenseRepository.findByGroupAndActiveTrue(testGroup)).thenReturn(groupLicenses);

        // Act
        List<GroupLicense> result = licenseService.getGroupLicenses(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testGroupLicense, result.get(0));
        verify(groupRepository, times(1)).findById(1L);
        verify(groupLicenseRepository, times(1)).findByGroupAndActiveTrue(testGroup);
    }

    @Test
    @DisplayName("Should throw exception when getting licenses for non-existent group")
    void testGetGroupLicenses_GroupNotFound() {
        // Arrange
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.getGroupLicenses(999L);
        });

        assertEquals("Group not found with id: 999", exception.getMessage());
        verify(groupRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get license groups successfully")
    void testGetLicenseGroups_Success() {
        // Arrange
        List<GroupLicense> groupLicenses = Arrays.asList(testGroupLicense);
        when(licenseRepository.findById(1L)).thenReturn(Optional.of(testLicense));
        when(groupLicenseRepository.findByLicenseAndActiveTrue(testLicense)).thenReturn(groupLicenses);

        // Act
        List<GroupLicense> result = licenseService.getLicenseGroups(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(licenseRepository, times(1)).findById(1L);
        verify(groupLicenseRepository, times(1)).findByLicenseAndActiveTrue(testLicense);
    }

    @Test
    @DisplayName("Should throw exception when getting groups for non-existent license")
    void testGetLicenseGroups_LicenseNotFound() {
        // Arrange
        when(licenseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            licenseService.getLicenseGroups(999L);
        });

        assertEquals("License not found with id: 999", exception.getMessage());
        verify(licenseRepository, times(1)).findById(999L);
    }

    // ===== History and Audit Tests =====

    @Test
    @DisplayName("Should get license history successfully")
    void testGetLicenseHistory_Success() {
        // Arrange
        LicenseHistory history1 = new LicenseHistory();
        history1.setId(1L);
        history1.setLicenseId(1L);
        history1.setActionType(LicenseHistory.ActionType.LICENSE_CREATED);
        history1.setDescription("License created");

        LicenseHistory history2 = new LicenseHistory();
        history2.setId(2L);
        history2.setLicenseId(1L);
        history2.setActionType(LicenseHistory.ActionType.LICENSE_UPDATED);
        history2.setDescription("License updated");

        List<LicenseHistory> historyList = Arrays.asList(history2, history1); // Ordered by timestamp desc
        when(historyRepository.findByLicenseIdOrderByTimestampDesc(1L)).thenReturn(historyList);

        // Act
        List<LicenseHistory> result = licenseService.getLicenseHistory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LicenseHistory.ActionType.LICENSE_UPDATED, result.get(0).getActionType());
        verify(historyRepository, times(1)).findByLicenseIdOrderByTimestampDesc(1L);
    }

    @Test
    @DisplayName("Should get user history successfully")
    void testGetUserHistory_Success() {
        // Arrange
        LicenseHistory history = new LicenseHistory();
        history.setId(1L);
        history.setUserId(1L);
        history.setLicenseId(1L);
        history.setActionType(LicenseHistory.ActionType.LICENSE_ASSIGNED_TO_USER);
        history.setDescription("License assigned to user");

        List<LicenseHistory> historyList = Arrays.asList(history);
        when(historyRepository.findByUserIdOrderByTimestampDesc(1L)).thenReturn(historyList);

        // Act
        List<LicenseHistory> result = licenseService.getUserHistory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(historyRepository, times(1)).findByUserIdOrderByTimestampDesc(1L);
    }

    @Test
    @DisplayName("Should get recent history successfully")
    void testGetRecentHistory_Success() {
        // Arrange
        LicenseHistory history1 = new LicenseHistory();
        history1.setId(1L);
        history1.setActionType(LicenseHistory.ActionType.LICENSE_CREATED);

        LicenseHistory history2 = new LicenseHistory();
        history2.setId(2L);
        history2.setActionType(LicenseHistory.ActionType.LICENSE_UPDATED);

        List<LicenseHistory> historyList = Arrays.asList(history2, history1);
        when(historyRepository.findTop50ByOrderByTimestampDesc()).thenReturn(historyList);

        // Act
        List<LicenseHistory> result = licenseService.getRecentHistory();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(historyRepository, times(1)).findTop50ByOrderByTimestampDesc();
    }

    // ===== Additional Edge Cases =====

    @Test
    @DisplayName("Should handle seat calculation correctly")
    void testSeatCalculation() {
        // Arrange
        License license = new License();
        license.setTotalSeats(100);
        license.setUsedSeats(40);

        // Act
        Integer availableSeats = license.getAvailableSeats();

        // Assert
        assertEquals(60, availableSeats);
    }

    @Test
    @DisplayName("Should verify history entry is created with correct data on license creation")
    void testHistoryEntryOnCreate() {
        // Arrange
        License newLicense = new License();
        newLicense.setId(5L);
        newLicense.setSoftwareName("Test Software");
        newLicense.setLicenseKey("TEST-KEY");
        newLicense.setTotalSeats(50);
        newLicense.setExpirationDate(LocalDateTime.now().plusMonths(6));

        when(licenseRepository.existsByLicenseKey(anyString())).thenReturn(false);
        when(licenseRepository.save(any(License.class))).thenReturn(newLicense);

        ArgumentCaptor<LicenseHistory> historyCaptor = ArgumentCaptor.forClass(LicenseHistory.class);

        // Act
        licenseService.createLicense(newLicense);

        // Assert
        verify(historyRepository).save(historyCaptor.capture());
        LicenseHistory capturedHistory = historyCaptor.getValue();
        assertEquals(LicenseHistory.ActionType.LICENSE_CREATED, capturedHistory.getActionType());
        assertEquals(5L, capturedHistory.getLicenseId());
        assertTrue(capturedHistory.getDescription().contains("Test Software"));
    }
}
