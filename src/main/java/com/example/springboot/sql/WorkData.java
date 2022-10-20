package com.example.springboot.sql;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;


public class WorkData implements RowMapper<WorkData> {
    private int id;
    private String subject ;

    public WorkData(){}

    public void setId(int id){this.id = id;}

    public void setSubject(String subject){this.subject = subject;}

    public int getId(){return id;}

    public String getSubject(){return subject;}

    @Override
    public WorkData mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkData workdata = new WorkData();
        workdata.setId(rs.getInt("id"));
        workdata.setSubject(rs.getString("subject"));
        return workdata;
    }
}
