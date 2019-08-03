package com.chen.lang.apollo.openapi.util;

import java.io.File;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-26 10:56
 */
@Slf4j
@ComponentScan
public class FileUtilTest {

    @Test
    public void test() {
        log.info(File.pathSeparator);
        log.info(File.separator);
        log.info(File.separatorChar + "");
    }

}