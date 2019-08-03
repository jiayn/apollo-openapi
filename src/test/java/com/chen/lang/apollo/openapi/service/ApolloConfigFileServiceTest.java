package com.chen.lang.apollo.openapi.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 15:32
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApolloConfigFileServiceTest {


    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Test
    public void export() throws Exception {
        apolloConfigFileService.export(null);
    }
    @Test
    public void exportOne() throws Exception {
        apolloConfigFileService.export(new String[]{"company-xxxx-kylin-query",
            "company-xxxx-user",
            "company-xxxx-acctcenter",
            "company-xxxx-acctdayend",
            "company-xxxx-asset",
            "company-xxxx-convert",
            "company-xxxx-market",
            "company-xxxx-cc",
            "company-xxxx-credit",
            "company-xxxx-flow",
            "company-xxxx-platform",
            "company-xxxx-gateway",
            "company-xxxx-job-admin",
            "company-xxxx-api-web",
            "company-xxxx-public"});
    }

    @Test
    public void importNameSpaceConfigFile() throws Exception {

        //环境
        String env = "DEV";

        // String dirFile = "D:\\Workspaces\\opensource\\apollo-openapi\\apollo\\10.139.33.158_17989\\apollo";
        String dirFile = "apollo-openapi/apollo/Apollo/yr-job";
        //文件路径
        /*String filePath
            = "D:\\Workspaces\\opensource\\apollo-openapi\\apollo\\10.139.33.158_17989\\apollo\\company-acctcenter\\config-cache\\company-acctcenter+default+000.properties";*/

        List<String> list = Lists.newArrayList();
        getFiles(dirFile,list);
        for(String filePath : list){
            try {
                apolloConfigFileService.importNameSpaceConfigFile(filePath, env);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void getFiles(String path, List<String> list) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            if(files != null){
                for (File value : files) {
                    // 如果还是文件夹 递归获取里面的文件 文件夹
                    if (value.isDirectory()) {
                        // System.out.println("目录：" + files[i].getPath());
                        getFiles(value.getPath(), list);
                    } else {
                        if (value.getName()
                            .endsWith("properties")) {
                            System.out.println("文件：" + value.getPath());
                            list.add(value.getPath());
                        }
                    }

                }
            }

        } else {
            if(file.getName().endsWith("properties")) {
                System.out.println("文件：" + file.getPath());
                list.add(file.getPath());
            }
        }
    }

    @Test
    public void rename() throws Exception{
        Map<String,String> map = Maps.newHashMap();
        map.put("company", "company-xxxx");
        map.put("CREP", "xxxx");
        String dirFile = "apollo-openapi/apollo/Apollo";
        rename(dirFile,map);
    }

    public static void rename(String path, Map<String,String> map) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            if(files != null){
                for (File value : files) {
                    // 如果还是文件夹 递归获取里面的文件 文件夹
                    if (value.isDirectory()) {
                        // System.out.println("目录：" + files[i].getPath());
                        rename(value.getPath(), map);
                    } else {
                        if (value.getName()
                            .endsWith("properties")) {
                            System.out.println("文件：" + value.getPath());
                            rename(value,map);
                        }
                    }

                }
            }
        } else {
            if(file.getName().endsWith("properties")) {
                System.out.println("文件：" + file.getPath());
                rename(file,map);
            }
        }
    }

    public static void rename(File file,Map<String,String> map){
        String fileName = file.getName();
        for(Map.Entry<String,String> entry: map.entrySet()){
            fileName = fileName.replaceAll(entry.getKey(),entry.getValue());
        }
        if(file.renameTo(new File(file.getParent()+File.separator+fileName))){
            System.out.println("新文件名：" + fileName);
        }

    }
}