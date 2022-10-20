package com.example.springboot.sql;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWork implements RowMapper<UserWork> {
    private int uid;
    private int work;
    private String subject;

    public UserWork(){}
    public void setUid(int id){this.uid = id;}
    public int getUid(){return uid;}
    public void setWork(int work){this.work = work;}
    public int getWork(){return work;}
    public void setSubject(String subject){this.subject = subject;}
    public String getSubject(){return subject;}

    @Override
    public UserWork mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserWork userwork = new UserWork();
        userwork.setUid(rs.getInt("uid"));
        userwork.setWork(rs.getInt("work"));
        userwork.setSubject(rs.getString("subject"));
        return userwork;
    }
}
