package com.bazi.service;

import com.bazi.model.response.DeepSeekResponse;
import com.bazi.model.request.CalculationRequest;
import com.bazi.util.DeepSeekApiException;

/**
 * DeepSeek API服务接口
 * 
 * @author bazi-team
 */
public interface DeepSeekService {
    
    /**
     * 请求DeepSeek API计算八字
     * 
     * @param request 计算请求参数
     * @return DeepSeek API响应
     * @throws DeepSeekApiException 如果API调用失败
     */
    DeepSeekResponse requestCalculation(CalculationRequest request) throws DeepSeekApiException;
} 