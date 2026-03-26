package com.bpi.java.training.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bpi.java.training.dashboard.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("SELECT r.name FROM Role r WHERE r.user.id = :userId")
	List<String> findRoleByUserId(@Param("userId") Long userId);
}
