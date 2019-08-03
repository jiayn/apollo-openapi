package com.chen.lang.apollo.openapi.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenEnvClusterDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.chen.lang.apollo.openapi.util.ApolloPropertiesUtil;
import com.chen.lang.apollo.openapi.util.FileUtil;
import com.chen.lang.apollo.openapi.util.IPUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-22 17:37
 */
@Slf4j
public class OpenAPI {

    private ApolloOpenApiClient client;

    public OpenAPI(String portalUrl, String token) {
        this.client = getApolloOpenApiClient(portalUrl, token);
    }

    @Deprecated
    public OpenAPI() {
        this("http://106.12.25.204:8070/", "fcd182e92c989f5317d8f60d633139b3c761535d");
        // 911
        // Token: fcd182e92c989f5317d8f60d633139b3c761535d
        // http:/127.0.0.1:17989/
        // portal url
    }
    /**
     * 根据APP属性导出 应用的 配置信息
     *
     * @param openAppDTO
     * @throws Exception
     */
    public void exportConfigByAppDto(OpenAppDTO openAppDTO) throws Exception {
        String orgId = openAppDTO.getOrgId();
        exportConfigByAppId(openAppDTO.getAppId(), orgId);
    }
    /**
     * 根据appid 导出 当前项目下的配置
     * @param appid
     * @throws Exception
     */
    public void exportConfigByAppId(String appid, String orgId) throws Exception {
        List<OpenEnvClusterDTO> openEnvClusterDTOList = queryOpenEnvClusterDTOList(appid);

        String hostPath = IPUtil.getHostPath(client.getPortalUrl());
        //集群环境 列表
        for (OpenEnvClusterDTO openEnvClusterDTO : openEnvClusterDTOList) {
            String env = openEnvClusterDTO.getEnv();
            for (String clusterName : openEnvClusterDTO.getClusters()) {
                exportNamespacesByClusterName(hostPath, appid, clusterName, env, orgId);
            }
        }

    }

    /**
     * 获取APP集群信息
     *
     * @param appid
     * @return
     */
    public List<OpenEnvClusterDTO> queryOpenEnvClusterDTOList(String appid) {
        //获取App的环境，集群信息
        log.info("获取AppId={}的环境，集群信息", appid);
        List<OpenEnvClusterDTO> openEnvClusterDTOList = this.client
            .getEnvClusterInfo(appid);
        log.info("获取AppId={}的环境的集群信息\n {}", appid, JsonUtil.toPrettyFormat(openEnvClusterDTOList));
        return openEnvClusterDTOList;
    }

    /**
     * 获取所有Namespace信息接口
     * 并导出
     *
     * @param appid
     * @param clusterName
     * @param env
     */
    private void exportNamespacesByClusterName(String hostPath, String appid, String clusterName, String env,
        String orgId)
    throws Exception {
        List<OpenNamespaceDTO> openNamespaceDTOList = queryOpenNamespaceDTOList(appid, clusterName, env);
        if (openNamespaceDTOList == null) {
            log.info("namespace为空，跳过！appid={},clusterName={},env={}", appid, clusterName, env);
            return;
        }
        String configDirectoryPath = checkConfigDirectoryPath(hostPath, appid, env, orgId);

        for (OpenNamespaceDTO openNamespaceDTO : openNamespaceDTOList) {
            exportNameSpaceConfigFile(appid, clusterName, env, configDirectoryPath, openNamespaceDTO);
        }
    }

    /**
     * 查询 在env环境下 根据appid 查询集群clusterName下的所有namespace信息
     *
     * @param appid
     * @param clusterName
     * @param env
     * @return
     */
    public List<OpenNamespaceDTO> queryOpenNamespaceDTOList(String appid, String clusterName, String env) {
        List<OpenNamespaceDTO> openNamespaceDTOList = getOpenNamespaceDTOS(appid, clusterName, env);

        if (CollectionUtils.isEmpty(openNamespaceDTOList)) {
            log.info("没有查询到Namespace信息接口");
            return null;
        }
        return openNamespaceDTOList;
    }

    /**
     * 检查并构建 配置文件导出的路径
     *
     * @param appid
     * @param env
     * @param orgId
     * @return
     * @throws IOException
     */
    private String checkConfigDirectoryPath(String hostPath, String appid, String env, String orgId)
    throws IOException {
        //构建文件路径
        String configDirectoryPath = ApolloPropertiesUtil.getConfigDirectoryPath(hostPath, orgId, env, appid);
        FileUtil.createDirectories(configDirectoryPath);
        return configDirectoryPath;
    }

    /**
     * 导出 指定环境的 namespace 配置文件
     *
     * @param appid
     * @param clusterName
     * @param env
     * @param configDirectoryPath
     * @param openNamespaceDTO
     * @throws IOException
     */
    private void exportNameSpaceConfigFile(String appid, String clusterName, String env, String configDirectoryPath,
        OpenNamespaceDTO openNamespaceDTO) throws IOException {
        //构建文件名
        String congfigFileName = ApolloPropertiesUtil.getCongfigFileName(openNamespaceDTO.getAppId(),
            openNamespaceDTO.getClusterName(), openNamespaceDTO.getNamespaceName(), openNamespaceDTO.getFormat());

        //写入文件路径
        String targetFilePath = String.join(File.separator, configDirectoryPath, congfigFileName);

        //配置内容
        String namesapcePropertiesContext = openNamespaceDTO.getItems()
            .stream()
            .map(openItemDTO -> ApolloPropertiesUtil.getContentString(openItemDTO))
            .collect(Collectors.joining());
        log.info("appid={},获取集群{}下,env={},Namespace={}配置属性：\n{}", appid, clusterName, env,
            openNamespaceDTO.getNamespaceName(), namesapcePropertiesContext);

        Path path = Paths.get(targetFilePath);
        //写入文件
        Files.write(path, namesapcePropertiesContext.getBytes());
        log.info("配置文件成功导出导出路径为{}", targetFilePath);
    }

    /**
     * 获取环境下 appid 的所有 namespace
     *
     * @param appid
     * @param clusterName
     * @param env
     * @return
     */
    private List<OpenNamespaceDTO> getOpenNamespaceDTOS(String appid, String clusterName, String env) {
        List<OpenNamespaceDTO> openNamespaceDTOList = null;
        try {
            openNamespaceDTOList = new ArrayList<>();
            log.info("appid={},获取集群{}下,env={}所有Namespace信息接口", appid, clusterName, env);
            openNamespaceDTOList = client.getNamespaces(appid, env, clusterName);
            log.info("appid={},获取集群{}下,env={}所有Namespace信息接口\n {}", appid, clusterName, env,
                JsonUtil.toPrettyFormat(openNamespaceDTOList));
        } catch (Exception e) {
            log.error("获取所有namespace出错！ appid={},获取集群{}下,env={}所有Namespace信息接口", appid, clusterName, env);
            e.printStackTrace();
        }
        return openNamespaceDTOList;
    }

    /**
     * 获取环境下 appid 的指定的 namespace
     *
     * @param appid
     * @param clusterName
     * @param env
     * @return
     */
    public OpenNamespaceDTO getOpenNamespaceDTO(String appid, String clusterName, String env, String namespaceName) {
        log.info("appid={},获取集群{}下,env={},Namespace={}信息接口", appid, clusterName, env, namespaceName);
        OpenNamespaceDTO openNamespaceDTOList = client.getNamespace(appid, env, clusterName, namespaceName);
        log.info("appid={},获取集群{}下,env={},Namespace={}信息接口\n {}", appid, clusterName, env, namespaceName,
            JsonUtil.toPrettyFormat(openNamespaceDTOList));
        return openNamespaceDTOList;
    }

    private ApolloOpenApiClient getApolloOpenApiClient(String portalUrl, String token) {
        return ApolloOpenApiClient.newBuilder()
            .withPortalUrl(portalUrl)
            .withToken(token)
            .build();
    }

    public ApolloOpenApiClient getClient() {
        return this.client;
    }

    /**
     * 导入文件
     *
     * @param env 环境
     * @param userName 修改人
     * @param filePath 文件路径
     * @throws IOException
     */
    public void importConfigFile(String env, String userName, String filePath) throws IOException {
        OpenNamespaceDTO openNamespaceDTO = ApolloPropertiesUtil.generateOpenNamespaceDTOFromFile(filePath);

        OpenNamespaceDTO oldOpenNamespaceDTO = this.getOpenNamespaceDTO(openNamespaceDTO.getAppId(),
            openNamespaceDTO.getClusterName(), env, openNamespaceDTO.getNamespaceName());
        HashMap<String, OpenItemDTO> itemDTOHashMap = new HashMap<>();
        if (oldOpenNamespaceDTO != null && oldOpenNamespaceDTO.getItems() != null) {
            oldOpenNamespaceDTO.getItems()
                .stream()
                .forEach(openItemDTO -> {
                    itemDTOHashMap.put(openItemDTO.getKey(), openItemDTO);
                });
        }
        if (openNamespaceDTO != null) {
            log.info("{}文件解析为：\n{}", filePath, JsonUtil.toPrettyFormat(openNamespaceDTO));
            openNamespaceDTO.getItems()
                .stream()
                .forEach(itemDTO -> {
                    itemDTO.setDataChangeLastModifiedBy(userName);
                    itemDTO.setDataChangeCreatedBy(userName);
                    if (itemDTOHashMap.containsKey(itemDTO.getKey())) {
                        log.info("更新配置项 {}", JsonUtil.toPrettyFormat(itemDTO));
                        client.updateItem(openNamespaceDTO.getAppId(), env, openNamespaceDTO.getClusterName(),
                            openNamespaceDTO.getNamespaceName(), itemDTO);
                    } else {
                        log.info("新增配置项 {}", JsonUtil.toPrettyFormat(itemDTO));
                        client.createItem(openNamespaceDTO.getAppId(), env, openNamespaceDTO.getClusterName(),
                            openNamespaceDTO.getNamespaceName(), itemDTO);
                    }
                    ;
                });

        }
    }
}
