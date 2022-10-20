package com.example.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Controller
@CrossOrigin
public class UploadController {
    //用于访问数据库的组件
    @Resource
    JdbcTemplate jdbcTemplate;

    @Value("${file.upload.url}")
    private String uploadFilePath;

    @RequestMapping("/upload")
    @ResponseBody
    public Object create(@RequestPart MultipartFile file, @RequestParam(value = "uid")String uid, @RequestParam(value = "name")String name, @RequestParam(value = "subject")String subject) throws IOException {
        Integer i = jdbcTemplate.queryForObject("SELECT count(*) FROM `workdata` WHERE subject = ?", Integer.class, subject);
        JSONObject object=new JSONObject();
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String filePath = uploadFilePath +"/"+ subject +"/"+ uid + name + subject + suffixName;
        System.out.println(filePath);
        File dest = new File(filePath);
        try {
            file.transferTo(dest); //上传重复会覆盖源文件
        } catch (Exception e) {
            object.put("success",0);
            object.put("result","程序错误，请重新上传");
            return object;
        }
        object.put("success",1);
        object.put("result","文件上传成功");
        jdbcTemplate.update("UPDATE userwork SET work = 1 WHERE uid = ? AND subject = ?",uid,subject);
        return object;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload.html";
    }

}
