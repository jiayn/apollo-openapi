package com.chen.lang.apollo.openapi.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.service.CmdService;
import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-26 17:13
 */
public class CmdServiceImplTest extends SpringBootBaseTest {

    @Autowired
    private CmdService cmdService;

    @Test
    public void cmd() throws Exception {

        cmdService.cmd();

    }
}