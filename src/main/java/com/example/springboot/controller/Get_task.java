package com.example.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.springboot.sql.WorkData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
@CrossOrigin
public class Get_task {
    @Value("${file.upload.url}")
    private String uploadFilePath;
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/get_tasks")//删除文件夹以及删除相关数据表字段
    @ResponseBody
    public JSONArray getTsak() throws SQLException, IOException {
        JSONArray subjects = new JSONArray();//所有用户的学科
        String sql = " select * from workdata";
        List<WorkData> workDatas = jdbcTemplate.query(sql, new WorkData());
        for (WorkData data : workDatas) {
            subjects.add(data.getSubject());
        }
        return subjects;
    }
}