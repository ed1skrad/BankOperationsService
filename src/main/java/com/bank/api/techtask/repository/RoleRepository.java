package com.bank.api.techtask.repository;

import com.bank.api.techtask.domain.model.Role;
import com.bank.api.techtask.domain.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity.
 * Provides methods for basic CRUD operations and querying roles by name.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * @param roleName the name of the role to find.
     * @return an Optional containing the role if it exists, or an empty Optional if it does not.
     */
    Optional<Role> findByName(RoleEnum roleName);
}
