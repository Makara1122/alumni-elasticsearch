package org.example.attributeconverter18052024.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.attributeconverter18052024.feature.person.PersonName;
import org.example.attributeconverter18052024.convert.PersonConverter;

@Entity(name = "person_tbl")
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
   @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Convert(converter = PersonConverter.class)
    private PersonName personName;
}
