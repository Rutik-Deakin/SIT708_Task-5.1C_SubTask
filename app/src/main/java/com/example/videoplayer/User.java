package com.example.videoplayer;

public class User {
    private long userId;
    private String username;
    private String fullname;
    private String password;

    public User(long userId, String username, String fullname, String password) {
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }
}
