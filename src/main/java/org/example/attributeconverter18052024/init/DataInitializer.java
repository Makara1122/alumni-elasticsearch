package org.example.attributeconverter18052024.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.attributeconverter18052024.domain.Authority;
import org.example.attributeconverter18052024.domain.Role;
import org.example.attributeconverter18052024.feature.authority.AuthorityRepository;
import org.example.attributeconverter18052024.feature.role.RoleReposity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer {


    private final RoleReposity roleReposity;
    private final AuthorityRepository authorityRepository;
    @PostConstruct
    void initAuthority(){
        List<String> authorities = List.of("READ","WRITE","DELETE");
        if(authorityRepository.count()==0){
            authorities.forEach(auth -> {
                var authority = new Authority();
                authority.setName(auth);
                authorityRepository.save(authority);
            });
        }
    }

    @PostConstruct
    void roleInit(){
        List<String> roles = List.of("ADMIN","USER");
        if(roleReposity.count()==0){
            var allAuthorities =  new HashSet<>(authorityRepository.findAll());
            for(var role : roles){
                var roleObj = new Role();
                if(role.equals("ADMIN")){
                    roleObj.setAuthorities(allAuthorities);
                }else if (role.equals("USER")){
                    roleObj.setAuthorities(
                            allAuthorities.stream()
                                    .filter(auth -> auth.getName().equals("READ"))
                                    .collect(Collectors.toSet())
                    );
                }
                roleObj.setName(role);
                roleReposity.save(roleObj);
            }
        }
    }



}
