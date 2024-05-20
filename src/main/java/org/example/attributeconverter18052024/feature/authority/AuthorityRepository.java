package org.example.attributeconverter18052024.feature.authority;

import org.example.attributeconverter18052024.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {
}
