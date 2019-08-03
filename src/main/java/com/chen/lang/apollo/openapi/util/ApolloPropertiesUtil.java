package com.chen.lang.apollo.openapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.enums.EnumApp;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-23 10:53
 */
@Slf4j
public class ApolloPropertiesUtil {

    /** 配置所在服务器地址 */
    private static final String HOST_PATH = "{hostPath}";

    /** 配置所属分组 */
    private static final String ORG_ID = "{orgId}";

    private static final String DATE = "{date}";

    /** env 所管理的配置环境 */
    private static final String ENV_PLACEHOLDER = "{env}";
    /** appId 	所管理的配置AppId */
    private static final String APP_ID_PLACEHOLDER = "{appId}";
    /** clusterName 	所管理的配置集群名， 一般情况下传入 default 即可。如果是特殊集群，传入相应集群的名称即可 */
    private static final String CLUSTER_NAME_PLACEHOLDER = "{clusterName}";
    /** namespaceName 	所管理的Namespace的名称 */
    private static final String NAMESPACE_NAME_PLACEHOLDER = "{namespaceName}";

    /** Namespace格式可能取值为：properties、xml、json、yml、yaml */
    private static final String FORMAT_PLACEHOLDER = "{format}";
    /** 导入导出的文件名 */
    public static final String CONGFIG_FILE_NAME = "{appId}+{clusterName}+{namespaceName}.{format}";

    /** 导出的Excel文件路径 */
    private static final String EXPORT_EXCEL_PATH = "apollo";
    /** 导出的Excel文件名 */
    private static final String EXPORT_EXCEL_NAME = "apollo_config_export_{env}_{date}.xlsx";
    /** 导出的文件路径 */
    private static final String CONFIG_DIRECTORY_PATH = "apollo/{hostPath}/{orgId}/{env}/opt/data/{appId}/config-cache";

    public static final String COMMENT_MARK = "#comment# ";

    public static final String POUND_SIGN = "#";

    public static final String EQUAL_SIGN = "=";

    /** key value 键值对 分隔符 */
    public static final String KV_SPLIT = " = ";
    /**
     * 导入导出的文件名
     *
     * @param appId 所管理的配置AppId
     * @param clusterName 所管理的配置集群名
     * @param namespaceName 所管理的Namespace的名称
     * @return 导入导出的文件名
     */
    public static String getCongfigFileName(String appId, String clusterName, String namespaceName, String format) {
        return CONGFIG_FILE_NAME.replace(APP_ID_PLACEHOLDER, appId)
            .replace(CLUSTER_NAME_PLACEHOLDER, clusterName)
            .replace(FORMAT_PLACEHOLDER, format)
            .replace(NAMESPACE_NAME_PLACEHOLDER, namespaceName);
    }

    /**
     * 导出的文件路径
     *
     * @param env 所管理的配置环境
     * @param appId 所管理的配置AppId
     * @return
     */
    public static String getConfigDirectoryPath(String hostpath, String orgId, String env, String appId) {
        return CONFIG_DIRECTORY_PATH.replace(ENV_PLACEHOLDER, env)
            .replace(APP_ID_PLACEHOLDER, appId)
            .replace(HOST_PATH, hostpath)
            .replace(ORG_ID, orgId)
            .replace(":", "_");
    }

    /**
     * 组装单条配置属性
     *
     * @param openItemDTO
     * @return
     */
    public static String getContentString(OpenItemDTO openItemDTO) {

        // http:/127.0.0.1:17989/apps/company-credit/envs/DEV/clusters/default/namespaces/application/items/export


        String line = "";
        if (StringUtils.isNotBlank(openItemDTO.getKey())) {
            line = String.join(KV_SPLIT, openItemDTO.getKey()
                .trim(), openItemDTO.getValue()) + FileUtil.getLineSeparator();
            if (StringUtils.isNotBlank(openItemDTO.getComment())) {
                line = COMMENT_MARK + openItemDTO.getComment()
                    .replaceAll("[\\n\\r]", " ; ") + FileUtil.getLineSeparator() + line;
            }
        } else {
            if (StringUtils.isNotBlank(openItemDTO.getComment())) {
                line = openItemDTO.getComment() + FileUtil.getLineSeparator();
            }
        }


        return line;
    }


    /**
     * 拼接应用枚举
     * @see  EnumApp
     * @param openAppDTO
     * @return
     */
    public static String getEnumApp(OpenAppDTO openAppDTO) {
        // company_USER("company-user", "LOAN 2.1用户模块"),
        String appId = openAppDTO.getAppId();
        String enumName = appId.replace(".", "_")
            .replace("-", "_")
            .toUpperCase();
        return String.format("%s(\"%s\",\"%s\"),", enumName, appId, openAppDTO.getName());
    }

    /**
     * 从文件中提取namespace信息
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static OpenNamespaceDTO generateOpenNamespaceDTOFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!FileUtil.isPathExists(path)) {
            log.info("当前环境不存在该文件，路径为={}", filePath);
            return null;
        }
        String fileName = path.getFileName()
            .toString();
        log.info("开始处理文件{}", fileName);
        //属性名绑定
        int formatIndex = fileName.lastIndexOf(".");
        String format = fileName.substring(formatIndex + 1)
            .trim();
        String[] property = fileName.substring(0, formatIndex)
            .trim()
            .split("\\+");
        if (property.length != 3) {
            log.error("{}文件名格式不符合{appId}+{clusterName}+{namespaceName}.{format}规范！", fileName);
            return null;
        }

        OpenNamespaceDTO openNamespaceDTO = new OpenNamespaceDTO();
        openNamespaceDTO.setAppId(property[0]);
        openNamespaceDTO.setClusterName(property[1]);
        openNamespaceDTO.setNamespaceName(property[2]);
        openNamespaceDTO.setFormat(format);

        //配置项
        List<OpenItemDTO> items = new ArrayList<>();
        openNamespaceDTO.setItems(items);

        List<String> propertiesLines = Files.readAllLines(path);
        for (int i = 0; i < propertiesLines.size(); i++) {

            String propertiesLine = propertiesLines.get(i);
            log.info(propertiesLine);

            OpenItemDTO openItemDTO = new OpenItemDTO();
            //普通注释 #号打头 并且没有标记未 ApolloPropertiesUtil.COMMENT_MARK  或者 空行
            if ((propertiesLine.startsWith(ApolloPropertiesUtil.POUND_SIGN) && !propertiesLine.startsWith(
                ApolloPropertiesUtil.COMMENT_MARK)) || "".equals(propertiesLine)) {
                log.info("不处理普通注释，{}",propertiesLine);
                continue;
                // openItemDTO.setKey("");
                // openItemDTO.setValue("");
                // openItemDTO.setComment(propertiesLine);
            }
            //格式设置 备注紧随着配置项
            if (propertiesLine.startsWith(ApolloPropertiesUtil.COMMENT_MARK)) {
                openItemDTO.setComment(propertiesLine.replace(ApolloPropertiesUtil.COMMENT_MARK, ""));
                propertiesLine = propertiesLines.get(++i);
                //处理单行配置
                dealPropertiesLine(openItemDTO, propertiesLine);
            }
            //处理单行配置
            dealPropertiesLine(openItemDTO, propertiesLine);

            items.add(openItemDTO);
        }
        return openNamespaceDTO;
    }

    /**
     * 处理单行配置项
     *
     * @param openItemDTO
     * @param propertiesLine
     */
    private static void dealPropertiesLine(OpenItemDTO openItemDTO, String propertiesLine) {
        //kv 配置项
        if (!propertiesLine.startsWith(ApolloPropertiesUtil.POUND_SIGN)) {

            int index = propertiesLine.indexOf(ApolloPropertiesUtil.EQUAL_SIGN);
            if (index < 0) {
                log.error("处理配置 = {}失败！", propertiesLine);
            }
            String key = propertiesLine.substring(0, index)
                .trim();
            String value = "";
            if (propertiesLine.length() > index + ApolloPropertiesUtil.EQUAL_SIGN.length()) {
                value = propertiesLine.substring(index + ApolloPropertiesUtil.EQUAL_SIGN.length())
                    .trim();
            }
            openItemDTO.setKey(key);
            openItemDTO.setValue(value);
        }
    }

    /**
     * 导出的Excel文件路径
     *
     * @return
     */
    public static String getExportExcelPath() {
        return EXPORT_EXCEL_PATH;
    }

    /**
     * 导出的Excel文件名
     *
     * @param env
     * @return
     */
    public static String getExportExcelName(String env) {
        return EXPORT_EXCEL_NAME.replace(ENV_PLACEHOLDER, env)
            .replace(DATE, DateUtil.getDate(new Date(), DateUtil.DATE_FORMAT_2));
    }

}
