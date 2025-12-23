package com.xcodeleon.userapp4.service;

import com.xcodeleon.userapp4.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(UUID id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(UUID id, UserDTO userDTO);

    void delete(UUID id);

    UserDTO getUserByLoginAndPassword(String login, String password);
}
