package com.chen.lang.apollo.openapi.client;

import java.util.List;

import org.junit.Test;

import com.chen.lang.apollo.openapi.entity.dto.AppDTO;
import com.chen.lang.apollo.openapi.entity.dto.ItemDTO;
import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.enums.EnumApp;
import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.chen.lang.apollo.openapi.util.ScopeUtil;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.chen.lang.apollo.openapi.util.ApolloPropertiesUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @see OpenAPI
 * @since 2.1.0 2019-04-22 18:02
 */
@Slf4j
public class OpenAPITest {

    private OpenAPI openAPI = new OpenAPI();

    private ApolloOpenApiClient apolloOpenApiClient = openAPI.getClient();


    private HttpApiService httpApiService = new HttpApiService();


    /**
     * 导出所有配置
     *
     * @throws Exception
     */
    @Test
    public void testExportAll() throws Exception {
        for (EnumApp enumApp : EnumApp.values()) {
            openAPI.exportConfigByAppId(enumApp.getCode(), "");
        }
    }

    /**
     * 导出单个
     * @throws Exception
     */
    @Test
    public void testExportOne() throws Exception {
        String appid = EnumApp.company_CREDIT.getCode();
        openAPI.exportConfigByAppId(appid, "");
    }

    /**
     * 3.2.4 创建Namespace
     */
    @Test
    public void testCreatenameSpace() {

        // createNamespaceByAppId();

    }

    /**
     * 创建namespace
     * @param namespace
     * @param appId
     * @param comment
     */
    private void createNamespaceByAppId(String namespace,String appId,String comment) {

        //
        // name 	true 	String 	Namespace的名字
        // appId 	true 	String 	Namespace所属的AppId
        // format 	true 	String 	Namespace的格式，只能是以下类型： properties、xml、json、yml、yaml
        // isPublic 	true 	boolean 	是否是公共文件
        // comment 	false 	String 	Namespace说明
        // dataChangeCreatedBy 	true 	String 	namespace的创建人，格式为域账号，也就是sso系统的User ID
        // {"appId":"company-xxxx-acctcenter","name":"000","comment":"000租户配置","isPublic":false,"format":"properties"}
        OpenAppNamespaceDTO appNamespaceDTO = new OpenAppNamespaceDTO();
        appNamespaceDTO.setName(namespace);
        appNamespaceDTO.setAppId(appId);
        appNamespaceDTO.setFormat("properties");
        appNamespaceDTO.setPublic(false);
        appNamespaceDTO.setComment(comment);
        appNamespaceDTO.setDataChangeCreatedBy("apollo");
        httpApiService.createPrivateAppNamespace(appNamespaceDTO);
    }

    @Test
    public void testUpsertIteam() throws Exception {

        // 参数名 	必选 	类型 	说明
        // key 	true 	String 	配置的key，长度不能超过128个字符
        // value 	true 	String 	配置的value，长度不能超过20000个字符
        // comment 	false 	String 	配置的备注,长度不能超过1024个字符
        // dataChangeCreatedBy 	true 	String 	item的创建人，格式为域账号，也就是sso系统的User ID

        ItemDTO itemDTO = new ItemDTO();
        OpenItemDTO openItemDTO = new OpenItemDTO();

        itemDTO.setItemDTO(openItemDTO);

        itemDTO.setAppId("open-api-test003_id");
        itemDTO.setClusterName("default");
        itemDTO.setEnv("DEV");
        itemDTO.setNamespaceName("CREP.PUBLICTEST");

        String operator = "apollo";
        openItemDTO.setComment("API添加");
        String key = "apikey";
        // key = URLEncoder.encode(key, "UTF-8");
        openItemDTO.setKey(key);
        openItemDTO.setValue("api_value_3_2");
        openItemDTO.setDataChangeCreatedBy("apollo");
        openItemDTO.setDataChangeLastModifiedBy("apollo");

        apolloOpenApiClient.createItem(itemDTO.getAppId(), itemDTO.getEnv(), itemDTO.getClusterName(),
            itemDTO.getNamespaceName(), itemDTO.getItemDTO());

        apolloOpenApiClient.removeItem(itemDTO.getAppId(), itemDTO.getEnv(), itemDTO.getClusterName(),
            itemDTO.getNamespaceName(), itemDTO.getItemDTO()
                .getKey(), operator);

    }


    @Test
    public void testUrl() throws Exception {

        String url = "apps/open-api-test003_id";
        httpApiService.getApiInfo(url);

    }

    @Test
    public void testCreatApp() throws Exception {
        String appJson
            = "{\"appId\":\"company-xxxx-kylin-query\",\"name\":\"company-xxxx-kylin-query\",\"orgId\":\"xxxx\",\"orgName\":\"宁波银行\",\"ownerName\":\"apollo\",\"admins\":[\"xuxd\"]}";
        // = "{\"appId\":\"open-api-test003_id\",\"name\":\"open-api-test003_name\",\"orgId\":\"CREP\",\"orgName\":\"信贷产品部\",\"ownerName\":\"apollo\",\"admins\":[\"apollo\"]}";
        AppDTO dto = JsonUtil.json2Obj(appJson, AppDTO.class);
        httpApiService.creatApp(dto);

    }

    /**
     * 根据appId建立 app
     *
     * @throws Exception
     */
    @Test
    public void testCreatApps() throws Exception {
        String apps = "company-xxxx-user\n" + "company-xxxx-acctcenter\n" + "company-xxxx-acctdayend\n"
            + "company-xxxx-asset\n" + "company-xxxx-convert\n" + "company-xxxx-market\n" + "company-xxxx-cc\n"
            + "company-xxxx-credit\n" + "company-xxxx-flow\n" + "company-xxxx-platform\n" + "company-xxxx-gateway\n"
            + "company-xxxx-job-admin\n" + "company-xxxx-kylin-query\n" + "company-xxxx-api-web\n"+"company-xxxx-public";
        String[] appIds = apps.split("\n");
        for (String appId : appIds) {
            createAppById(appId);
        }

    }

    /**
     * 建立 Namespaces
     *
     * @throws Exception
     */
    @Test
    public void createNamespaces()  {
        String apps = "company-xxxx-user\n" + "company-xxxx-acctcenter\n" + "company-xxxx-acctdayend\n"
            + "company-xxxx-asset\n" + "company-xxxx-convert\n" + "company-xxxx-market\n" + "company-xxxx-cc\n"
            + "company-xxxx-credit\n" + "company-xxxx-flow\n" + "company-xxxx-platform\n" + "company-xxxx-gateway\n"
            + "company-xxxx-job-admin\n" + "company-xxxx-kylin-query\n" + "company-xxxx-api-web\n";
        String[] appIds = apps.split("\n");
        for (String appId : appIds) {
            try {
                createNamespaceByAppId("000",appId,"000租户配置");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 关联公共nameSapce
     *
     * @throws Exception
     */
    @Test
    public void relevantPublicNamespaces()  {
        String apps = "company-xxxx-user\n" + "company-xxxx-acctcenter\n" + "company-xxxx-acctdayend\n"
            + "company-xxxx-asset\n" + "company-xxxx-convert\n" + "company-xxxx-market\n" + "company-xxxx-cc\n"
            + "company-xxxx-credit\n" + "company-xxxx-flow\n" + "company-xxxx-platform\n" + "company-xxxx-gateway\n"
            + "company-xxxx-job-admin\n" + "company-xxxx-kylin-query\n" + "company-xxxx-api-web\n";
        String[] appIds = apps.split("\n");
        for (String appId : appIds) {
            try {
                httpApiService.createlAssociatedAppNamespace(appId,"xxxx.system");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void createAppById(String appId) {
        String appJson
            = "{\"appId\":\"%s\",\"name\":\"%s\",\"orgId\":\"xxxx\",\"orgName\":\"宁波银行\",\"ownerName\":\"apollo\",\"admins\":[\"xuxd\"]}";
        // = "{\"appId\":\"open-api-test003_id\",\"name\":\"open-api-test003_name\",\"orgId\":\"CREP\",\"orgName\":\"信贷产品部\",\"ownerName\":\"apollo\",\"admins\":[\"apollo\"]}";
        AppDTO dto = JsonUtil.json2Obj(String.format(appJson,appId,appId), AppDTO.class);
        try {
            httpApiService.creatApp(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteApp() throws Exception {
        String appId = "open-api-test003_id";
        httpApiService.delteApp(appId);

    }

    /**
     * 查询APP所有信息
     */
    @Test
    public void listOneApps() {

        String appId = "open-api-test003_id";
        getMoreAppInfo(appId);
        getMoreAppInfo("open-api-test002_id");

    }

    private void getMoreAppInfo(String appId) {
        String url = String.format("/apps/%s?operator=apollo", appId);
        httpApiService.getApiInfo(url);
        openAPI.queryOpenEnvClusterDTOList(appId);
        openAPI.queryOpenNamespaceDTOList(appId, "default", "DEV");
    }

    /**
     * 查询所有APP
     */
    @Test
    public void listAllApps() {

        List<OpenAppDTO> openAppDTOList = httpApiService.getAppsInfo();

        log.info("共有{}个应用！", openAppDTOList.size());
        for (OpenAppDTO openAppDTO : openAppDTOList) {
            System.out.println(ApolloPropertiesUtil.getEnumApp(openAppDTO));
        }
        log.info(JsonUtil.toPrettyFormat(openAppDTOList));

        openAppDTOList.stream()
            .filter(x -> x.getAppId()
                .contains("xxxx"))
            .forEach(x -> {
                System.out.println(x.getAppId());
            });

    }

    /**
     * 导出所有APP
     */
    @Test
    public void testExportAllBylistApps() throws Exception {

        List<OpenAppDTO> openAppDTOList = httpApiService.getAppsInfo();

        openAppDTOList = ScopeUtil.filterApps(openAppDTOList);

        log.info("共有{}个应用！", openAppDTOList.size());
        for (OpenAppDTO openAppDTO : openAppDTOList) {
            System.out.println(ApolloPropertiesUtil.getEnumApp(openAppDTO));
            openAPI.exportConfigByAppDto(openAppDTO);
        }
        log.info(JsonUtil.toPrettyFormat(openAppDTOList));

    }

    /**
     * 文件导入信息
     */
    @Test
    public void importNameSpaceConfigFile() throws Exception {
        //环境
        String env = "DEV";
        //修改人
        String userName = "apollo";

        //文件路径
        String filePath
            = "apollo-openapi/apollo/CREP/DEV/opt/data/open-api-test003_id/config-cache/open-api-test003_id+default+application.yml";
        openAPI.importConfigFile(env, userName, filePath);
    }


    private static Gson gson = (new GsonBuilder()).serializeNulls()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();

    @Test
    public void testString() {

        String str = "unhappy";
        log.info(str.substring(str.length())
            .trim());

        String param
            = "[{\"env\":\"DEV\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}},{\"env\":\"FAT\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}},{\"env\":\"UAT\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}}]";
        String jsonParam = param.replace("company-xxxx-credit", "loan2")
            .replace("xxxx.system", "system");

        System.out.print(gson.toJson(param));
    }


}