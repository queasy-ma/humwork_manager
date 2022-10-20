package com.example.springboot.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {


    public static String packageFileToZip(String[] filePath, String subject,String uploadFilePath) throws IOException {
        List<File> fileList = new ArrayList<>();
        for (String s : filePath) {
            File file = new File(s);                // 将需要打包的文件都放在一个集合中
            fileList.add(file);
        }
        // 先在上传科目根目录创建一个压缩包
        String zipname = uploadFilePath+"/"+subject+"/"+subject+".zip";
        File zipFile = new File(zipname);
        // 将package.zip的File对象传到toZip对象中
        toZip(fileList, zipFile);
        return zipname;
    }



    public static void toZip(List<File> srcFiles, File zipFile) throws IOException {
        if(zipFile == null){
            return;
        }
        if(!zipFile.getName().endsWith(".zip")){
            return;
        }
        ZipOutputStream zos = null;
        FileOutputStream out = new FileOutputStream(zipFile);
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[1024];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                // 读取文件并写入到zip中
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                    zos.flush();
                }
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (zos != null) {
                zos.close();
            }
        }
    }
}

