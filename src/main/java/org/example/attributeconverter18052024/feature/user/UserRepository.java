package org.example.attributeconverter18052024.feature.user;

import org.example.attributeconverter18052024.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByName(String name);
    boolean existsByName(String name);
}
