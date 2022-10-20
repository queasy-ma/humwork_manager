package com.example.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot.exception.BizException;
import com.example.springboot.sql.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

@Controller
@CrossOrigin
public class ArrangeController {
    @Value("${file.upload.url}")
    private String uploadFilePath;
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/arrange")
    @ResponseBody
    public JSONObject arrange(@RequestParam(value = "subject", required = false)String subject) throws SQLException, IOException {
        Integer i = jdbcTemplate.queryForObject("SELECT count(*) FROM `workdata` WHERE subject = ?", Integer.class, subject);//查看是否存在布置过的作业
        String dirname = uploadFilePath +"/"+ subject;
        Path path = Paths.get(dirname);

        if(i != 0)
        {
            throw new BizException("400","不能重复布置！");
        }
        else if(Files.exists(path))
        {
            throw new BizException("400","文件夹已经存在！");
        }
        else
        {
            jdbcTemplate.update("INSERT INTO workdata (subject)VALUES (?)",subject); //更新workdata表
            List<User> users = jdbcTemplate.query("SELECT * FROM user",new User());
            for (User data : users)
            {
                jdbcTemplate.update("INSERT INTO userwork (uid,subject)values (?,?)",data.getId(),subject);
            }
        }
        Files.createDirectory(path);
        System.out.println("已创建文件夹：" + dirname);
        JSONObject object = new JSONObject();
        object.put("success", 1);
        object.put("result","布置完成");
        return object;

    }

}
