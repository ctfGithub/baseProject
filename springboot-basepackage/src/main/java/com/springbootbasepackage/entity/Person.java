package com.springbootbasepackage.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Integer id;
    private String name;
    private Integer age;
    private Date birthday;
}
