package com.chen.lang.apollo.openapi.entity.properties;

import lombok.Data;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 15:06
 */
@Data
public class ApiConfigProperties {

    /**
     * 待导入数据的 apollo管理平台用户
     */
    private HttpApiProperties importHttpApiProperties;

    /**
     * 待导出数据的 apollo管理平台用户
     */
    private HttpApiProperties exportHttpApiProperties;

    /**
     * 待导出数据的 官方open-api 配置
     */
    private OpenApiProperties importOpenApiProperties;

    /**
     * 待导出数据的 官方open-api 配置
     */
    private OpenApiProperties exportOpenApiProperties;
}
