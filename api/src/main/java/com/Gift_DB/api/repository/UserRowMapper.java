package com.Gift_DB.api.repository;

import com.Gift_DB.api.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setHashedPassword(rs.getString("hashedPassword"));
        user.setCreatedAt(rs.getString("createdAt"));
        user.setUpdatedAt(rs.getString("updatedAt"));

        return user;
    }
}
