package com.chen.lang.apollo.openapi.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-07-31 19:52
 */
public class PortalServiceTest extends SpringBootBaseTest {

    @Autowired
    private PortalService portalService;

    @Test
    public void initApolloData() {
        portalService.initApolloData();
    }

    @Test
    public void export() throws Exception {
        portalService.export();
    }

    @Test
    public void importNameSpaceConfigFile() {
        portalService.importNameSpaceConfigFile();

    }
}