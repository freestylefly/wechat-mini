package com.bazi.service.impl;

import com.bazi.model.request.CalculationRequest;
import com.bazi.model.response.DeepSeekResponse;
import com.bazi.service.DeepSeekService;
import com.bazi.util.DeepSeekApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * 模拟数据服务实现类，用于开发和测试环境
 * 
 * @author bazi-team
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "mock")
public class MockServiceImpl implements DeepSeekService {

    private final ObjectMapper objectMapper;
    
    @Value("${mock.data.response-content:}")
    private String mockResponseContent;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON处理器
     */
    public MockServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 请求计算，返回模拟数据
     * 
     * @param request 计算请求
     * @return 模拟响应
     */
    @Override
    public DeepSeekResponse requestCalculation(CalculationRequest request) throws DeepSeekApiException {
        log.info("使用模拟数据服务计算八字，日期: {}, 时辰: {}", request.getDate(), request.getTime());
        
        return getMockResponse(request);
    }
    
    /**
     * 获取模拟响应数据
     *
     * @param request 计算请求
     * @return 模拟响应数据
     */
    private DeepSeekResponse getMockResponse(CalculationRequest request) {
        DeepSeekResponse response = new DeepSeekResponse();
        response.setId("mock-" + System.currentTimeMillis());
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel("mock-model");
        response.setObject("chat.completion");
        
        DeepSeekResponse.Message message = new DeepSeekResponse.Message();
        message.setRole("assistant");
        
        // 如果配置中有响应内容，则使用配置中的内容
        String content;
        if (StringUtils.hasText(mockResponseContent)) {
            // 替换日期和时辰
            content = mockResponseContent
                    .replace("1990-01-01", request.getDate())
                    .replace("甲子", getTimeStem(request.getTime()));
        } else {
            // 否则使用默认生成的模拟数据
            content = "{\n" +
                    "  \"gregorianDate\": \"" + request.getDate() + "\",\n" +
                    "  \"lunarDate\": \"十一月十五\",\n" +
                    "  \"eightCharacters\": \"己巳-戊子-壬午-" + getTimeStem(request.getTime()) + "\",\n" +
                    "  \"fiveElements\": \"土火-土水-水火-" + getTimeElement(request.getTime()) + "\",\n" +
                    "  \"missingElements\": \"金木\",\n" +
                    "  \"zodiac\": \"" + getZodiacByYear(request.getDate()) + "\"\n" +
                    "}";
        }
        
        message.setContent(content);
        
        DeepSeekResponse.Choice choice = new DeepSeekResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(message);
        choice.setFinish_reason("stop");
        
        response.setChoices(Collections.singletonList(choice));
        
        DeepSeekResponse.Usage usage = new DeepSeekResponse.Usage();
        usage.setPrompt_tokens(100);
        usage.setCompletion_tokens(100);
        usage.setTotal_tokens(200);
        response.setUsage(usage);
        
        log.info("生成模拟响应: {}", content);
        
        return response;
    }
    
    /**
     * 根据时辰获取对应的天干地支
     * 
     * @param time 时辰
     * @return 天干地支
     */
    private String getTimeStem(String time) {
        if (!StringUtils.hasText(time)) {
            return "甲子";
        }
        
        switch (time) {
            case "子时": return "甲子";
            case "丑时": return "乙丑";
            case "寅时": return "丙寅";
            case "卯时": return "丁卯";
            case "辰时": return "戊辰";
            case "巳时": return "己巳";
            case "午时": return "庚午";
            case "未时": return "辛未";
            case "申时": return "壬申";
            case "酉时": return "癸酉";
            case "戌时": return "甲戌";
            case "亥时": return "乙亥";
            default: return "甲子";
        }
    }
    
    /**
     * 根据时辰获取对应的五行
     * 
     * @param time 时辰
     * @return 五行
     */
    private String getTimeElement(String time) {
        if (!StringUtils.hasText(time)) {
            return "木水";
        }
        
        switch (time) {
            case "子时": return "木水";
            case "丑时": return "木土";
            case "寅时": return "火木";
            case "卯时": return "火木";
            case "辰时": return "土土";
            case "巳时": return "土火";
            case "午时": return "金火";
            case "未时": return "金土";
            case "申时": return "水金";
            case "酉时": return "水金";
            case "戌时": return "木土";
            case "亥时": return "木水";
            default: return "木水";
        }
    }
    
    /**
     * 根据年份获取生肖
     * 
     * @param dateStr 日期字符串 (YYYY-MM-DD)
     * @return 生肖
     */
    private String getZodiacByYear(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return "鼠";
        }
        
        int year;
        try {
            year = Integer.parseInt(dateStr.substring(0, 4));
        } catch (Exception e) {
            return "鼠";
        }
        
        String[] zodiacs = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return zodiacs[(year - 4) % 12];
    }
} 