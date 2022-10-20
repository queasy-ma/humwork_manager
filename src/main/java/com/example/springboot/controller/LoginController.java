package com.example.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot.sql.User;
import com.example.springboot.sql.WorkData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Controller
@CrossOrigin
public class LoginController {
    //数据源组件
    @Resource
    DataSource dataSource;
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;



    @RequestMapping("/login")
    @ResponseBody
    public JSONObject hello(@RequestParam(value = "uid",required = false) int uid, HttpSession session) throws SQLException {
        System.out.println("默认数据源为：" + dataSource.getClass());
        System.out.println("数据库连接实例：" + dataSource.getConnection());
        JSONObject object=new JSONObject();
        JSONArray array = new JSONArray();

        String sql = "SELECT * FROM user WHERE id =?";
        String sql2 = "SELECT * FROM workdata";
        List<User> result =  jdbcTemplate.query(sql,new User(),uid);
        for(User data :result)
        {
            object.put("user_id",data.getId());
            object.put("user_name",data.getUser_name());
        }

        List<WorkData> workdata =  jdbcTemplate.query(sql2,new WorkData());
        for(WorkData data :workdata)
        {
            array.add(data.getSubject());
        }
        object.put("subjects",array);

        session.setAttribute("uid", uid);
        return object;
    }
    @GetMapping("/login")
    public String login()
    {return "login.html";}
}