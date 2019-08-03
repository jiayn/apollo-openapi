package com.chen.lang.apollo.openapi.client;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.entity.dto.AppDTO;
import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-07-31 14:52
 */
@Slf4j
public class AppTest extends SpringBootBaseTest {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Test
    public void createApp() {
        // {"appId":"dev1_1","name":"dev1_1","orgId":"dev1","orgName":"研发一部","ownerName":"apollo","admins":[]}

        String orgId = "dev1";
        String orgName = "研发一部";

        for (int i = 0; i < 100; i++) {
            String appId = String.format("dev1_1_appId%d", i);
            AppDTO dto = new AppDTO();
            dto.setAppId(appId);
            dto.setName(appId);
            dto.setOrgId(orgId);
            dto.setOrgName(orgName);
            dto.setOwnerName("apollo");
            apolloConfigFileService.getHttpApiServiceClient()
                .creatApp(dto);
        }
    }

}
