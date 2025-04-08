package com.bazi.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI服务配置类
 * 
 * @author bazi-team
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AIServiceConfig {
    /**
     * AI服务提供商：mock, deepseek, volcengine
     */
    private String provider = "mock";
    
    /**
     * 在API调用失败时是否使用模拟数据
     */
    private boolean useMockOnFailure = true;
} 