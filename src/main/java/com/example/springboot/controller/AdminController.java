package com.example.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot.exception.BizException;
import com.example.springboot.sql.Admin;
import com.example.springboot.sql.User;
import com.example.springboot.sql.UserWork;
import com.example.springboot.sql.WorkData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
public class AdminController {
    //数据源组件
    @Resource
    DataSource dataSource;
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/admin")
    @ResponseBody
    public JSONObject admin(@RequestParam(value = "username",required = false)String username, @RequestParam(value = "password",required = false)String password, HttpSession session) throws SQLException {
        System.out.println("默认数据源为：" + dataSource.getClass());
        System.out.println("数据库连接实例：" + dataSource.getConnection());
        String adminsql = "SELECT * FROM admin WHERE id = ?";
        Admin admin = jdbcTemplate.queryForObject(adminsql,new Admin(),1);
        assert admin != null;
        if (!Objects.equals(admin.getUsername(), username) || !Objects.equals(admin.getPassword(), password))
        {
           throw new BizException("403","用户名或密码不正确！");
        }
        else {
            session.setAttribute("admin", username);
            JSONObject UserData = new JSONObject();//要返回的信息
            UserData.put("code", 200);

            JSONArray usersdata = new JSONArray();//所有用户的所有信息
            JSONArray subjects = new JSONArray();//所有用户的学科

            String worksql = "SELECT * FROM workdata";//学科
            String usersql = "SELECT * FROM user";//用户
            //获取数据
            List<WorkData> workDatas = jdbcTemplate.query(worksql, new WorkData());
            List<User> users = jdbcTemplate.query(usersql, new User());

            for (WorkData data : workDatas) {
                subjects.add(data.getSubject());
            }
            UserData.put("subjects", subjects);//将所有科目打包给返回的数据

            for (User data : users) //获取每个用户的信息、整合
            {
                JSONObject useritem = new JSONObject();//某个用户的所有信息
                String userwork_item_sql = "SELECT * FROM userwork where uid = ?";
                useritem.put("uid", data.getId());
                useritem.put("user_name", data.getUser_name());
                List<UserWork> userWork_Item = jdbcTemplate.query(userwork_item_sql, new UserWork(), data.getId());
                for (UserWork workItem : userWork_Item) {
                    useritem.put(workItem.getSubject(), workItem.getWork());
                }
                usersdata.add(useritem);
            }
            UserData.put("users", usersdata);
            return UserData;
        }

    }

}
