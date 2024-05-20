package org.example.attributeconverter18052024.repo;

import org.example.attributeconverter18052024.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonReposity extends JpaRepository<Person,String> {
}
