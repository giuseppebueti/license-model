package com.license.management.repository;

import com.license.management.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    
    Optional<UserGroup> findByName(String name);
    
    List<UserGroup> findByActiveTrue();
    
    List<UserGroup> findByActiveFalse();
    
    boolean existsByName(String name);
}
