package org.example.attributeconverter18052024.feature.user.dto;

import lombok.Builder;
import java.util.Set;

@Builder
public record UserRequest(
        String name,
        Set<String> roles
) {
}
