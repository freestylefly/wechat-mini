package com.bazi.model.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DeepSeek API请求模型
 * 
 * @author bazi-team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeepSeekRequest {
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 消息列表
     */
    private List<Message> messages;
    
    /**
     * 温度参数，控制随机性，取值范围0-1
     */
    private Double temperature;
    
    /**
     * 最大生成token数
     */
    @JsonProperty("max_tokens")
    private Integer max_tokens;
    
    /**
     * 是否流式输出
     */
    private Boolean stream;
    
    /**
     * Top-p采样参数，控制词汇选择的多样性
     */
    @JsonProperty("top_p")
    private Double top_p;
    
    /**
     * 重复惩罚参数
     */
    @JsonProperty("frequency_penalty")
    private Double frequency_penalty;
    
    /**
     * 存在惩罚参数
     */
    @JsonProperty("presence_penalty")
    private Double presence_penalty;
    
    /**
     * 停止序列
     */
    private List<String> stop;
    
    /**
     * 消息对象
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        /**
         * 角色，如"user"或"assistant"
         */
        private String role;
        
        /**
         * 消息内容
         */
        private String content;
    }
} 