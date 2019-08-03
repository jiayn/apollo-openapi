package com.chen.lang.apollo.openapi.service;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-07-31 17:07
 */
public interface PortalService {

    /**
     * 1、初始化环境
     * 2、新建nameSpace ,共有的，私有的，关联的
     */
    void initApolloData();

    /**
     * 导出对应的appId的配置
     * 若配置了 白名单 app,则导出指定的，若不存在则导出所有APP
     */
    void export() throws Exception;

    /**
     * 开放平台授权后 才可以导入数据
     * 导入指定配置的 nameSpace
     */
    void importNameSpaceConfigFile();
}
