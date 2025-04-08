package com.bazi.service;

import com.bazi.model.response.DeepSeekResponse;
import com.bazi.model.request.CalculationRequest;
import com.bazi.service.impl.DeepSeekServiceImpl;
import com.bazi.util.DeepSeekApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DeepSeek服务测试
 * 
 * @author bazi-team
 */
@ExtendWith(MockitoExtension.class)
public class DeepSeekServiceTest {
    
    @Mock
    private OkHttpClient httpClient;
    
    @Mock
    private Call call;
    
    @Mock
    private Response response;
    
    @Mock
    private ResponseBody responseBody;
    
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @InjectMocks
    private DeepSeekServiceImpl deepSeekService;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(deepSeekService, "apiUrl", "https://api.deepseek.com/chat/completions");
        ReflectionTestUtils.setField(deepSeekService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(deepSeekService, "model", "deepseek-chat");
        ReflectionTestUtils.setField(deepSeekService, "prompt", "你是一个专业的八字命理分析助手");
    }
    
    @Test
    void requestCalculation_Success() throws IOException {
        // 准备测试请求
        CalculationRequest request = new CalculationRequest();
        request.setDate("2023-05-15");
        request.setTime("子时");
        
        // 准备模拟响应
        String mockResponseJson = "{\"id\":\"123\",\"object\":\"chat.completion\",\"created\":1620000000,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"{\\\"gregorianDate\\\":\\\"2023-05-15\\\",\\\"lunarDate\\\":\\\"三月廿六\\\",\\\"eightCharacters\\\":\\\"癸卯-乙巳-丁酉-壬子\\\",\\\"fiveElements\\\":\\\"水木-木火-火金-水水\\\",\\\"missingElements\\\":\\\"土\\\",\\\"zodiac\\\":\\\"兔\\\"}\"}}]}";
        
        // 模拟HTTP调用
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn(mockResponseJson);
        when(response.isSuccessful()).thenReturn(true);
        
        // 执行被测方法
        DeepSeekResponse result = deepSeekService.requestCalculation(request);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("deepseek-chat", result.getModel());
        assertEquals(1, result.getChoices().size());
        assertTrue(result.getChoices().get(0).getMessage().getContent().contains("gregorianDate"));
        
        // 验证交互
        verify(httpClient).newCall(any(Request.class));
        verify(call).execute();
        verify(response).isSuccessful();
        verify(responseBody).string();
    }
    
    @Test
    void requestCalculation_ApiError() throws IOException {
        // 准备测试请求
        CalculationRequest request = new CalculationRequest();
        request.setDate("2023-05-15");
        request.setTime("子时");
        
        // 模拟HTTP调用失败
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);
        when(response.isSuccessful()).thenReturn(false);
        when(response.code()).thenReturn(401);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn("{\"error\":\"Invalid API key\"}");
        
        // 执行被测方法并验证异常
        DeepSeekApiException exception = assertThrows(DeepSeekApiException.class, () -> {
            deepSeekService.requestCalculation(request);
        });
        
        // 验证异常内容
        assertTrue(exception.getMessage().contains("API认证失败"));
        
        // 验证交互
        verify(httpClient).newCall(any(Request.class));
        verify(call).execute();
        verify(response).isSuccessful();
        verify(response, times(1)).code();
    }
    
    @Test
    void requestCalculation_NetworkError() throws IOException {
        // 准备测试请求
        CalculationRequest request = new CalculationRequest();
        request.setDate("2023-05-15");
        request.setTime("子时");
        
        // 模拟网络异常
        when(httpClient.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenThrow(new IOException("Network error"));
        
        // 执行被测方法并验证异常
        DeepSeekApiException exception = assertThrows(DeepSeekApiException.class, () -> {
            deepSeekService.requestCalculation(request);
        });
        
        // 验证异常内容
        assertTrue(exception.getMessage().contains("服务调用失败"));
        
        // 验证交互
        verify(httpClient).newCall(any(Request.class));
        verify(call).execute();
    }
} 