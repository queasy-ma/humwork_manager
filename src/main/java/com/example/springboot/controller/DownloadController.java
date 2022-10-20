package com.example.springboot.controller;

import com.example.springboot.exception.BizException;
import com.example.springboot.tools.ZipUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@CrossOrigin
public class DownloadController {
    @Value("${file.upload.url}")
    private String uploadFilePath;

    @RequestMapping("/download")
    @ResponseBody
    public void fileDownLoad(HttpServletResponse response, @RequestParam("subject") String subject) throws IOException {
        File file = new File(uploadFilePath +'/'+ subject);
        File[] files = file.listFiles();
        assert files != null;
        int filenumber = filenumber(files); //获取文件夹中的文件数量
        String[] filePath = new String[filenumber];
        int count = 0;
        for (File filename : files) {
            filePath[count] = uploadFilePath+"/"+subject+"/"+filename.getName();//获取文件名
            System.out.println(filename.getName());
            count++;
        }
        String zipname = ZipUtils.packageFileToZip(filePath,subject,uploadFilePath);//打包
        File zipfile = new File(zipname);
        if(!zipfile.exists()){
            throw new BizException("400","下载文件不存在！");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) zipfile.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(subject, StandardCharsets.UTF_8) + ".zip" );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipfile));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
             System.out.println("下载失败");
        }
        System.out.println("下载成功");

    }

    public int filenumber(File[] list) {

        int fileCount = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                fileCount++;
            }
        }
        return fileCount;
    }
}
