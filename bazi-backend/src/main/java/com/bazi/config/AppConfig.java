package com.bazi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;

/**
 * 应用配置类
 * 用于配置和管理应用程序的环境信息
 */
@Configuration
public class AppConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    
    @Autowired
    private Environment environment;
    
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;
    
    /**
     * 初始化配置
     */
    @PostConstruct
    public void init() {
        logger.info("应用正在使用配置文件: {}", activeProfile);
        logger.info("当前活动的配置文件: {}", String.join(", ", environment.getActiveProfiles()));
        
        // 根据不同环境打印适当的信息
        if (isLocalEnvironment()) {
            logger.info("本地开发环境已激活 - 使用本地开发配置");
        } else if (isDevEnvironment()) {
            logger.info("开发环境已激活 - 使用开发配置");
        } else if (isProdEnvironment()) {
            logger.info("生产环境已激活 - 使用生产配置");
        }
    }
    
    /**
     * 检查是否为本地开发环境
     */
    public boolean isLocalEnvironment() {
        return environment.acceptsProfiles(profiles -> profiles.test("local"));
    }
    
    /**
     * 检查是否为开发环境
     */
    public boolean isDevEnvironment() {
        return environment.acceptsProfiles(profiles -> profiles.test("dev"));
    }
    
    /**
     * 检查是否为生产环境
     */
    public boolean isProdEnvironment() {
        return environment.acceptsProfiles(profiles -> profiles.test("prod"));
    }
} 