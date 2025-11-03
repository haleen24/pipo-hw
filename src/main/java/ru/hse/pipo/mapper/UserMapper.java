package ru.hse.pipo.mapper;


import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.hse.inventory.model.RegisterRequest;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    default User fromRegisterRequest(RegisterRequest registerRequest, List<GrantedAuthority> authorities) {
        return new User(registerRequest.getUsername(), registerRequest.getPassword(), authorities);
    }
}
