package com.chen.lang.apollo.openapi.client;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.chen.lang.apollo.openapi.entity.dto.AppDTO;
import com.chen.lang.apollo.openapi.entity.dto.OpenAppDTO;
import com.chen.lang.apollo.openapi.util.JsonUtil;
import com.ctrip.framework.apollo.openapi.client.constant.ApolloOpenApiConstants;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.chen.lang.apollo.openapi.util.IPUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-23 14:01
 */
@Slf4j
public class HttpApiService {

    private static final Type OPEN_APP_DTO_LIST_TYPE = (new TypeToken<List<OpenAppDTO>>() {
    }).getType();

    private String hostUrl;
    private HttpClient httpClient;

    public HttpApiService(String hostUrl, String userName, String password) {
        this.hostUrl = hostUrl;
        this.httpClient = new HttpClient(getCloseableHttpClient(), hostUrl);
        login(userName, password);
    }

    @Deprecated
    public HttpApiService() {
        this("http://106.12.25.204:8070/", "apollo", "admin");
    }
    /**
     * 获取所有APP
     *
     * @return
     */
    public List<OpenAppDTO> getAppsInfo() {

        String path = "apps";
        try (CloseableHttpResponse response = httpClient.get(path)) {
            System.out.println(hostUrl + " 返回结果为：" + response);
            Gson gson = new GsonBuilder().setDateFormat(ApolloOpenApiConstants.JSON_DATE_FORMAT)
                .create();
            return gson.fromJson(EntityUtils.toString(response.getEntity()), OPEN_APP_DTO_LIST_TYPE);
        } catch (Throwable ex) {
            throw new RuntimeException(String.format("Load app information for appIds: %s failed", hostUrl), ex);
        }
    }

    private CloseableHttpClient getCloseableHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(1000)
            .setSocketTimeout(5000)
            .build();

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setRedirectStrategy(new DefaultRedirectStrategy() {
                @Override
                public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                    boolean isRedirect = false;
                    try {
                        isRedirect = super.isRedirected(request, response, context);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    if (!isRedirect) {
                        int responseCode = response.getStatusLine()
                            .getStatusCode();
                        if (responseCode == 301 || responseCode == 302) {
                            return true;
                        }
                    }
                    return isRedirect;
                }
            })
            .setDefaultHeaders(Lists.newArrayList(new BasicHeader("Host", IPUtil.getHostPath(hostUrl)),
                new BasicHeader("User-Agent",
                    " Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:66.0) Gecko/20100101 Firefox/66.0"),
                new BasicHeader("Accept", " application/json, text/plain, */*"),
                new BasicHeader("Accept-Language", " zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2"),
                new BasicHeader("Accept-Encoding", " gzip, deflate"), new BasicHeader("Referer", hostUrl),
                new BasicHeader("Connection", "keep-alive"),
                new BasicHeader("Cookie", "hibext_instdsigdipv2=1; JSESSIONID=B16E01A6C1617B826D644AE8FD6C82C6"),
                new BasicHeader("Pragma", " no-cache"), new BasicHeader("Cache-Control", " no-cache")))
            .build();
    }

    /**
     * 执行API
     *
     * @param baseUrl
     * @throws Exception
     */
    public void getApiInfo(String baseUrl) {

        try (CloseableHttpResponse response = httpClient.get(baseUrl)) {
            System.out.println(baseUrl + " 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));

        } catch (Throwable ex) {
            throw new RuntimeException(String.format("Load app information for getApiInfo: %s failed", baseUrl), ex);
        }

    }

    /**
     * 执行API
     *
     * @param baseUrl
     * @throws Exception
     */
    public void postApiInfo(String baseUrl, Object entity) {

        try (CloseableHttpResponse response = httpClient.post(baseUrl, entity)) {
            System.out.println(baseUrl + " 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));

        } catch (Throwable ex) {
            throw new RuntimeException(String.format("Load app information for getApiInfo: %s failed", baseUrl), ex);
        }
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    private void login(String userName, String password) {
        String loginSubmit = "登录";
        String path = String.format("/signin?username=%s&password=%s&login-submit=%s", userName, password, loginSubmit);
        try (CloseableHttpResponse response = httpClient.post(path, null)) {
            System.out.println(path + " 登录成功！ 返回结果为：" + response);
        } catch (Throwable ex) {
            log.error(String.format("login : %s failed", path), ex);
        }
    }

    /**
     * 生成新的 APP
     *
     * @param dto
     */
    public void creatApp(AppDTO dto) {
        try (CloseableHttpResponse response = httpClient.post("apps", dto)) {
            System.out.println(" 生成新的 APP 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));
        } catch (Throwable ex) {
            log.error(String.format("creat app information for : %s failed", hostUrl), ex);
        }

    }

    /**
     * 创建私有的nameSpace
     *
     * @param dto
     */
    public void createPrivateAppNamespace(OpenAppNamespaceDTO dto) {

        String path = String.format("/apps/%s/appnamespaces?appendNamespacePrefix=false", dto.getAppId());
        try (CloseableHttpResponse response = httpClient.post(path, dto)) {
            System.out.println(" 生成新的 nameSpace 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));
        } catch (Throwable ex) {
            log.error(String.format("creat nameSpace information for : %s failed", hostUrl), ex);
        }

    }

    /**
     * 创建关联的nameSpace
     */
    public void createlAssociatedAppNamespace(String appId, String publicNameSapceName) {
        String path = String.format("/apps/%s/namespaces", appId);
        String param
            = "[{\"env\":\"DEV\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}},{\"env\":\"FAT\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}},{\"env\":\"UAT\",\"namespace\":{\"appId\":\"company-xxxx-credit\",\"clusterName\":\"default\",\"namespaceName\":\"xxxx.system\"}}]";

        String jsonParam = param.replace("company-xxxx-credit", appId)
            .replace("xxxx.system", publicNameSapceName);

        try (CloseableHttpResponse response = httpClient.post(path, jsonParam)) {
            System.out.println(" 生成新的 关联nameSpace 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));
        } catch (Throwable ex) {
            throw new RuntimeException(String.format("creat 关联nameSpace information for : %s failed", hostUrl + path),
                ex);
        }
    }

    /**
     * token 第三方应用 管理APP
     */
    public void assignAPPRole(String appId, String token) {
        String path = String.format("/consumers/%s/assign-role?type=AppRole", token);
        String param = "{\"appId\":\"%s\"}";
        String jsonParam = String.format(param, appId);
        try (CloseableHttpResponse response = httpClient.post(path, jsonParam)) {
            System.out.println(" 第三方应用 管理APP 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));
        } catch (Throwable ex) {
            throw new RuntimeException(String.format("第三方应用 管理APP information for : %s failed", hostUrl + path), ex);
        }
    }

    /**
     * 删除 APP
     *
     * @param appId
     */
    public void delteApp(String appId) {
        String path = String.format("/apps/%s?operator=apollo", appId);
        try (CloseableHttpResponse response = httpClient.delete("path")) {
            System.out.println(" 删除 APP 返回结果为：" + response);
            System.out.println(JsonUtil.toPrettyFormat(EntityUtils.toString(response.getEntity())));
        } catch (Throwable ex) {
            throw new RuntimeException(String.format("delte app information for : %s failed,url=%s", appId, path), ex);
        }

    }



}
