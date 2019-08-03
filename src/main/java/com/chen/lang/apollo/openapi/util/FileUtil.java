package com.chen.lang.apollo.openapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-23 09:23
 */
@Slf4j
public class FileUtil {
    /**
     * 检查文件路径是否存在 不存在创建完整的路径
     *
     * @param targetFilePath 目标文件路径
     * @throws IOException io异常
     */
    public static void createDirectories(String targetFilePath) throws IOException {
        Path directoriesPath = Paths.get(targetFilePath);
        createDirectories(directoriesPath);
    }

    /**
     * 检查文件路径是否存在 不存在创建完整的路径
     *
     * @param directoriesPath 目标文件路径
     * @throws IOException io异常
     */
    public static void createDirectories(Path directoriesPath) throws IOException {
        boolean pathExists = isPathExists(directoriesPath);
        if (pathExists) {
            log.info("{}文件夹已经存在！", directoriesPath.toString());
        } else {
            Files.createDirectories(directoriesPath);
            log.info("{} directory created successfully!", directoriesPath.toString());
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param directoriesPath 文件路径
     * @return
     */
    public static boolean isPathExists(Path directoriesPath) {
        return Files.exists(directoriesPath, LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * 检查文件是否存在
     *
     * @param targetFilePath 目标文件路径
     * @return 存在标记
     */
    public static boolean exists(String targetFilePath) {
        Path directoriesPath = Paths.get(targetFilePath);
        return Files.exists(directoriesPath, LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param filePath 文件路径
     * @return
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            buffer = Files.readAllBytes(Paths.get(filePath));
        } catch (Exception e) {
            log.error("文件读取异常！", e);
        }
        return buffer;
    }

    /**
     * 获取系统换行符
     *
     * @return 系统换行符
     */
    public static String getLineSeparator() {
        return System.lineSeparator();
    }

}
