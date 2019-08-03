package com.chen.lang.apollo.openapi.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-25 16:52
 */

public class IPUtil {

    /**
     * 获取主机目录
     *
     * @throws Exception
     */
    public static String getHostPath(String hostUrl) {
        try {
            URL url = new URL(hostUrl);
            String hostPath = url.getHost();
            if (url.getPort() > 0) {
                hostPath = hostPath + ":" + url.getPort();
            }
            return hostPath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
