package com.chen.lang.apollo.openapi.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.chen.lang.apollo.openapi.service.PortalService;
import com.google.common.collect.Lists;
import com.chen.lang.apollo.openapi.config.ConfigProperties;
import com.chen.lang.apollo.openapi.util.CollectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-07-31 17:10
 */
@Slf4j
@Service
public class PortalServiceImpl implements PortalService {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Autowired
    private ConfigProperties apiConfigProperties;

    /**
     * 1、初始化环境
     * 2、新建nameSpace ,共有的，私有的，关联的
     */
    @Override
    public void initApolloData() {
        List<String> appIds = apiConfigProperties.getAppIds();

        if (CollectionUtil.isNotEmpty(appIds)) {
            //公共nameSpace
            String publicNameSpace = String.format("%s.system", apiConfigProperties.getOrgId());

            for (String appId : appIds) {
                //新建APP
                apolloConfigFileService.createApp(appId);
                //新建nameSpace ,共有的，私有的
                if (appId.contains("public")) {
                    apolloConfigFileService.createNamespaceByAppId(publicNameSpace, appId, "项目公共配置", true);
                } else {
                    apolloConfigFileService.createNamespaceByAppId("000", appId, "000租户配置", false);
                }
            }
            //新建nameSpace ，关联的
            for (String appId : appIds) {
                //新建nameSpace ,共有的，私有的
                if (!appId.contains("public")) {
                    apolloConfigFileService.createlAssociatedAppNamespace(appId, publicNameSpace);
                }
                //开放平台授权
                apolloConfigFileService.assignAPPRole(appId);
            }
        }
        //数据导入
        this.importNameSpaceConfigFile();


    }

    /**
     * 导出对应的appId的配置
     * 若配置了 白名单 app,则导出指定的，若不存在则导出所有APP
     */
    @Override
    public void export() throws Exception {
        List<String> appIds = apiConfigProperties.getAppIds();
        String[] strs = null;
        if (CollectionUtil.isNotEmpty(appIds)) {
            strs = (String[]) appIds.toArray();
        }
        apolloConfigFileService.export(strs);
    }

    /**
     * 导入指定配置的 nameSpace
     */
    @Override
    public void importNameSpaceConfigFile() {

        //环境
        String env = apiConfigProperties.getImportEnv();

        String dirFile = apiConfigProperties.getApolloDataFilePath();
        List<String> list = Lists.newArrayList();
        getFiles(dirFile, list);
        for (String filePath : list) {
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
            if (files != null) {
                for (File value : files) {
                    // 如果还是文件夹 递归获取里面的文件 文件夹
                    if (value.isDirectory()) {
                        System.out.println("目录：" + value.getPath());
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
            if (file.getName()
                .endsWith("properties")) {
                System.out.println("文件：" + file.getPath());
                list.add(file.getPath());
            }
        }
    }

}
