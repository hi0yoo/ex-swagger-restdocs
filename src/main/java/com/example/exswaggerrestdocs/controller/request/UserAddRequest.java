package com.example.exswaggerrestdocs.controller.request;

import com.example.exswaggerrestdocs.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAddRequest {

    private String name;
    private int age;
    private Gender gender;
}
