package com.example.springboot.sql;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements RowMapper<User> {
    private int id;
    private String user_name ;

    public User(int id, String user_name, String pass_word) {
        this.id = id;
        this.user_name = user_name;
    }

    public User() {
    }

    public User(String user_name, String pass_word) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUser_name(resultSet.getString("user_name"));
        return user;
    }
}
