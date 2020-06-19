package com.example.demo.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author:dahua
 * @date:2020/6/19
 * @description:
 */
@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

    @Value("${test.log.enable}")
    private boolean enable;

    @Value("${test.class.log.level}")
    private String level;

    public TestController() {
        pool.scheduleAtFixedRate(new Thread(new Task()), 1, 1, TimeUnit.SECONDS);
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        log.info("-----------------------------------------------111");
        System.out.println(LogManager.getLogger(TestController.class).getLevel());
        return "1";
    }

    class Task implements Runnable {

        @Override
        public void run() {
            if (enable) {
                org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
                Configuration config = ctx.getConfiguration();
                LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
                Level l;
                switch (level) {
                    case "off":
                        l = Level.OFF;
                        break;
                    case "fatal":
                        l = Level.FATAL;
                        break;
                    case "info":
                        l = Level.INFO;
                        break;
                    case "debug":
                        l = Level.DEBUG;
                        break;
                    case "trace":
                        l = Level.TRACE;
                        break;
                    case "warn":
                        l = Level.WARN;
                        break;
                    case "error":
                        l = Level.ERROR;
                        break;
                    case "all":
                        l = Level.ALL;
                        break;
                    default:
                        l = Level.ERROR;
                        break;
                }
                loggerConfig.setLevel(l);
                ctx.updateLoggers();
            }
        }
    }
}
