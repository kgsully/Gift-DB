package com.Gift_DB.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gift_DB.api.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Map<String, Object>> getUserByUserName(String userName) {
        return userRepository.getUserByUserName(userName);
    }

}
