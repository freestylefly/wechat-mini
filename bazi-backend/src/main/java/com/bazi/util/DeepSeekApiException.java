package com.bazi.util;

import lombok.Getter;

/**
 * DeepSeek API调用异常
 * 
 * @author bazi-team
 */
@Getter
public class DeepSeekApiException extends RuntimeException {
    
    private final int code;
    
    /**
     * 创建DeepSeek API异常
     * 
     * @param message 错误消息
     */
    public DeepSeekApiException(String message) {
        this(502, message);
    }
    
    /**
     * 创建DeepSeek API异常
     * 
     * @param message 错误消息
     * @param cause 原始异常
     */
    public DeepSeekApiException(String message, Throwable cause) {
        this(502, message, cause);
    }
    
    /**
     * 创建DeepSeek API异常
     * 
     * @param code 错误码
     * @param message 错误消息
     */
    public DeepSeekApiException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 创建DeepSeek API异常
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public DeepSeekApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
} 