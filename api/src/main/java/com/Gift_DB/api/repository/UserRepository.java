package com.Gift_DB.api.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepository {

    // Using constructor injection instead of @Autowired for attaching JdbcTemplate for DB Queries
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Perform DB queries based upon a passed in credential
    public Map<String, Object> getUserByCredential(String credential) {
        String query = "SELECT * FROM users "
                + "WHERE (username = ? OR email = ?) AND (active = true)";

        // Expected result will be a single record as the username field has a unique constraint
        // Could alternatively use the queryForObject, specifying a row mapper
        return jdbcTemplate.queryForMap(query, credential, credential);
    }

    // Perform DB queries based upon a passed in user ID
    public Map<String, Object> getUserById(int userId) {
        String query = "SELECT * FROM users "
                + "WHERE id = ? AND active = true";

        // Expected result will be a single record as the username field has a unique constraint
        // Could alternatively use the queryForObject, specifying a row mapper
        return jdbcTemplate.queryForMap(query, userId);
    }

    // Perform DB queries based upon a passed in user ID
    public Map<String, Object> getCurrentUserById(int userId) {
        String query = "SELECT id, userName, email, accountAdmin, linkedTo, active, name FROM users"
                + "WHERE id = ? AND active = true";

        // Expected result will be a single record as the username field has a unique constraint
        // Could alternatively use the queryForObject, specifying a row mapper
        return jdbcTemplate.queryForMap(query, userId);
    }

}

// --------------------------------------------------------------------------------------------------------------------
// Notes:
// --------------------------------------------------------------------------------------------------------------------

// getUserLogin method using the User class
// public User getUserLogin(String credential) {
//     String query = "SELECT * FROM users "
//                  + "WHERE username = ? OR email = ?";
//
//     // Expected result will be a single record as the username field has a unique constraint
//     // Could alternatively use the queryForMap to return a hashmap object
//     // without using a model.  ---> System.out.println(jdbcTemplate.queryForMap(query, new Object[] {userName}));
//     return jdbcTemplate.queryForObject(query, new UserRowMapper(), credential, credential);
// }