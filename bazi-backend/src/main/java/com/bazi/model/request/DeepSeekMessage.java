package com.bazi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DeepSeek API的消息对象
 * 
 * @author bazi-team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeepSeekMessage {
    /**
     * 消息角色，如 "system", "user", "assistant"
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
} 