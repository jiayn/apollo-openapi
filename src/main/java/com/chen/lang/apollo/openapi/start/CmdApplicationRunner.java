package com.chen.lang.apollo.openapi.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.chen.lang.apollo.openapi.service.CmdService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author czq@chen.cn.cn
 * @version V2.1
 * @since 2.1.0 2019-04-26 17:09
 */
@Component
@Order(value = Integer.MAX_VALUE)
@Slf4j
@ConditionalOnProperty(value = {"start.cmd.enable"}, matchIfMissing = false)
public class CmdApplicationRunner implements ApplicationRunner {

    @Autowired
    private CmdService cmdService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        cmdService.cmd();

    }
}
