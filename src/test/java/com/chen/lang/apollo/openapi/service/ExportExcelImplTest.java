package com.chen.lang.apollo.openapi.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-29 11:02
 */
@Slf4j
public class ExportExcelImplTest extends SpringBootBaseTest {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Test
    public void export() throws Exception {
        String appIds[] = new String[]{"company-acctcenter",
            "company-acctdayend",
            "company-app-web",
            "company-asset",
            "company-cc",
            "company-convert",
            "company-credit",
            "company-deploy",
            "company-flow",
            "company-gateway",
            "company-guard",
            "company-kylin-query",
            "company-market",
            "company-platform",
            "company-product",
            "company-project-platform",
            "company-public",
            "company-user",
            "yr-job"};
        apolloConfigFileService.export(appIds);
    }


    @Autowired
    private ExportExcel exportExcel;
    @Test
    public void generateConfigExcel() throws Exception {

        // String evns = "DEV ,UAT, FAT, PRO";
        String evns = "FAT";

        for (String evn : evns.split(",")) {
            exportExcel.generateConfigExcel(evn.trim());
        }
    }
}
