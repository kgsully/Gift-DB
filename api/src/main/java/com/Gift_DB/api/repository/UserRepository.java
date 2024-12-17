package com.Gift_DB.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getUserByUserName(String userName) {
        String query = "SELECT * FROM users "
                     + "WHERE username = ? ";
        return jdbcTemplate.queryForList(query, new Object[] {userName});
    }

}
