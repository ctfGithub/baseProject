package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> queryByUser(UserDTO user);
}
