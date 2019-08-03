package com.chen.lang.apollo.openapi.entity.properties;

import lombok.Data;

/**
 * apollo 官方open-api 配置
 *
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 14:18
 */

@Data
public class OpenApiProperties {

    /**
     * Apollo开放平台url
     */
    private String portalUrl;
    /**
     * 第三方应用授权
     */
    private String token;

    /**
     * 操作人 登录名
     */
    private String operator;

}
