package com.chen.lang.apollo.openapi.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chen.lang.apollo.openapi.util.GsonUtil;

/**
 * 具体参数描述，参考{@link Log}接口注释
 *
 * @author czq@chen.cn
 * @version $$Id: TenantLog.java, v 0.1 2018/9/19 上午9:01 czq@chen.cn Exp $$
 */
public final class TenantLog implements Log {

    /**
     * 缓存日志对象
     */
    private static Map<Object, TenantLog> loggerCache = new ConcurrentHashMap<>();

    /**
     * 底层日志实现对象
     */
    private Logger logger;

    /**
     * 私有构造函数
     *
     * @param logger logger
     */
    private TenantLog(Logger logger) {
        this.logger = logger;
    }

    /**
     * 获取Logger对象
     *
     * @param clazz 日志对象
     * @return {@link TenantLog}
     */
    public static TenantLog getLogger(Class<?> clazz) {
        if (loggerCache.get(clazz) != null) {
            return loggerCache.get(clazz);
        }
        TenantLog tenantLog = new TenantLog(LoggerFactory.getLogger(clazz));
        loggerCache.put(clazz, tenantLog);
        return tenantLog;
    }

    /**
     * 获取Logger对象
     *
     * @param className 日志对象类名
     * @return {@link TenantLog}
     */
    public static TenantLog getLogger(String className) {
        if (loggerCache.get(className) != null) {
            return loggerCache.get(className);
        }
        TenantLog tenantLog = new TenantLog(LoggerFactory.getLogger(className));
        loggerCache.put(className, tenantLog);
        return tenantLog;
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (logger.isTraceEnabled()) {
            logger.trace(format, arguments);
        }
    }

    @Override
    public void trace(Object format) {
        trace(GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    @Override
    public void debug(Object format) {
        debug(GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void info(String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    @Override
    public void info(Object format) {
        info(GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, arguments);
        }
    }

    @Override
    public void warn(Throwable e, String format, Object... arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(format(format, arguments), e);
        }
    }

    @Override
    public void warn(Object format) {
        warn(GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void warn(Throwable e, Object format) {
        warn(e, GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void error(String format, Object... arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(format, arguments);
        }
    }

    @Override
    public void error(Throwable e, String format, Object... arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(format(format, arguments), e);
        }
    }

    @Override
    public void error(Object format) {
        error(GsonUtil.obj2Json(format), (Object[]) null);
    }

    @Override
    public void error(Throwable e, Object format) {
        error(e, GsonUtil.obj2Json(format), (Object[]) null);
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values 参数值
     * @return 格式化后的文本
     */
    private String format(String template, Object... values) {
        return String.format(template.replace("{}", "%s"), values);
    }
}
