package com.chen.lang.apollo.openapi.log;

/**
 * 可使用{@link lombok.extern.slf4j.Slf4j}注解来代替该{@link Log}接口<br>
 * 如果需要打印个性化参数，使用SPI机制实现{@link BusinessParameterPrinter}接口即可<br>
 *
 * @author czq@chen.cn
 * @version $$Id: Log.java, v 0.1 2018/9/19 上午9:01 czq@chen.cn Exp $$
 */
public interface Log {

    /**
     * Trace等级日志，小于debug
     *
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void trace(String format, Object... arguments);

    /**
     * Trace等级日志，小于debug
     *
     * @param format 内部会调用gson转成String类型
     */
    void trace(Object format);

    /**
     * Debug等级日志，小于Info
     *
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void debug(String format, Object... arguments);

    /**
     * Debug等级日志，小于Info
     *
     * @param format 内部会调用gson转成String类型
     */
    void debug(Object format);

    /**
     * Info等级日志，小于Warn
     *
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */

    void info(String format, Object... arguments);

    /**
     * Info等级日志，小于Warn
     *
     * @param format 内部会调用gson转成String类型
     */
    void info(Object format);

    /**
     * Warn等级日志，小于Error
     *
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void warn(String format, Object... arguments);

    /**
     * Warn等级日志，小于Error
     *
     * @param e 需在日志中堆栈打印的异常
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void warn(Throwable e, String format, Object... arguments);

    /**
     * Warn等级日志，小于Error
     *
     * @param format 内部会调用gson转成String类型
     */
    void warn(Object format);

    /**
     * Warn等级日志，小于Error
     *
     * @param e 需在日志中堆栈打印的异常
     * @param format 内部会调用gson转成String类型
     */
    void warn(Throwable e, Object format);

    /**
     * Error等级日志
     *
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void error(String format, Object... arguments);

    /**
     * Error等级日志
     *
     * @param e 需在日志中堆栈打印的异常
     * @param format 格式文本，{} 代表变量
     * @param arguments 变量对应的参数
     */
    void error(Throwable e, String format, Object... arguments);

    /**
     * Error等级日志
     *
     * @param format 内部会调用gson转成String类型
     */
    void error(Object format);

    /**
     * Error等级日志
     *
     * @param e 需在日志中堆栈打印的异常
     * @param format 内部会调用gson转成String类型
     */
    void error(Throwable e, Object format);
}
