package com.chen.lang.apollo.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.lang.apollo.openapi.entity.dto.AppDTO;
import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenEnvClusterDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.chen.lang.apollo.openapi.client.HttpApiService;
import com.chen.lang.apollo.openapi.client.OpenAPI;
import com.chen.lang.apollo.openapi.config.ConfigProperties;
import com.chen.lang.apollo.openapi.constant.ConfigConsts;
import com.chen.lang.apollo.openapi.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 15:57
 */
@Service
@Slf4j
public class ApolloConfigFileServiceImpl implements ApolloConfigFileService {

    @Autowired
    private ConfigProperties apiConfigProperties;

    private volatile static HttpApiService httpApiService;
    private volatile static OpenAPI openAPI;

    @Override
    public HttpApiService getHttpApiServiceClient() {
        if (httpApiService == null) {
            synchronized (HttpApiService.class) {
                if (httpApiService == null) {
                    httpApiService = new HttpApiService(apiConfigProperties.getApi()
                        .getExportHttpApiProperties()
                        .getHostUrl(), apiConfigProperties.getApi()
                        .getExportHttpApiProperties()
                        .getUserName(), apiConfigProperties.getApi()
                        .getExportHttpApiProperties()
                        .getPassword());
                }
            }
        }
        return httpApiService;
    }
    @Override
    public OpenAPI getOpenAPIClient() {
        if (openAPI == null) {
            synchronized (OpenAPI.class) {
                if (openAPI == null) {
                    openAPI = new OpenAPI(apiConfigProperties.getApi()
                        .getExportOpenApiProperties()
                        .getPortalUrl(), apiConfigProperties.getApi()
                        .getExportOpenApiProperties()
                        .getToken());
                }
            }
        }
        return openAPI;
    }



    /**
     * 导出对应的appId的配置
     *
     * @param appIds
     */
    @Override
    public void export(String[] appIds) throws Exception {

        OpenAPI openAPI = getOpenAPIClient();

        HttpApiService httpApiService = getHttpApiServiceClient();

        log.info("导出{} 平台下的配置", apiConfigProperties.getApi()
            .getExportOpenApiProperties()
            .getPortalUrl());

        if (appIds != null) {
            log.info("导出appid 在{}中的数据", JsonUtil.toPrettyFormat(appIds));
            for (String id : appIds) {
                openAPI.exportConfigByAppId(id, ConfigConsts.DEFAULT);
            }
        }
        if (appIds == null) {
            log.info("导出所有配置");
            exportAllConfig(openAPI, httpApiService);
        }

    }

    /**
     * 导出所有
     *
     * @param openAPI
     * @param httpApiService
     * @throws Exception
     */
    private void exportAllConfig(OpenAPI openAPI, HttpApiService httpApiService) throws Exception {
        List<OpenAppDTO> openAppDTOList = httpApiService.getAppsInfo();
        log.info("共有{}个应用！", openAppDTOList.size());
        for (OpenAppDTO openAppDTO : openAppDTOList) {
            openAPI.exportConfigByAppDto(openAppDTO);
        }
        log.info(JsonUtil.toPrettyFormat(openAppDTOList));
    }

    private void safteExportConfigByAppDto(OpenAPI openAPI, OpenAppDTO openAppDTO) {
        try {
            log.info("开始导出 {}", JsonUtil.toPrettyFormat(openAppDTO));
            openAPI.exportConfigByAppDto(openAppDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导入对应环境的namespace的配置
     *
     * @param filePath
     * @param env
     */
    @Override
    public void importNameSpaceConfigFile(String filePath, String env)  throws Exception{

        getOpenAPIClient().importConfigFile(env, apiConfigProperties.getApi().getImportOpenApiProperties().getOperator(), filePath);
    }

    /**
     * 查询所有APP
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<OpenAppDTO> listApps() {
        return getHttpApiServiceClient().getAppsInfo();
    }

    /**
     * 查询APP使用的 环境列表
     *
     * @param appid
     * @return
     */
    @Override
    public List<OpenEnvClusterDTO> queryOpenEnvClusterDTOList(String appid) {
        return getOpenAPIClient().queryOpenEnvClusterDTOList(appid);
    }

    /**
     * 查询namesapce信息
     *
     * @param appid
     * @param clusterName
     * @param env
     * @return
     */
    @Override
    public List<OpenNamespaceDTO> queryOpenNamespaceDTOList(String appid, String clusterName, String env) {
        return getOpenAPIClient().queryOpenNamespaceDTOList(appid, clusterName, env);
    }

    /**
     * 新建app
     *
     * @param appId
     */
    @Override
    public void createApp(String appId) {
        AppDTO dto = new AppDTO();
        dto.setAppId(appId);
        dto.setName(appId);
        dto.setOrgId(apiConfigProperties.getOrgId());
        dto.setOrgName(apiConfigProperties.getOrgName());
        dto.setOwnerName(apiConfigProperties.getApi()
            .getImportHttpApiProperties()
            .getUserName());
        getHttpApiServiceClient().creatApp(dto);
    }

    /**
     * 新建namespace
     *
     * @param namespace
     * @param appId
     * @param comment
     */
    @Override
    public void createNamespaceByAppId(String namespace, String appId, String comment, boolean aPublic) {

        OpenAppNamespaceDTO appNamespaceDTO = new OpenAppNamespaceDTO();
        appNamespaceDTO.setName(namespace);
        appNamespaceDTO.setAppId(appId);
        appNamespaceDTO.setFormat("properties");
        if (aPublic) {
            appNamespaceDTO.setPublic(true);
        } else {
            appNamespaceDTO.setPublic(false);
        }
        appNamespaceDTO.setComment(comment);
        appNamespaceDTO.setDataChangeCreatedBy(apiConfigProperties.getApi()
            .getImportHttpApiProperties()
            .getUserName());
        getHttpApiServiceClient().createPrivateAppNamespace(appNamespaceDTO);
    }

    /**
     * 创建关联的nameSpace
     *
     * @param appId
     * @param publicNameSapceName
     */
    @Override
    public void createlAssociatedAppNamespace(String appId, String publicNameSapceName) {
        getHttpApiServiceClient().createlAssociatedAppNamespace(appId, publicNameSapceName);
    }

    /**
     * 开放平台授权
     *
     * @param appId
     */
    @Override
    public void assignAPPRole(String appId) {
        String token = apiConfigProperties.getApi()
            .getImportOpenApiProperties()
            .getToken();
        getHttpApiServiceClient().assignAPPRole(appId, token.trim());
    }
}
