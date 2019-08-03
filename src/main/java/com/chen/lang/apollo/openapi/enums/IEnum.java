package com.chen.lang.apollo.openapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举要实现的接口
 *
 * @author czq
 * @version 1.0
 * @since v1.0
 */
public interface IEnum {

    /**
     * 获取编码
     *
     * @return 编码
     */
    @JsonValue
    String getCode();
}
