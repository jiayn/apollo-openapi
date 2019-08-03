package com.chen.lang.apollo.openapi.log;

import java.util.List;
import java.util.Map;

import com.chen.lang.apollo.openapi.constant.SymbolConstants;
import com.google.common.collect.Maps;
import com.chen.lang.apollo.openapi.util.CollectionUtil;
import com.chen.lang.apollo.openapi.util.ServiceBootstrapUtil;
import com.chen.lang.apollo.openapi.util.StringUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * @author czq@chen.cn
 * @version $$Id: BusinessParameterConverter.java, v 0.1 2018/12/22 11:20 AM czq@chen.cn Exp $$
 */
public class BusinessParameterConverter extends CompositeConverter<ILoggingEvent> {

    @Override
    protected String transform(ILoggingEvent event, String in) {
        return print(getParameter());
    }

    /**
     * 通过SPI机制获取要打印的业务参数
     *
     * @return key-value形式的业务参数
     */
    private Map<String, String> getParameter() {
        Map<String, String> parameters = Maps.newHashMap();
        List<BusinessParameterPrinter> printerList = ServiceBootstrapUtil.loadAll(BusinessParameterPrinter.class);
        if (CollectionUtil.isNotEmpty(printerList)) {
            for (BusinessParameterPrinter businessParameterPrinter : printerList) {
                businessParameterPrinter.print(parameters);
            }
        }
        return parameters;
    }

    /**
     * 把value值不为空的参数进行组装成字符串
     *
     * @param parameters key-value形式的业务参数
     * @return 要打印的字符串
     */
    private String print(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        parameters.forEach((key, value) -> {
            if (StringUtil.isNotBlank(value)) {
                sb.append(key)
                    .append(SymbolConstants.COLON)
                    .append(StringUtil.SPACE)
                    .append(value)
                    .append(SymbolConstants.COMMA)
                    .append(StringUtil.SPACE);
            }
        });
        // 由于后面有长度 - 2的操作，所以这里要先判断长度是否已经大于2
        return sb.length() > 2 ? SymbolConstants.OPEN_BRACKET + sb.substring(0, sb.length() - 2)
            + SymbolConstants.CLOSE_BRACKET : StringUtil.EMPTY;
    }
}
