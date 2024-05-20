package org.example.attributeconverter18052024.feature.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.example.attributeconverter18052024.feature.user.dto.UserRequest;
import org.example.attributeconverter18052024.feature.user.dto.UserResponse;
import org.example.attributeconverter18052024.utils.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class UserRestController {

    private final UserService userService;

    @GetMapping("")
    public BaseResponse<List<UserResponse>> getAllUser() {

    

        return BaseResponse.
                <List<UserResponse>>ok().
                setPayload(userService.getAllStudents());

    }


    @Operation(summary = "Register a new user",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = UserRequest.class),
                    examples = @ExampleObject(
                            value = """
                                    
                                    {
                                       "name" : "makara",
                                        "roles" : [
                                        "ADMIN", "USER"
                                        ]
                                    }
                                    """
                    )

            )
    ))


    @PostMapping("")
    public BaseResponse<UserResponse> registerUser(@RequestBody UserRequest userRequest) {

        return BaseResponse.
                <UserResponse>ok().
                setPayload(userService.registerUser(userRequest));

    }
}
