package com.bazi.util;

import com.bazi.model.response.DeepSeekResponse;
import com.bazi.model.response.BaziCalculationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * DeepSeek响应解析工具类
 * 
 * @author bazi-team
 */
@Slf4j
@Component
public class DeepSeekResponseParser {
    
    private final ObjectMapper objectMapper;
    
    public DeepSeekResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * 从DeepSeek响应解析计算结果
     * 
     * @param response DeepSeek API响应
     * @return 八字计算结果
     * @throws DeepSeekApiException 如果解析失败
     */
    public BaziCalculationResult parseResult(DeepSeekResponse response) {
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new DeepSeekApiException("DeepSeek响应为空或无效");
        }
        
        try {
            // 获取消息内容
            String content = response.getChoices().get(0).getMessage().getContent();
            if (!StringUtils.hasText(content)) {
                throw new DeepSeekApiException("DeepSeek响应内容为空");
            }
            
            // 从内容中提取JSON部分
            String jsonContent = extractJsonFromContent(content);
            if (!StringUtils.hasText(jsonContent)) {
                throw new DeepSeekApiException("无法从DeepSeek响应中提取JSON");
            }
            
            log.debug("提取到的JSON内容: {}", jsonContent);
            
            // 解析JSON内容
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            
            return BaziCalculationResult.builder()
                    .gregorianDate(getStringValue(jsonNode, "gregorianDate"))
                    .lunarDate(getStringValue(jsonNode, "lunarDate"))
                    .eightCharacters(getStringValue(jsonNode, "eightCharacters"))
                    .fiveElements(getStringValue(jsonNode, "fiveElements"))
                    .missingElements(getStringValue(jsonNode, "missingElements"))
                    .zodiac(getStringValue(jsonNode, "zodiac"))
                    .build();
            
        } catch (JsonProcessingException e) {
            log.error("解析DeepSeek响应失败", e);
            throw new DeepSeekApiException("解析DeepSeek响应失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从文本内容中提取JSON部分
     * 
     * @param content 内容文本
     * @return JSON字符串
     */
    private String extractJsonFromContent(String content) {
        // 查找第一个 { 和最后一个 } 的位置
        int start = content.indexOf('{');
        int end = content.lastIndexOf('}');
        
        if (start >= 0 && end > start) {
            return content.substring(start, end + 1);
        }
        
        return null;
    }
    
    /**
     * 从JsonNode中获取字符串值
     * 
     * @param jsonNode JsonNode对象
     * @param fieldName 字段名称
     * @return 字符串值，如果不存在则返回空字符串
     */
    private String getStringValue(JsonNode jsonNode, String fieldName) {
        if (jsonNode.has(fieldName)) {
            return jsonNode.get(fieldName).asText();
        }
        return "";
    }
} 