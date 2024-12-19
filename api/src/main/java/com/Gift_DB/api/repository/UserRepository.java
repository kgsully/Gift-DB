package com.Gift_DB.api.repository;

import com.Gift_DB.api.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    // Attach the JdbcTemplate for use in querying the DB
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserLogin(String credential) {
        String query = "SELECT * FROM users "
                     + "WHERE username = ? OR email = ?";

        // Expected result will be a single record as the username field has a unique constraint
        // Could alternatively use the queryForMap to return a hashmap object
        // without using a model.  ---> System.out.println(jdbcTemplate.queryForMap(query, new Object[] {userName}));
        return jdbcTemplate.queryForObject(query, new UserRowMapper(), credential, credential);

    }

}
