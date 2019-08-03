package com.chen.lang.apollo.openapi.log;

import java.util.Map;

/**
 * @author czq@chen.cn
 * @version $$Id: BusinessParameterPrinter.java, v 0.1 2018/12/22 11:22 AM czq@chen.cn Exp $$
 */
public interface BusinessParameterPrinter {

    /**
     * 打印要输出的内容
     *
     * @param parameters 要输出的内容
     */
    void print(Map<String, String> parameters);
}
