package com.chen.lang.apollo.openapi.service;

import java.util.List;

import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenEnvClusterDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.chen.lang.apollo.openapi.client.HttpApiService;
import com.chen.lang.apollo.openapi.client.OpenAPI;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-24 13:23
 */
public interface ApolloConfigFileService {

    /**
     * 导出对应的appId的配置
     *
     * @param appIds
     */
    void export(String[] appIds) throws Exception;

    /**
     * 导入对应环境的namespace的配置
     *
     * @param filePath 文件路径
     * @param env 环境
     */
    void importNameSpaceConfigFile(String filePath, String env) throws Exception;

    /**
     * 查询所有APP
     *
     * @return
     * @throws Exception
     */
    List<OpenAppDTO> listApps();

    /**
     * 查询APP使用的 环境列表
     *
     * @param appid
     * @return
     */
    List<OpenEnvClusterDTO> queryOpenEnvClusterDTOList(String appid);

    /**
     * 查询namesapce信息
     *
     * @param appid
     * @param clusterName
     * @param env
     * @return
     */
    List<OpenNamespaceDTO> queryOpenNamespaceDTOList(String appid, String clusterName, String env);

    HttpApiService getHttpApiServiceClient();

    OpenAPI getOpenAPIClient();

    /**
     * 新建app
     *
     * @param appId
     */
    void createApp(String appId);

    /**
     * 新建namespace
     *
     * @param namespace
     * @param appId
     * @param comment
     */
    void createNamespaceByAppId(String namespace, String appId, String comment, boolean aPublic);

    /**
     * 创建关联的nameSpace
     */
    void createlAssociatedAppNamespace(String appId, String publicNameSapceName);

    /**
     * 开放平台授权
     *
     * @param appId
     */
    void assignAPPRole(String appId);
}