package com.bazi.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek API请求对象
 * 
 * @author bazi-team
 */
@Data
@Builder
public class DeepSeekRequest {
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 消息列表
     */
    private List<DeepSeekMessage> messages;
    
    /**
     * 采样温度，取值0.0-2.0
     */
    private Double temperature;
    
    /**
     * 生成的最大token数
     */
    private Integer max_tokens;
    
    /**
     * 是否流式响应
     */
    private Boolean stream;
    
    /**
     * 用于控制生成的文本中高概率词汇的出现概率，取值0.0-2.0
     */
    private Double top_p;
    
    /**
     * 频率惩罚系数，用于降低重复内容的出现概率
     */
    private Double frequency_penalty;
    
    /**
     * 存在惩罚系数，用于增加新内容的出现概率
     */
    private Double presence_penalty;
} 