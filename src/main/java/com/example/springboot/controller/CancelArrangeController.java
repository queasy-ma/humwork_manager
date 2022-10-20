package com.example.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot.exception.BizException;
import com.example.springboot.sql.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
@CrossOrigin
public class CancelArrangeController {
    @Value("${file.upload.url}")
    private String uploadFilePath;
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/cancel_arrange")//删除文件夹以及删除相关数据表字段
    @ResponseBody
    public JSONObject cancel_arrange(@RequestParam(value = "subject",required = false)String subject) throws SQLException, IOException {
        String dirname = uploadFilePath +"/"+ subject;
        File file = new File(dirname);
        if(FileSystemUtils.deleteRecursively(file)) {
            jdbcTemplate.update("DELETE FROM workdata WHERE subject = ?",subject);
            List<User> user = jdbcTemplate.query("SELECT * FROM user", new User());
            for( User data : user) {
                jdbcTemplate.update("DELETE FROM userwork WHERE uid = ? AND subject = ?", data.getId(), subject);
            }
            jdbcTemplate.execute("alter table userwork auto_increment=1;");
            jdbcTemplate.execute("alter table workdata auto_increment=1;");//将两个经常变化的数据表设置自增从1开始
            JSONObject object = new JSONObject();
            object.put("success", 1);
            object.put("result","撤销布置完成");
            return object;
        }
        else {
            throw new BizException("400","撤销布置失败！");
        }
    }
}
