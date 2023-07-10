package com.example.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot.exception.BizException;
import com.example.springboot.tools.TextFileChecker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@CrossOrigin
public class CheckController {

    @Value("${file.upload.url}")
    private String uploadFilePath;


    static private final double SIMILARITY = 0.8;

    @RequestMapping("/check")
    @ResponseBody
    public JSONObject fileDownLoad( @RequestParam("subject") String subject) {

        File folder = new File(uploadFilePath+'/'+subject);
        try {
            List<File> textFiles = getTextFiles(folder);
            if (textFiles.isEmpty())
            {
                JSONObject result = new JSONObject();
                result.put("code",500);
                result.put("msg","存在非文本无法查重");
                return result;
            }
            else
            {
                return checkSimilarity(textFiles);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("code",500);
        result.put("msg", "发生错误");
        return result;
    }

    public List<File> getTextFiles(File folder) throws IOException {
        File[] files = folder.listFiles();
        List<File> textFiles = new ArrayList<>();

        if (files == null || files.length == 0) {
            throw new IOException("No files found in the folder.");
        }
        try{
            TextFileChecker.checkFolder(folder);
            textFiles.addAll(Arrays.asList(files));
        }
        catch (BizException e){
            System.out.println(e.getErrorMsg());
        }

        return textFiles;
    }



    public Map<CharSequence, Integer> getTermFrequencies(String text) {
        Map<CharSequence, Integer> frequencies = new HashMap<>();

        // 使用正则表达式匹配单词，忽略大小写和非字母字符
        Pattern pattern = Pattern.compile("\\b\\w+\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        // 计算每个单词的词频
        while (matcher.find()) {
            CharSequence word = matcher.group().toLowerCase();
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }

        return frequencies;
    }

    public JSONObject checkSimilarity(List<File> textFiles) throws IOException {
        JSONObject result = new JSONObject();
        JSONArray files = new JSONArray();
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> vectorA;
        Map<CharSequence, Integer> vectorB;

        for (int i = 0; i < textFiles.size(); i++) {
            File fileA = textFiles.get(i);
            String contentA = FileUtils.readFileToString(fileA, StandardCharsets.UTF_8);
            vectorA = getTermFrequencies(contentA);

            for (int j = i + 1; j < textFiles.size(); j++) {
                File fileB = textFiles.get(j);
                String contentB = FileUtils.readFileToString(fileB, StandardCharsets.UTF_8);
                vectorB = getTermFrequencies(contentB);

                double similarity = cosineSimilarity.cosineSimilarity(vectorA, vectorB);
                System.out.println(similarity);
                if (similarity > SIMILARITY) {
                    DecimalFormat df = new DecimalFormat("0.00%");
                    String sim = df.format(similarity);
                    System.out.printf("找到相似度较高的文件: %s 和 %s (相似度: %s)%n", fileA.getName(), fileB.getName(), sim);
                    JSONObject simFiles = new JSONObject();
                    simFiles.put("fileA",fileA.getName());
                    simFiles.put("fileB", fileB.getName());
                    simFiles.put("similarity",  sim);
                    files.add(simFiles);
                }
            }
        }
        if (files.isEmpty()) {
            DecimalFormat df = new DecimalFormat("0.00%");
            String sim = df.format(SIMILARITY);
            result.put("code", 200);
            result.put("msg", "未找到相似度大于" + sim + "文件");
            return result;
        }else
        {
            result.put("code", 200);
            result.put("simFiles",files);
            return result;
        }
    }

}