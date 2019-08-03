package com.chen.lang.apollo.openapi.util;

import java.util.ArrayList;
import java.util.List;

import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 集合范围
 *
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-30 09:20
 */
@Slf4j
public class ScopeUtil {

    public static List<OpenAppDTO> filterApps(List<OpenAppDTO> appList) {

        List<OpenAppDTO> newList = new ArrayList<>();
        if (CollectionUtil.isEmpty(appList)) {
            log.info("没有查询到app列表信息！");
        } else {
            List<String> appIds = ScopeUtil.export_2_1_Apps();
            for (OpenAppDTO openAppDTO : appList) {
                if (appIds.contains(openAppDTO.getAppId())) {
                    newList.add(openAppDTO);
                }
            }
        }

        return newList;
    }

    /**
     * 2.1范围的app
     *
     * @return
     */
    public static List<String> export_2_1_Apps() {

        List<String> appIds = new ArrayList<>();
        appIds.add("company-acctcenter");
        appIds.add("company-acctdayend");
        // appIds.add("company-apiweb");
        appIds.add("company-app-web");
        appIds.add("company-asset");
        appIds.add("company-cc");
        // appIds.add("company-console");
        appIds.add("company-convert");
        appIds.add("company-credit");
        // appIds.add("company-platform、guard、product、deploy");
        appIds.add("company-project-platform");
        // appIds.add("company-deploy");
        appIds.add("company-flow");
        appIds.add("company-gateway");
        // appIds.add("company-guard");
        // appIds.add("company-icdm");
        // appIds.add("company-job-admin");
        appIds.add("yr-job");
        // appIds.add("company-job-executor");
        // appIds.add("company-kylin");
        appIds.add("company-kylin-query");
        appIds.add("company-market");
        // appIds.add("company-platform");
        // appIds.add("company-policy");
        // appIds.add("company-product");
        // appIds.add("company-project-platform");
        appIds.add("company-public");
        // appIds.add("company-riskengine");
        // appIds.add("company-tamc");
        appIds.add("company-user");
        // appIds.add("company-web");
        return appIds;
    }

}
