package com.chen.lang.apollo.openapi.start;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.chen.lang.apollo.openapi.service.ApolloConfigFileService;
import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.chen.lang.apollo.openapi.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 21:03
 */
@Component
@Order(value = Integer.MAX_VALUE)
@Slf4j
@ConditionalOnProperty(value = {"start.xxx.enable"}, matchIfMissing = false)
@Deprecated
public class StartApplicationRunner implements ApplicationRunner {

    @Autowired
    private ApolloConfigFileService apolloConfigFileService;

    /**
     * #部分导出
     * java -jar apollo-openapi-0.0.1-SNAPSHOT.jar   --operate=export --appId=company-credit,company-deploy,open-api-test003_id
     * #全部导出
     * java -jar apollo-openapi-0.0.1-SNAPSHOT.jar  --operate=export
     *
     * #导入
     * java -jar apollo-openapi-0.0.1-SNAPSHOT.jar  --operate=import  --env=DEV --filePath=apollo-openapi/target/apollo127.0.0.1:17989/default/DEV/opt/data/open-api-test003_id/config-cache/open-api-test003_id+default+CREP.openAPI2.properties
     */
    /**
     * 方法操作  取值 import export
     */
    private static final String OPERATE_METHOD_OPTION_NAME = "operate";
    private static final String APPID_OPTION_NAME = "appId";
    private static final String FILEPATH_OPTION_NAME = "filePath";
    private static final String ENV_OPTION_NAME = "env";

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        printArgs(args);

        switch (getOperateValue(args, OPERATE_METHOD_OPTION_NAME)) {
            case "import":
                executeImport(args);
                break;
            case "export":
                executeExport(args);
                break;
            default:
                log.info("未发现对应的操作符，operate取值为 import / export");
                break;
        }

        //执行方法
    }

    private void executeImport(ApplicationArguments args) throws Exception {
        log.info("执行导入操作");
        String filePath = getOperateValue(args, FILEPATH_OPTION_NAME);
        String env = getOperateValue(args, ENV_OPTION_NAME);
        apolloConfigFileService.importNameSpaceConfigFile(filePath, env);
    }

    private void executeExport(ApplicationArguments args) throws Exception {
        log.info("执行导出操作");
        String appId = getOperateValue(args, APPID_OPTION_NAME);
        String[] appIds = null;
        if (StringUtil.isNotBlank(appId)) {
            appIds = appId.split(",");
        } else {
            log.info("参数【{}】未填写", APPID_OPTION_NAME);
        }
        apolloConfigFileService.export(appIds);
    }

    private void printArgs(ApplicationArguments args) {
        // java -jar commandline-app-0.0.1-SNAPSHOT.jar iamnonoption --app.name=CmdRulez --app.hosts=abc,def,ghi --app.name=2
        System.out.println("# NonOptionArgs: " + args.getNonOptionArgs()
            .size());
        System.out.println("NonOptionArgs:");
        args.getNonOptionArgs()
            .forEach(System.out::println);
        System.out.println("# OptionArgs: " + args.getOptionNames()
            .size());
        System.out.println("OptionArgs:");
        args.getOptionNames()
            .forEach(optionName -> {
                System.out.println(optionName + "=" + args.getOptionValues(optionName));
            });
    }

    private String getOperateValue(ApplicationArguments args, String optionName) throws Exception {
        String optionValue = "";
        List<String> list = args.getOptionValues(optionName);
        if (CollectionUtils.isEmpty(list)) {
            throw new Exception("操作符" + optionName + "未定义！");
        } else if (list.size() > 1) {
            throw new Exception("操作符" + optionName + " 找到多个！" + JsonUtil.toPrettyFormat(list));
        } else {
            optionValue = list.get(0);
        }
        return optionValue;
    }
}
