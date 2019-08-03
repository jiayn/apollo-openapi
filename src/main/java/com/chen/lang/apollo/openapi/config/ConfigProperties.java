package com.chen.lang.apollo.openapi.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.chen.lang.apollo.openapi.entity.properties.ApiConfigProperties;

import lombok.Data;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 15:06
 */
@Data
@Component
@ConfigurationProperties("app.apollo")
public class ConfigProperties {

    /**
     * apollo api 接口配置项
     */
    private ApiConfigProperties api;

    private List<String> appIds;

    /**
     * 新建APP所属机构id
     */
    private String orgId;

    /**
     * 新建APP所属机构 name
     */
    private String orgName;

    /**
     * apollo数据本地路径
     */
    private String apolloDataFilePath;

    /**
     * apollo数据导入的环境
     */
    private String importEnv;

}
