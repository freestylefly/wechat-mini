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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

// 火山引擎SDK导入
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

/**
 * 火山引擎API服务实现类
 * 基于火山引擎SDK实现
 * 
 * @author bazi-team
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "volcengine")
public class VolcengineServiceImpl implements DeepSeekService {

    private final ObjectMapper objectMapper;
    private ArkService arkService;
    
    @Value("${volcengine.api.key}")
    private String apiKey;
    
    @Value("${volcengine.api.timeout:30}")
    private int timeout;
    
    @Value("${volcengine.api.model}")
    private String model;
    
    @Value("${volcengine.api.max-connections:5}")
    private int maxConnections;
    
    @Value("${volcengine.api.connection-ttl:60}")
    private int connectionTtl;
    
    @Value("${volcengine.api.prompt}")
    private String prompt;
    
    @Value("${ai.use-mock-on-failure:false}")
    private boolean useMockOnFailure;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON处理器
     */
    public VolcengineServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * 初始化火山引擎服务
     */
    @PostConstruct
    public void init() {
        log.info("初始化火山引擎服务，模型: {}", model);
        
        try {
            ConnectionPool connectionPool = new ConnectionPool(maxConnections, connectionTtl, TimeUnit.SECONDS);
            Dispatcher dispatcher = new Dispatcher();
            
            // 按照Sample示例初始化ArkService
            arkService = ArkService.builder()
                    .apiKey(apiKey)
                    .connectionPool(connectionPool)
                    .dispatcher(dispatcher)
                    .build();
            
            log.info("火山引擎服务初始化成功");
        } catch (Exception e) {
            log.error("火山引擎服务初始化失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 关闭火山引擎服务
     */
    @PreDestroy
    public void destroy() {
        if (arkService != null) {
            log.info("关闭火山引擎服务");
            arkService.shutdownExecutor();
        }
    }

    /**
     * 请求计算
     * 
     * @param request 计算请求
     * @return DeepSeek响应
     * @throws DeepSeekApiException API调用异常
     */
    @Override
    public DeepSeekResponse requestCalculation(CalculationRequest request) throws DeepSeekApiException {
        String content = formatPrompt(request);
        
        log.info("正在请求火山引擎API进行八字计算，模型: {}, 请求内容: {}", model, content);
        
        try {
            if (arkService == null) {
                throw new DeepSeekApiException("火山引擎服务尚未初始化");
            }
            
            // 构建消息列表
            List<ChatMessage> messages = new ArrayList<>();
            
            // 添加系统提示
            ChatMessage systemMessage = ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content(prompt)
                    .build();
            messages.add(systemMessage);
            
            // 添加用户请求
            ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(content)
                    .build();
            messages.add(userMessage);
            
            // 构建请求 - 参考Sample.java
            ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .stream(false)
                    .logprobs(false)
                    .build();
            
            // 发送请求
            log.debug("发送火山引擎API请求: {}", chatRequest);
            
            // 获取响应并简化处理方式
            String responseContent = "";
            try {
                Object contentObj = arkService.createChatCompletion(chatRequest)
                        .getChoices()
                        .get(0)
                        .getMessage()
                        .getContent();
                
                // 确保类型转换安全
                responseContent = contentObj != null ? contentObj.toString() : "";
                log.debug("收到火山引擎API响应: {}", responseContent);
            } catch (Exception e) {
                log.error("处理火山引擎API响应时出错: {}", e.getMessage(), e);
                throw new DeepSeekApiException("处理响应失败", e);
            }
            
            // 构建DeepSeekResponse
            return convertToDeepSeekResponse(responseContent);
        } catch (Exception e) {
            log.error("火山引擎API调用失败: {}", e.getMessage(), e);
            
            // 如果配置了使用模拟数据，则在API调用失败时返回模拟数据
            if (useMockOnFailure) {
                log.info("使用模拟数据作为替代响应");
                return getMockResponse(request);
            }
            
            throw new DeepSeekApiException("服务调用失败，请稍后再试", e);
        }
    }
    
    /**
     * 将火山引擎响应内容转换为DeepSeekResponse格式
     */
    private DeepSeekResponse convertToDeepSeekResponse(String content) {
        DeepSeekResponse response = new DeepSeekResponse();
        
        response.setId("volcengine-" + System.currentTimeMillis());
        response.setModel(model);
        response.setObject("chat.completion");
        response.setCreated(System.currentTimeMillis() / 1000);
        
        List<DeepSeekResponse.Choice> choices = new ArrayList<>();
        DeepSeekResponse.Choice choice = new DeepSeekResponse.Choice();
        choice.setIndex(0);
        choice.setFinish_reason("stop");
        
        DeepSeekResponse.Message message = new DeepSeekResponse.Message();
        message.setRole("assistant");
        message.setContent(content);
        
        choice.setMessage(message);
        choices.add(choice);
        
        response.setChoices(choices);
        
        DeepSeekResponse.Usage usage = new DeepSeekResponse.Usage();
        usage.setPrompt_tokens(100); // 模拟值
        usage.setCompletion_tokens(100); // 模拟值
        usage.setTotal_tokens(200); // 模拟值
        response.setUsage(usage);
        
        return response;
    }
    
    /**
     * 获取模拟响应，用于API调用失败时提供替代数据
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
        
        // 根据时辰生成模拟数据
        String mockContent = "{\n" +
                "  \"gregorianDate\": \"" + request.getDate() + "\",\n" +
                "  \"lunarDate\": \"十一月十五\",\n" +
                "  \"eightCharacters\": \"己巳-戊子-壬午-" + getTimeStem(request.getTime()) + "\",\n" +
                "  \"fiveElements\": \"土火-土水-水火-" + getTimeElement(request.getTime()) + "\",\n" +
                "  \"missingElements\": \"金木\",\n" +
                "  \"zodiac\": \"" + getZodiacByYear(request.getDate()) + "\"\n" +
                "}";
        
        message.setContent(mockContent);
        
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
        
        log.info("生成模拟响应: {}", mockContent);
        
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
    
    /**
     * 格式化提示
     * 
     * @param request 计算请求
     * @return 格式化后的提示
     */
    private String formatPrompt(CalculationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("请根据以下信息分析生辰信息：\n");
        sb.append("公历日期：").append(request.getDate()).append("\n");
        
        if (StringUtils.hasText(request.getTime())) {
            sb.append("出生时辰：").append(request.getTime());
        } else {
            sb.append("出生时辰：未知");
        }
        
        return sb.toString();
    }
}