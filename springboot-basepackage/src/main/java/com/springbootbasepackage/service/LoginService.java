package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import com.springbootbasepackage.dto.LoginIphoneDTO;

public interface LoginService {

     String tokenCreate(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO);

     String sendIphone(LoginIphoneDTO loginIphoneDTO);
}
