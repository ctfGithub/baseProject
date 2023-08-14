package com.springbootbasepackage.service.serviceImpl;

import com.springbootbasepackage.service.TestService;
import com.springbootbasepackage.util.DateNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Resource
    private DateNumberUtil dateNumberUtil;

    @Override
    public Integer ceshi1() {
        dateNumberUtil.YearMonthDayNumber();
        return null;
    }
}
