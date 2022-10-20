package com.example.springboot.sql;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

public class ResultData {
    @Resource
    JdbcTemplate jdbcTemplate;



    //获取单条数据
    public User getUserById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?",
                new BeanPropertyRowMapper<>(User.class), id);
    }

    //获取多条数据
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users",
                new BeanPropertyRowMapper<>(User.class));
    }
}
