package com.chen.lang.apollo.openapi.entity.properties;

import lombok.Data;

/**
 * apollo管理平台用户 模拟请求
 *
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 14:54
 */
@Data
public class HttpApiProperties {

    /**
     * 平台地址
     */
    private String hostUrl;
    /**
     * 登录名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

}
