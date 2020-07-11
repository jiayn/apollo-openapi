package com.chen.lang.apollo.openapi.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.chen.lang.apollo.openapi.service.CmdService;
import com.chen.lang.apollo.openapi.service.PortalService;
import com.chen.lang.apollo.openapi.config.ConfigProperties;
import com.chen.lang.apollo.openapi.entity.bo.InputResultBean;
import com.chen.lang.apollo.openapi.util.ApolloPropertiesUtil;
import com.chen.lang.apollo.openapi.util.CMDUtil;
import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.chen.lang.apollo.openapi.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-26 17:05
 */
@Slf4j
@Service
public class CmdServiceImpl implements CmdService {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Autowired
    private ConfigProperties apiConfigProperties;

    @Autowired
    private PortalService portalService;


    @Override
    public void cmd() throws Exception {
        //复制 配置文件
        copyPropertiesToExternal();
        //打印主体信息
        CMDUtil.printPicture();
        Scanner sc = new Scanner(System.in);
        //检查配置后 执行结果
        checkConfig(sc);

    }

    private void checkConfig(Scanner sc) throws Exception {
        executeBusiness(sc);
    }

    private void executeBusiness(Scanner sc) throws Exception {
        while (true) {
            String title = "1.导出全部配置\n" + "2.根据APPID,导出指定应用配置\n" + "3.导入namespace配置文件\n" + "其他操作退出程序";
            InputResultBean inputResultBean = CMDUtil.inputValue(title, sc);
            if (inputResultBean.isExit()) {
                return;
            }
            String modSel = inputResultBean.getInputStr();
            switch (modSel) {
                case "1":
                    System.out.println("导出全部配置。。。。");
                    apolloConfigFileService.export(null);
                    break;
                case "2":
                    exportConfig(sc);
                    break;
                case "3":
                    importConfig(sc);
                    break;
                default:
                    System.out.println("退出程序。。。。");
                    return;
            }

        }
    }

    private void initApollo() {
        portalService.initApolloData();
    }

    private void importConfig(Scanner sc) throws Exception {
        while (true) {
            System.out.println("导入namespace配置文件。。。。");
            String promptMessage = "请输入要导入的文件路径（程序会根据文件的格式导入对应的namespace,文件格式为" + ApolloPropertiesUtil.CONGFIG_FILE_NAME
                + " 请导入前校验）：";
            InputResultBean filePathInputResultBean = CMDUtil.inputValue(promptMessage, sc);
            if (filePathInputResultBean.isExit()) {
                return;
            }
            String filePath = filePathInputResultBean.getInputStr();

            String envPromptMessage = "请输入一个要导入的环境，如 DEV ,UAT,  LOCAL, FWS, FAT, LPT, PRO, TOOLS, UNKNOWN";

            InputResultBean envInputResultBean = CMDUtil.inputValue(envPromptMessage, sc);
            if (envInputResultBean.isExit()) {
                return;
            }
            String env = envInputResultBean.getInputStr();

            String resultInput = String.format("输入的参数 \n" + "env=%s \n filePath=%s \n" + "确认继续，输入【 y 】 ;重新输入，输入 【 m 】 ",
                env, filePath);
            InputResultBean resultInputBean = CMDUtil.inputValue(resultInput, sc);
            if (resultInputBean.isExit()) {
                return;
            }
            switch (resultInputBean.getInputStr()) {
                case "y":
                    List<String> list = Lists.newArrayList();
                    getFiles(filePath, list);

                    for (String filep: list) {
                        apolloConfigFileService.importNameSpaceConfigFile(filep.trim(), env.trim());
                    }

                    break;
                default:
                    System.out.println("请重新输入");
                    continue;
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

    private void exportConfig(Scanner sc) throws Exception {
        while (true) {
            System.out.println("根据APPID,导出指定应用配置。。。。");
            InputResultBean inputResultBean = CMDUtil.inputValue("请输入appId，多个数据用逗号隔开", sc);
            if (inputResultBean.isExit()) {
                return;
            }
            if (StringUtil.isBlank(inputResultBean.getInputStr())) {
                System.out.println("输入字符无效，请重新输入。。。。");
                continue;
            }
            String[] appIds = inputResultBean.getInputStr()
                .trim()
                .split(",");
            apolloConfigFileService.export(appIds);
        }
    }

    /**
     * 配置文件复制到外面
     *
     * @throws IOException
     */
    private void copyPropertiesToExternal() throws IOException {

        String classPropertiesFilePath = "application.yml";
        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(classPropertiesFilePath);
        // File cfgFile = ResourceUtils.getFile(classPropertiesFilePath);
        if (inputStream != null) {
            log.info("系统内配置文件存在");
        } else {
            log.error("系统内配置文件没有找到");
        }
        // Path path = Paths.get(cfgFile.toURI());
        // if (FileUtil.isPathExists(path)) {
        //     List<String> lines = Files.readAllLines(path);
        //     StringBuilder stringBuilder = new StringBuilder();
        //     lines.stream()
        //         .forEach(line -> stringBuilder.append(line)
        //             .append(System.lineSeparator()));
        //     System.out.println("系统内配置文件n配置如下");
        //     System.out.println(stringBuilder.toString());
        // } else {
        //     System.out.println("未找到系统内配置文件");
        // }

        String externalPropertiesFilePath = "application.yml";
        File externalPropertiesFile = new File(externalPropertiesFilePath);
        if (!externalPropertiesFile.exists()) {
            log.info("生成文件，并复制系统默认配置到外部！");
            Files.copy(inputStream, externalPropertiesFile.toPath());
        } else {
            log.info("外部配置文件存在");
        }
    }

}
