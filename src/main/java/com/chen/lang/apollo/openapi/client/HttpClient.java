package com.chen.lang.apollo.openapi.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.ctrip.framework.apollo.openapi.client.exception.ApolloOpenApiException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-23 17:32
 */
@Slf4j
public class HttpClient {

    private static Gson gson = (new GsonBuilder()).serializeNulls()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();

    private final String baseUrl;

    private static final Escaper pathEscaper = UrlEscapers.urlPathSegmentEscaper();
    private static final Escaper queryParamEscaper = UrlEscapers.urlFormParameterEscaper();

    private final CloseableHttpClient client;

    public HttpClient(CloseableHttpClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
    }

    protected CloseableHttpResponse get(String path) throws IOException {
        HttpGet get = new HttpGet(String.format("%s/%s", baseUrl, path));

        return execute(get);
    }

    protected CloseableHttpResponse post(String path, Object entity) throws IOException {
        HttpPost post = new HttpPost(String.format("%s/%s", baseUrl, path));

        return execute(post, entity);
    }

    protected CloseableHttpResponse put(String path, Object entity) throws IOException {
        HttpPut put = new HttpPut(String.format("%s/%s", baseUrl, path));

        return execute(put, entity);
    }

    protected CloseableHttpResponse delete(String path) throws IOException {
        HttpDelete delete = new HttpDelete(String.format("%s/%s", baseUrl, path));

        return execute(delete);
    }

    protected String escapePath(String path) {
        return pathEscaper.escape(path);
    }

    protected String escapeParam(String param) {
        return queryParamEscaper.escape(param);
    }

    private CloseableHttpResponse execute(HttpEntityEnclosingRequestBase requestBase, Object entity)
    throws IOException {
        String param = (entity instanceof String) ? (String) entity : gson.toJson(entity);

        log.debug("请求 = {}, 参数： {}", requestBase.getURI()
            .getPath(), param);
        requestBase.setEntity(new StringEntity(param, ContentType.APPLICATION_JSON));

        return execute(requestBase);
    }

    private CloseableHttpResponse execute(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = client.execute(request);

        checkHttpResponseStatus(response);

        return response;
    }

    private void checkHttpResponseStatus(HttpResponse response) {
        if (response.getStatusLine()
            .getStatusCode() == 200) {
            return;
        }

        StatusLine status = response.getStatusLine();
        String message = "";
        try {
            message = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            //ignore
        }

        throw new ApolloOpenApiException(status.getStatusCode(), status.getReasonPhrase(), message);
    }

    protected void checkNotEmpty(String value, String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value), name + " should not be null or empty");
    }

}
