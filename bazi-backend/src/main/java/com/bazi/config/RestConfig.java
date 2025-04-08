package com.bazi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * REST相关配置，包括CORS等
 * 
 * @author bazi-team
 */
@Configuration
public class RestConfig {

    /**
     * 配置CORS过滤器，允许跨域请求
     * 
     * @return CorsFilter CORS过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 使用通配符允许所有来源
        config.addAllowedOrigin("*");
        
        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 允许所有HTTP方法
        config.addAllowedMethod("*");
        
        // 设置预检请求的有效期，单位秒
        config.setMaxAge(3600L);
        
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
    
    /**
     * 添加安全响应头过滤器
     * 处理Referrer-Policy问题
     */
    @Bean
    public FilterRegistrationBean<Filter> responseHeaderFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                             FilterChain filterChain) throws ServletException, IOException {
                // 设置Referrer Policy，允许小程序发送完整的referrer信息
                response.setHeader("Referrer-Policy", "no-referrer-when-downgrade");
                
                // 设置其他安全响应头
                response.setHeader("X-Content-Type-Options", "nosniff");
                response.setHeader("X-XSS-Protection", "1; mode=block");
                
                // 允许通过HTTPS和HTTP访问
                response.setHeader("Strict-Transport-Security", "max-age=0");
                
                filterChain.doFilter(request, response);
            }
        });
        
        // 设置过滤器顺序
        filterRegistrationBean.setOrder(1);
        // 设置过滤器的URL模式
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}