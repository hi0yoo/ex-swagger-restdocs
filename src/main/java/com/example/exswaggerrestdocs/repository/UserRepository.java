package com.example.exswaggerrestdocs.repository;

import com.example.exswaggerrestdocs.entity.Gender;
import com.example.exswaggerrestdocs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByGender(Gender gender);
}
