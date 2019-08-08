package com.chen.lang.apollo.openapi.client;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.chen.lang.apollo.openapi.base.SpringBootBaseTest;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-07-31 15:16
 */
public class NameSpaceTest extends SpringBootBaseTest {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    @Test
    public void creatNameSpace() {
        for (int i = 0; i < 100; i++) {
            String appId = String.format("dev1_1_appId%d", i);
            createNamespaceByAppId("000", appId, "000租户配置");
        }
    }

    @Test
    public void createlAssociatedAppNamespace() {
        for (int i = 0; i < 100; i++) {
            String appId = String.format("dev1_1_appId%d", i);
            apolloConfigFileService.getHttpApiServiceClient().createlAssociatedAppNamespace(appId,"dev1.system4");
        }
    }


    @Test
    public void creatPublicNameSpace() {
        String appId = String.format("dev1_1_appId%d", 18);
        createPublicNamespaceByAppId("system7", appId, "公共配置7");
    }

    private void createPublicNamespaceByAppId(String namespace, String appId, String comment) {

        //
        // name 	true 	String 	Namespace的名字
        // appId 	true 	String 	Namespace所属的AppId
        // format 	true 	String 	Namespace的格式，只能是以下类型： properties、xml、json、yml、yaml
        // isPublic 	true 	boolean 	是否是公共文件
        // comment 	false 	String 	Namespace说明
        // dataChangeCreatedBy 	true 	String 	namespace的创建人，格式为域账号，也就是sso系统的User ID
        // {"appId":"company-xxxx-acctcenter","name":"000","comment":"000租户配置","isPublic":false,"format":"properties"}
        OpenAppNamespaceDTO appNamespaceDTO = new OpenAppNamespaceDTO();
        appNamespaceDTO.setName(namespace);
        appNamespaceDTO.setAppId(appId);
        appNamespaceDTO.setFormat("properties");
        appNamespaceDTO.setPublic(true);
        appNamespaceDTO.setAppendNamespacePrefix(true);
        appNamespaceDTO.setComment(comment);
        appNamespaceDTO.setDataChangeCreatedBy("apollo");
        apolloConfigFileService.getHttpApiServiceClient()
            .createPrivateAppNamespace(appNamespaceDTO);
    }

    private void createNamespaceByAppId(String namespace, String appId, String comment) {

        //
        // name 	true 	String 	Namespace的名字
        // appId 	true 	String 	Namespace所属的AppId
        // format 	true 	String 	Namespace的格式，只能是以下类型： properties、xml、json、yml、yaml
        // isPublic 	true 	boolean 	是否是公共文件
        // comment 	false 	String 	Namespace说明
        // dataChangeCreatedBy 	true 	String 	namespace的创建人，格式为域账号，也就是sso系统的User ID
        // {"appId":"company-xxxx-acctcenter","name":"000","comment":"000租户配置","isPublic":false,"format":"properties"}
        OpenAppNamespaceDTO appNamespaceDTO = new OpenAppNamespaceDTO();
        appNamespaceDTO.setName(namespace);
        appNamespaceDTO.setAppId(appId);
        appNamespaceDTO.setFormat("properties");
        appNamespaceDTO.setPublic(false);
        appNamespaceDTO.setComment(comment);
        appNamespaceDTO.setDataChangeCreatedBy("apollo");
        apolloConfigFileService.getHttpApiServiceClient()
            .createPrivateAppNamespace(appNamespaceDTO);
    }

}
