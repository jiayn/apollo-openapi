package com.chen.lang.apollo.openapi.entity.dto;

import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;

import lombok.Data;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-23 20:25
 */
@Data
public class ItemDTO {

    private String appId;
    private String env;
    private String clusterName;
    private String namespaceName;
    private OpenItemDTO itemDTO;
}
