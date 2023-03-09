package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import com.springbootbasepackage.dto.LoginIphoneDTO;

public interface LoginService {


     String sendIphone(LoginIphoneDTO loginIphoneDTO);

     LoginIphoneAndYzmDTO login(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO);
}
