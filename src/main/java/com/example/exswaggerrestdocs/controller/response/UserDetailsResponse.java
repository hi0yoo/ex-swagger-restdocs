package com.example.exswaggerrestdocs.controller.response;

import com.example.exswaggerrestdocs.entity.Gender;
import com.example.exswaggerrestdocs.entity.User;
import lombok.Data;

@Data
public class UserDetailsResponse {

    private Long userId;
    private String name;
    private int age;
    private Gender gender;

    public UserDetailsResponse(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.gender = user.getGender();
    }
}
