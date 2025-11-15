package com.license.management.service;

import com.license.management.entity.UserGroup;
import com.license.management.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserGroupService {
    
    @Autowired
    private UserGroupRepository groupRepository;
    
    public List<UserGroup> getAllGroups() {
        return groupRepository.findAll();
    }
    
    public Optional<UserGroup> getGroupById(Long id) {
        return groupRepository.findById(id);
    }
    
    public Optional<UserGroup> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }
    
    public List<UserGroup> getActiveGroups() {
        return groupRepository.findByActiveTrue();
    }
    
    public UserGroup createGroup(UserGroup group) {
        if (groupRepository.existsByName(group.getName())) {
            throw new RuntimeException("Group name already exists");
        }
        return groupRepository.save(group);
    }
    
    public UserGroup updateGroup(Long id, UserGroup groupDetails) {
        UserGroup group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        
        // Check for duplicate name if changed
        if (!group.getName().equals(groupDetails.getName()) && 
            groupRepository.existsByName(groupDetails.getName())) {
            throw new RuntimeException("Group name already exists");
        }
        
        group.setName(groupDetails.getName());
        group.setDescription(groupDetails.getDescription());
        group.setActive(groupDetails.getActive());
        
        return groupRepository.save(group);
    }
    
    public void deleteGroup(Long id) {
        UserGroup group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        groupRepository.delete(group);
    }
}
