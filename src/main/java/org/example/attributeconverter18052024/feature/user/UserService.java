package org.example.attributeconverter18052024.feature.user;

import org.example.attributeconverter18052024.feature.user.dto.UserRequest;
import org.example.attributeconverter18052024.feature.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllStudents();

    UserResponse registerUser(UserRequest userRequest);
}
