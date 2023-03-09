package com.springbootbasepackage.dao;

import com.springbootbasepackage.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO {

    List<UserDTO> queryByUser(UserDTO user);

}
