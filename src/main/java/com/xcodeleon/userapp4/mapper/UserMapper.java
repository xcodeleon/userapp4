package com.xcodeleon.userapp4.mapper;

import com.xcodeleon.userapp4.dto.UserDTO;
import com.xcodeleon.userapp4.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "login", target = "name")
    UserDTO toDTO(UserEntity entity);

    @Mapping(source = "name", target = "login")
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    UserEntity toEntity(UserDTO dto);

    @Named("hashPassword")
    default String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
