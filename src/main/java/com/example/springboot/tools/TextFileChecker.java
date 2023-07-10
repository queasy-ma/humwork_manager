package com.example.springboot.tools;

import com.example.springboot.exception.BizException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public class TextFileChecker {
    public static void checkFolder(File folder) throws IOException {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("非目录: " + folder);
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile()) {
                if (!isTextFile(file)) {
                    throw new BizException("500" ,"不是文本文件: " + file.getName());

                }
            } else if (file.isDirectory()) {
                checkFolder(file);
            }
        }
    }

    private static boolean isTextFile(File file) throws IOException {
        // 获取文件的编码格式
        String charsetName = getFileCharset(file);

        // 如果编码格式为空，则认为文件不是文本文件
        if (charsetName != null) {
            // 读取文件的内容并将其转换为字符串
            if (charsetName == "ANSI")
            {
                String content = FileUtils.readFileToString(file, Charset.forName("Windows-1252"));
                // 判断文件的内容是否为文本内容
                return isTextContent(content);
            }
            else
            {
                String content = FileUtils.readFileToString(file, charsetName);
                // 判断文件的内容是否为文本内容
                return isTextContent(content);
            }

        }
        else {
            return false;
        }


    }


    private static String getFileCharset(File file) {
        byte[] buffer = new byte[3];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (buffer[0] == (byte) 0xEF && buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
            return "UTF-8";
        } else if (buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE) {
            return "UTF-16LE";
        } else if (buffer[0] == (byte) 0xFE && buffer[1] == (byte) 0xFF) {
            return "UTF-16BE";
        } else if (buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE && buffer[2] == 0x00 && buffer[3] == 0x00) {
            return "UTF-32LE";
        } else if (buffer[0] == 0x00 && buffer[1] == 0x00 && buffer[2] == (byte) 0xFE && buffer[3] == (byte) 0xFF) {
            return "UTF-32BE";
        } else {
            return "ANSI";
        }
    }

//    private static String detectCharset(File file) throws IOException {
//        // 读取文件的前 1024 个字节
//        byte[] bytes = new byte[1024];
//        try (FileInputStream fis = new FileInputStream(file)) {
//            fis.read(bytes);
//        }
//
//        // 判断文件的编码格式
//        System.out.println(Charset.forName(Objects.requireNonNull(detectEncoding(bytes))).name());
//        return Charset.forName(Objects.requireNonNull(detectEncoding(bytes))).name();
//    }
//
//    private static String detectEncoding(byte[] bytes) {
//        String[] charsets = {"UTF-8", "Windows-1252", "ISO-8859-1"};
//        for (String charset : charsets) {
//            Charset cs = Charset.forName(charset);
//            String content = new String(bytes, cs);
//            if (isTextContent(content)) {
//                return charset;
//            }
//        }
//        return null;
//    }
//
    private static boolean isTextContent(String content) {
        // 判断文件的内容是否为文本内容
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 32 && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}