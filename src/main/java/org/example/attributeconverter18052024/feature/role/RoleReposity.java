package org.example.attributeconverter18052024.feature.role;

import org.example.attributeconverter18052024.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleReposity extends JpaRepository<Role,String> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
