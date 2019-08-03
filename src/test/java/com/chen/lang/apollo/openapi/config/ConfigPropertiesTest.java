package com.chen.lang.apollo.openapi.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 15:40
 */
@Slf4j
public class ConfigPropertiesTest extends SpringBootBaseTest {

    @Autowired
    private ConfigProperties apiConfigProperties;

    @Test
    public void getApi() {
        log.info(JsonUtil.toPrettyFormat(apiConfigProperties.getApi()));
        log.info(JsonUtil.toPrettyFormat(apiConfigProperties.getAppIds()));
    }

}