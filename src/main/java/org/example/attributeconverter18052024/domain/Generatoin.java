package org.example.attributeconverter18052024.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Generatoin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nameType;
    private Integer Generation;
}
