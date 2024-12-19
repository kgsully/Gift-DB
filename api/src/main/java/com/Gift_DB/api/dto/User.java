package com.Gift_DB.api.dto;

//import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String email;
    private String hashedPassword;
    // Need to look into making these LocalDateTime data type. Was having issues with the row mapper
    // in that it would return an empty object (via the catch block). Currently changed to string to
    // verify that the query results are coming back from the DB properly
    private String createdAt;
    private String updatedAt;

    // Commented out the constructor for row-mapper / generation of 'empty' user to return in the event
    // that a user is not found in the DB.
    // public User(int id,
    //             String username,
    //             String email,
    //             String hashedPassword,
    //             LocalDateTime createdAt,
    //             LocalDateTime updatedAt) {
    //     this.id = id;
    //     this.username = username;
    //     this.email = email;
    //     this.hashedPassword = hashedPassword;
    //     this.createdAt = LocalDateTime.now();
    //     this.updatedAt = LocalDateTime.now();
    // }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setUsername(String newUserName) {
        this.username = newUserName;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setHashedPassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    public void setCreatedAt(String newCreatedAt) {
        this.createdAt = newCreatedAt;
    }

    public void setUpdatedAt(String newUpdatedAt) {
        this.updatedAt = newUpdatedAt;
    }

}
