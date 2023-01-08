package com.example.exswaggerrestdocs.controller.request;

import com.example.exswaggerrestdocs.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserAddRequest {

    @NotBlank
    private String name;

    @Range(min = 19, max = 30)
    private int age;

    private Gender gender;
}
