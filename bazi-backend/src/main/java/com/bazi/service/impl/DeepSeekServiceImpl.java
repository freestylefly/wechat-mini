package com.bazi.service.impl;

import com.bazi.model.request.CalculationRequest;
import com.bazi.model.request.DeepSeekMessage;
import com.bazi.model.request.DeepSeekRequest;
import com.bazi.model.response.DeepSeekResponse;
import com.bazi.service.DeepSeekService;
import com.bazi.util.DeepSeekApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek API服务实现类
 * 
 * @author bazi-team
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "deepseek", matchIfMissing = true)
public class DeepSeekServiceImpl implements DeepSeekService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Value("${deepseek.api.url}")
    private String apiUrl;
    
    @Value("${deepseek.api.key}")
    private String apiKey;
    
    @Value("${deepseek.api.timeout:30}")
    private int timeout;
    
    @Value("${deepseek.api.model:deepseek-chat}")
    private String model;
    
    @Value("${deepseek.api.prompt}")
    private String prompt;
    
    @Value("${ai.use-mock-on-failure:false}")
    private boolean useMockOnFailure;

    /**
     * 构造函数
     * 
     * @param objectMapper JSON处理器
     */
    public DeepSeekServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .followRedirects(true);
        
        // 在开发环境下，可以配置信任所有证书，生产环境中应移除此配置
        try {
            X509TrustManager trustManager = createTrustAllCertManager();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                   .hostnameVerifier((hostname, session) -> true);
            
            log.debug("配置了信任所有证书的TLS连接");
        } catch (Exception e) {
            log.warn("无法配置自定义TLS设置，将使用系统默认配置", e);
        }
        
        this.httpClient = builder.build();
    }
    
    /**
     * 创建信任所有证书的TrustManager
     */
    private X509TrustManager createTrustAllCertManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // 信任所有客户端证书
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // 信任所有服务器证书
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
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
        DeepSeekRequest deepSeekRequest = buildRequest(content);
        
        log.info("正在请求DeepSeek API进行八字计算，请求URL: {}, 请求内容: {}", apiUrl, content);
        try {
            return sendRequest(deepSeekRequest);
        } catch (IOException e) {
            log.error("DeepSeek API调用失败: {}", e.getMessage(), e);
            
            // 如果配置了使用模拟数据，在API调用失败时返回模拟数据
            if (useMockOnFailure) {
                log.info("使用模拟数据作为替代响应");
                return getMockResponse(request);
            }
            
            throw new DeepSeekApiException("服务调用失败，请稍后再试", e);
        }
    }

    /**
     * 发送请求到DeepSeek API
     * 
     * @param request DeepSeek请求
     * @return DeepSeek响应
     * @throws IOException IO异常
     * @throws DeepSeekApiException API调用异常
     */
    private DeepSeekResponse sendRequest(DeepSeekRequest request) throws IOException, DeepSeekApiException {
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("构建请求JSON失败", e);
            throw new DeepSeekApiException("系统内部错误：请求数据构建失败", e);
        }
        
        log.debug("DeepSeek API请求体: {}", requestBody);
        
        Request httpRequest = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .build();
        
        log.debug("DeepSeek API请求URL: {}, Headers: {}", apiUrl, httpRequest.headers());
        
        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            
            if (!response.isSuccessful()) {
                log.error("DeepSeek API返回错误: [{}] {}", response.code(), responseBody);
                
                if (response.code() == 401) {
                    throw new DeepSeekApiException("API认证失败，请检查API密钥是否正确");
                } else if (response.code() == 429) {
                    throw new DeepSeekApiException("API请求次数超限，请稍后再试");
                } else {
                    throw new DeepSeekApiException("服务调用失败 [" + response.code() + "]，请稍后再试");
                }
            }
            
            log.debug("DeepSeek API响应体: {}", responseBody);
            
            try {
                return objectMapper.readValue(responseBody, DeepSeekResponse.class);
            } catch (JsonProcessingException e) {
                log.error("解析响应数据失败", e);
                throw new DeepSeekApiException("系统内部错误：响应数据解析失败", e);
            }
        } catch (SSLHandshakeException e) {
            log.error("DeepSeek API SSL握手失败", e);
            throw new DeepSeekApiException("网络连接安全验证失败，请联系管理员", e);
        } catch (SSLException e) {
            log.error("DeepSeek API SSL错误", e);
            throw new DeepSeekApiException("网络连接安全问题，请联系管理员", e);
        } catch (SocketTimeoutException e) {
            log.error("DeepSeek API请求超时", e);
            throw new DeepSeekApiException("服务响应超时，请稍后再试", e);
        } catch (UnknownHostException e) {
            log.error("DeepSeek API域名解析失败: {}", apiUrl, e);
            throw new DeepSeekApiException("网络连接错误，无法连接到服务", e);
        }
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
        
        DeepSeekResponse.Message message = new DeepSeekResponse.Message();
        message.setRole("assistant");
        
        // 根据时辰生成模拟数据
        String mockContent = "{\n" +
                "  \"gregorianDate\": \"" + request.getDate() + "\",\n" +
                "  \"lunarDate\": \"十一月十五\",\n" +
                "  \"eightCharacters\": \"己巳-戊子-壬午-" + getTimeStem(request.getTime()) + "\",\n" +
                "  \"fiveElements\": \"土火-土水-水火-" + getTimeElement(request.getTime()) + "\",\n" +
                "  \"missingElements\": \"金木\",\n" +
                "  \"zodiac\": \"蛇\"\n" +
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
        
        return response;
    }
    
    /**
     * 根据时辰获取对应的天干地支
     * 
     * @param time 时辰
     * @return 天干地支
     */
    private String getTimeStem(String time) {
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
     * 构建DeepSeek请求
     * 
     * @param content 请求内容
     * @return DeepSeek请求
     */
    private DeepSeekRequest buildRequest(String content) {
        DeepSeekMessage systemMessage = new DeepSeekMessage("system", prompt);
        DeepSeekMessage userMessage = new DeepSeekMessage("user", content);
        
        return DeepSeekRequest.builder()
                .model(model)
                .messages(Arrays.asList(systemMessage, userMessage))
                .temperature(0.7)
                .max_tokens(2000)
                .stream(false)
                .build();
    }

    /**
     * 格式化提示
     * 
     * @param request 计算请求
     * @return 格式化后的提示
     */
    private String formatPrompt(CalculationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("请根据以下信息计算八字命盘：\n");
        sb.append("公历日期：").append(request.getDate()).append("\n");
        
        if (StringUtils.hasText(request.getTime())) {
            sb.append("出生时辰：").append(request.getTime());
        } else {
            sb.append("出生时辰：未知");
        }
        
        return sb.toString();
    }
} 