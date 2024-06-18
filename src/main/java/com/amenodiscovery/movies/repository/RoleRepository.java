package com.amenodiscovery.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.movies.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
