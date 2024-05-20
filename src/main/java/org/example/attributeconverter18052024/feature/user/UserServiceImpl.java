package org.example.attributeconverter18052024.feature.user;

import lombok.RequiredArgsConstructor;
import org.example.attributeconverter18052024.domain.Role;
import org.example.attributeconverter18052024.domain.User;
import org.example.attributeconverter18052024.feature.role.RoleReposity;
import org.example.attributeconverter18052024.feature.user.dto.UserRequest;
import org.example.attributeconverter18052024.feature.user.dto.UserResponse;
import org.example.attributeconverter18052024.mapper.MapperTo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final  UserRepository userRepository;
    private final RoleReposity roleReposity;
    private final MapperTo mapperTo;


    @Override
    public List<UserResponse> getAllStudents() {
        return userRepository.findAll().stream().map(mapperTo::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {

        if (userRepository.existsByName(userRequest.name())){
            throw new RuntimeException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        for (var role : userRequest.roles()) {
            var roleObj =  roleReposity.findByName(role).orElseThrow(()-> new RuntimeException("Role not found"));
            roles.add(roleObj);
        }
        var newUser = mapperTo.toUser(userRequest);


        newUser.setRoles(roles);


        System.out.println(userRepository.save(newUser));
        return mapperTo.toUserResponse(userRepository.save(newUser));
    }
}
