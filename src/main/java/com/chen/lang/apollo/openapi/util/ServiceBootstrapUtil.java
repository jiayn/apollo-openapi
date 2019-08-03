package com.chen.lang.apollo.openapi.util;

import java.util.List;
import java.util.ServiceLoader;

import com.google.common.collect.Lists;

/**
 * 使用SPI机制加载类
 *
 * @author czq@chen.cn
 * @version $$Id: ServiceBootstrapUtil.java, v 0.1 2018/10/20 9:19 PM czq@chen.cn Exp $$
 */
public class ServiceBootstrapUtil {

    /**
     * 根据class加载所有的类
     *
     * @param clazz 类
     * @param <T> 泛型
     * @return 所有的类
     */
    public static <T> List<T> loadAll(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        return Lists.newArrayList(loader.iterator());
    }

    /**
     * 根据class加载第一个类
     *
     * @param clazz 类
     * @param <T> 泛型
     * @return 第一个class的实现类
     */
    public static <T> T loadFirst(Class<T> clazz) {
        List<T> list = loadAll(clazz);
        if (CollectionUtil.isEmpty(list)) {
            throw new IllegalStateException(String.format(
                "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
                clazz.getName()));
        }
        return list.get(0);
    }
}
