package com.example.springboot.sql;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Parking implements RowMapper<Parking> {

    private int id;

    private String number;

    private String in_time;

    private String out_time;

    private int in_parking;

    public Parking(){}

    public void setId(int id){this.id = id;}
    public int getId(){return id;}

    public void setNumber(String number){this.number = number;}
    public String getNumber(){return number;}

    public void setIn_time(String in_time){this.in_time = in_time;}
    public String getIn_time(){return in_time;}

    public void setOut_time(String out_time){this.out_time = out_time;}
    public String getOut_time(){return out_time;}

    public void setIn_parking(int in_parking){this.in_parking = in_parking;}
    public int getIn_parking(){return in_parking;}


    @Override
    public Parking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Parking parking = new Parking();
        parking.setId(rs.getInt("id"));
        parking.setNumber(rs.getString("number"));
        parking.setIn_time(String.valueOf(rs.getTime("in_time")));
        parking.setOut_time(String.valueOf(rs.getTime("out_time")));
        parking.setIn_parking(Integer.parseInt(rs.getString("in_parking")));
        return parking;
    }
}
