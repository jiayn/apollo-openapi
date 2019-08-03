package com.chen.lang.apollo.openapi.service;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-06-10 20:33
 */
public interface ExportExcel {

    /**
     * 导出配置
     *
     * @throws Exception
     */
    void generateConfigExcel(String env) throws Exception;
}
