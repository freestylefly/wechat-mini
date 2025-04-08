package com.bazi.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp客户端配置
 * 
 * @author bazi-team
 */
@Configuration
public class OkHttpConfig {
    
    /**
     * 连接超时时间（秒）
     */
    private static final int CONNECT_TIMEOUT = 30;
    
    /**
     * 读取超时时间（秒）
     */
    private static final int READ_TIMEOUT = 60;
    
    /**
     * 写入超时时间（秒）
     */
    private static final int WRITE_TIMEOUT = 60;
    
    /**
     * 配置OkHttpClient Bean
     * 
     * @return OkHttpClient实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }
} 