package com.amenodiscovery.movies.repository;

import com.amenodiscovery.movies.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
