package com.bazi.model.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek API响应模型
 * 
 * @author bazi-team
 */
@Data
public class DeepSeekResponse {
    
    /**
     * API请求ID
     */
    private String id;
    
    /**
     * 生成的对象类型
     */
    private String object;
    
    /**
     * 创建时间戳（Unix秒）
     */
    private Long created;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 生成的选项/结果列表
     */
    private List<Choice> choices;
    
    /**
     * 使用情况统计
     */
    private Usage usage;
    
    /**
     * DeepSeek生成的选项
     */
    @Data
    public static class Choice {
        /**
         * 消息
         */
        private Message message;
        
        /**
         * 索引
         */
        private Integer index;
        
        /**
         * 结束原因
         */
        @JsonProperty("finish_reason")
        private String finishReason;
    }
    
    /**
     * DeepSeek消息
     */
    @Data
    public static class Message {
        /**
         * 角色（user/assistant）
         */
        private String role;
        
        /**
         * 消息内容
         */
        private String content;
    }
    
    /**
     * DeepSeek使用情况统计
     */
    @Data
    public static class Usage {
        /**
         * 提示使用的token数
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        
        /**
         * 生成回复使用的token数
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        
        /**
         * 总token使用量
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
} 