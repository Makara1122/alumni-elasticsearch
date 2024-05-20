package org.example.attributeconverter18052024.mapper;

import org.example.attributeconverter18052024.domain.Role;
import org.example.attributeconverter18052024.domain.User;
import org.example.attributeconverter18052024.feature.user.dto.UserRequest;
import org.example.attributeconverter18052024.feature.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MapperTo {

    @Mapping(target = "roles", source = "roles",qualifiedByName = "mapRoles")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUser(UserRequest userRequest);
    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles){
        return  roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
