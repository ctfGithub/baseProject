package com.springbootbasepackage.service.serviceImpl;

import com.springbootbasepackage.dto.UserDTO;
import com.springbootbasepackage.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private com.springbootbasepackage.dao.UserDAO userDAO;


    @Override
    public List<UserDTO> queryByUser(UserDTO user) {
        return userDAO.queryByUser(user);
    }
}
