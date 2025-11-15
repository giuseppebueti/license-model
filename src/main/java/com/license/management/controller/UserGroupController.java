package com.license.management.controller;

import com.license.management.entity.UserGroup;
import com.license.management.service.UserGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class UserGroupController {
    
    @Autowired
    private UserGroupService groupService;
    
    @GetMapping
    public ResponseEntity<List<UserGroup>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<UserGroup> getGroupByName(@PathVariable String name) {
        return groupService.getGroupByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<UserGroup>> getActiveGroups() {
        return ResponseEntity.ok(groupService.getActiveGroups());
    }
    
    @PostMapping
    public ResponseEntity<UserGroup> createGroup(@Valid @RequestBody UserGroup group) {
        try {
            UserGroup created = groupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> updateGroup(@PathVariable Long id, @Valid @RequestBody UserGroup group) {
        try {
            UserGroup updated = groupService.updateGroup(id, group);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        try {
            groupService.deleteGroup(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
