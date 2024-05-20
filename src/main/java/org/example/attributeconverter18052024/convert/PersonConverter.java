package org.example.attributeconverter18052024.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.attributeconverter18052024.feature.person.PersonName;
@Converter
public class PersonConverter implements AttributeConverter<PersonName,String> {

    private static final  String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(PersonName personName) {

        if (personName == null){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (personName.getSurname() != null && !personName.getSurname().isEmpty()){
            stringBuilder.append(personName.getSurname());
            stringBuilder.append(SEPARATOR);
        }

        if (personName.getName() != null && !personName.getName().isEmpty()){
            stringBuilder.append(personName.getName());
        }
        return  stringBuilder.toString();
    }

    @Override
    public PersonName convertToEntityAttribute(String dbData) {

        if (dbData == null || dbData.isEmpty()){
            return  null;
        }

        String[] split = dbData.split(SEPARATOR);
        if (split == null || split.length == 0)  {
            return  null;

        }

        PersonName personName = new PersonName();
        String firstPiece = !split[0].isEmpty() ? split[0] : null;
        if (dbData.contains(SEPARATOR)) {
            personName.setSurname(firstPiece);

            if (split.length >= 2 && split[1] != null
                    && !split[1].isEmpty()) {
                personName.setName(split[1]);
            }
        } else {
            personName.setName(firstPiece);
        }

        return personName;
    }

}
