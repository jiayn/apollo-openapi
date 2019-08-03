package com.chen.lang.apollo.openapi.util;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 16:56
 */
@Slf4j
public class IPUtilTest {

    @Test
    public void getHostPath() {

        String str = "http:/127.0.0.1/sadfasf";
        log.info(IPUtil.getHostPath(str));

        print();

    }

    private void print(String... str) {
        if (str != null) {
            log.info(str.toString());
            for (String s : str) {
                log.info(s);
            }
        }
    }

    @Test
    public void name() {
        String str = "# 日志地址\n" + "logging.path = ./logs\n" + "# 应用名称，要求和@FeignClient中的name保持一致\n"
            + "spring.application.name = company-demo\n" + "# port\n" + "server.port = 8080";
        log.info(str.replaceAll("[\\n\\r]", " ; "));

    }
}