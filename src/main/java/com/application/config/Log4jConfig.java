//package com.application.config;
//
//import jakarta.annotation.PostConstruct;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.config.Configurator;
//import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
//import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
//import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
//import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
//import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class Log4jConfig {
//
//    @PostConstruct
//    public void init() {
//        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
//
//        builder.setConfigurationName("CustomLog4j2Config");
//        builder.setStatusLevel(org.apache.logging.log4j.Level.ERROR);
//
//        // Console Appender with highlight and styles
//        AppenderComponentBuilder consoleAppender = builder.newAppender("Console", "Console")
//                .addAttribute("target", "SYSTEM_OUT");
//
//        consoleAppender.add(builder.newLayout("PatternLayout")
//                .addAttribute("pattern", "%style{%d{ISO8601}}{black} %highlight{%-5level }[%style{%t}{bright,blue}] %style{%C{1}}{bright,yellow}: %msg%n%throwable"));
//
//        builder.add(consoleAppender);
//
//        // RollingFile Appender
//        AppenderComponentBuilder rollingFileAppender = builder.newAppender("rolling", "RollingFile")
//                .addAttribute("fileName", "rolling.log")
//                .addAttribute("filePattern", "./logs/$${date:yyyy-MM-dd}/spring-boot-logger-log4j2-%d{-dd-MMMM-yyyy}-%i.log.gz");
//
//        rollingFileAppender.add(builder.newLayout("PatternLayout")
//                .addAttribute("pattern", "%d %p %C{1} [%t] %m%n"));
//
//        // SocketAppender (raw logs to Logstash)
//        AppenderComponentBuilder socketAppender = builder.newAppender("LogstashSocket", "Socket")
//                .addAttribute("host", System.getenv().getOrDefault("LOGSTASH_HOST", "localhost"))
//                .addAttribute("port", Integer.parseInt(System.getenv().getOrDefault("LOGSTASH_PORT", "5000")))
//                .addAttribute("protocol", "TCP");
//
//        socketAppender.add(builder.newLayout("JsonLayout")
//                .addAttribute("compact", true)
//                .addAttribute("eventEol", true));
//
//        builder.add(socketAppender);
//
//        // Async wrapper
//        AppenderComponentBuilder asyncLogstash = builder.newAppender("AsyncLogstash", "Async")
//                .addAttribute("bufferSize", 1024)
//                .addComponent(builder.newAppenderRef("LogstashSocket"));
//        builder.add(asyncLogstash);
//
//
//        // Root logger
//        builder.add(builder.newRootLogger(org.apache.logging.log4j.Level.INFO)
//                .add(builder.newAppenderRef("AsyncLogstash")));
//
//
//        ComponentBuilder<?> triggeringPolicies = builder.newComponent("Policies")
//                .addComponent(builder.newComponent("OnStartupTriggeringPolicy"))
//                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10 MB"))
//                .addComponent(builder.newComponent("TimeBasedTriggeringPolicy"));
//
//        rollingFileAppender.addComponent(triggeringPolicies);
//
//        builder.add(rollingFileAppender);
//
//        // Root Logger
//        builder.add(builder.newRootLogger(org.apache.logging.log4j.Level.INFO)
//                .add(builder.newAppenderRef("Console"))
//                .add(builder.newAppenderRef("RollingFile")));
//
//        // Logger for com.application
//        builder.add(builder.newLogger("com.application", org.apache.logging.log4j.Level.TRACE));
//
//        // Initialize context
//        LoggerContext context = Configurator.initialize(builder.build());
//        context.updateLoggers();
//    }
//}
