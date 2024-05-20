package org.example.attributeconverter18052024.feature.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.example.attributeconverter18052024.domain.Person;
import org.example.attributeconverter18052024.repo.PersonReposity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
public class PersonRestController {
    private final PersonReposity personReposity;
    @GetMapping("")
    public List<Person> getAllPerson() {
        return personReposity.findAll();
    }

    @PostMapping("")
    @Operation(summary = "Create a new person",
                        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                content = @Content(schema = @Schema(implementation = Person.class),
                                examples = @ExampleObject(value = """
                                        {
                                            "personName": {
                                                "name": "John",
                                                "surname": "Doe"
                                            }
                                        }
                                        """))

                        )
    )

    public Person createPerson(@RequestBody Person person){
        System.out.println(person);
        return personReposity.save(person);
    }

}
