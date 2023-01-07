package com.example.exswaggerrestdocs.service;

import com.example.exswaggerrestdocs.entity.Gender;
import com.example.exswaggerrestdocs.entity.User;
import com.example.exswaggerrestdocs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long saveUser(User user) {
        return userRepository.save(user).getId();
    }

    public void modifyName(Long userId, String name) {
        userRepository.findById(userId).orElseThrow().changeName(name);
    }

    public void removeUser(Long userID) {
        User user = userRepository.findById(userID).orElseThrow();
        userRepository.delete(user);
    }

    public User findUserBy(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public List<User> findUserBy(Gender gender) {
        return userRepository.findByGender(gender);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }
}
