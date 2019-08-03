package com.chen.lang.apollo.openapi.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * APPID 枚举
 *
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-22 17:51
 */
@Getter
@AllArgsConstructor
public enum EnumApp implements IEnum {

    company_CONVERT("company-convert", "company-convert-server"),
    company_MARKET("company-market", "company-market-server"),
    company_USER("company-user", "apollo-user-server"),
    company_CC("company-cc", "apollo-cc-server"),
    company_DEPLOY("company-deploy", "apollo-deploy-server"),
    company_CREDIT("company-credit", "apollo-credit-server"),
    company_DATACENTER("company-datacenter", "apollo-datacenter-server"),
    company_FLOW("company-flow", "apollo-flow-server"),
    company_ICDM("company-icdm", "apollo-icdm-server"),
    company_JOB_EXECUTOR("company-job-executor", "apollo-job-executor-server"),
    company_PLATFORM("company-platform", "apollo-platform-server"),
    company_PRODUCT("company-product", "apollo-product-server"),
    company_RISKENGINE("company-riskengine", "apollo-riskengine-server"),
    company_CONSOLE("company-console", "apollo-console-server"),

    OPEN_API_TEST001("open-api-test001", "open-api-test"),
    ;

    /** 状态码 **/
    private String code;
    /** 状态描述 **/
    private String description;

    /**
     * 根据编码查找枚举
     *
     * @param code 编码
     * @return {@link EnumApp} 实例
     **/
    public static EnumApp find(String code) {
        return Arrays.stream(EnumApp.values())
            .filter(input -> input.getCode()
                .equals(code))
            .findFirst()
            .orElse(null);
    }}