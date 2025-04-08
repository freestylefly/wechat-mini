package com.bazi.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DeepSeek API响应对象
 * 
 * @author bazi-team
 */
@Data
@NoArgsConstructor
public class DeepSeekResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    
    /**
     * DeepSeek响应中的选择项
     */
    @Data
    @NoArgsConstructor
    public static class Choice {
        private Integer index;
        private Message message;
        private String finish_reason;
    }
    
    /**
     * DeepSeek响应中的消息
     */
    @Data
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
    
    /**
     * DeepSeek响应中的用量信息
     */
    @Data
    @NoArgsConstructor
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }
} 