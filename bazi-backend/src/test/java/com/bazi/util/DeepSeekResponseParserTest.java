package com.bazi.util;

import com.bazi.model.response.DeepSeekResponse;
import com.bazi.model.response.BaziCalculationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DeepSeek响应解析器测试
 * 
 * @author bazi-team
 */
@ExtendWith(MockitoExtension.class)
public class DeepSeekResponseParserTest {
    
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @InjectMocks
    private DeepSeekResponseParser parser;
    
    private DeepSeekResponse validResponse;
    
    @BeforeEach
    void setUp() throws IOException {
        // 创建一个有效的响应对象
        String json = "{\"id\":\"123\",\"object\":\"chat.completion\",\"created\":1620000000,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"{\\\"gregorianDate\\\":\\\"2023-05-15\\\",\\\"lunarDate\\\":\\\"三月廿六\\\",\\\"eightCharacters\\\":\\\"癸卯-乙巳-丁酉-壬子\\\",\\\"fiveElements\\\":\\\"水木-木火-火金-水水\\\",\\\"missingElements\\\":\\\"土\\\",\\\"zodiac\\\":\\\"兔\\\"}\"}}]}";
        validResponse = objectMapper.readValue(json, DeepSeekResponse.class);
    }
    
    @Test
    void parseResult_ValidResponse_Success() {
        // 执行解析
        BaziCalculationResult result = parser.parseResult(validResponse);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("2023-05-15", result.getGregorianDate());
        assertEquals("三月廿六", result.getLunarDate());
        assertEquals("癸卯-乙巳-丁酉-壬子", result.getEightCharacters());
        assertEquals("水木-木火-火金-水水", result.getFiveElements());
        assertEquals("土", result.getMissingElements());
        assertEquals("兔", result.getZodiac());
    }
    
    @Test
    void parseResult_NullResponse_ThrowsException() {
        // 执行解析并验证异常
        DeepSeekApiException exception = assertThrows(DeepSeekApiException.class, () -> {
            parser.parseResult(null);
        });
        
        // 验证异常内容
        assertTrue(exception.getMessage().contains("DeepSeek响应为空或无效"));
    }
    
    @Test
    void parseResult_EmptyChoices_ThrowsException() {
        // 创建一个无选项的响应
        DeepSeekResponse emptyResponse = new DeepSeekResponse();
        emptyResponse.setId("123");
        emptyResponse.setChoices(Collections.emptyList());
        
        // 执行解析并验证异常
        DeepSeekApiException exception = assertThrows(DeepSeekApiException.class, () -> {
            parser.parseResult(emptyResponse);
        });
        
        // 验证异常内容
        assertTrue(exception.getMessage().contains("DeepSeek响应为空或无效"));
    }
    
    @Test
    void parseResult_InvalidJson_ThrowsException() throws IOException {
        // 创建一个包含无效JSON的响应
        String json = "{\"id\":\"123\",\"object\":\"chat.completion\",\"created\":1620000000,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"这不是有效的JSON格式\"}}]}";
        DeepSeekResponse invalidResponse = objectMapper.readValue(json, DeepSeekResponse.class);
        
        // 执行解析并验证异常
        DeepSeekApiException exception = assertThrows(DeepSeekApiException.class, () -> {
            parser.parseResult(invalidResponse);
        });
        
        // 验证异常内容
        assertTrue(exception.getMessage().contains("无法从DeepSeek响应中提取JSON") 
                || exception.getMessage().contains("解析DeepSeek响应失败"));
    }
    
    @Test
    void parseResult_ResponseWithMarkdown_Success() throws IOException {
        // 创建一个包含Markdown格式的响应
        String json = "{\"id\":\"123\",\"object\":\"chat.completion\",\"created\":1620000000,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"以下是计算结果：\\n```json\\n{\\\"gregorianDate\\\":\\\"2023-05-15\\\",\\\"lunarDate\\\":\\\"三月廿六\\\",\\\"eightCharacters\\\":\\\"癸卯-乙巳-丁酉-壬子\\\",\\\"fiveElements\\\":\\\"水木-木火-火金-水水\\\",\\\"missingElements\\\":\\\"土\\\",\\\"zodiac\\\":\\\"兔\\\"}\\n```\"}}]}";
        DeepSeekResponse markdownResponse = objectMapper.readValue(json, DeepSeekResponse.class);
        
        // 执行解析
        BaziCalculationResult result = parser.parseResult(markdownResponse);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("2023-05-15", result.getGregorianDate());
        assertEquals("三月廿六", result.getLunarDate());
        assertEquals("癸卯-乙巳-丁酉-壬子", result.getEightCharacters());
        assertEquals("水木-木火-火金-水水", result.getFiveElements());
        assertEquals("土", result.getMissingElements());
        assertEquals("兔", result.getZodiac());
    }
} 