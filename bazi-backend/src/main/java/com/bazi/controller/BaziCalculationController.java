package com.bazi.controller;

import com.bazi.model.request.CalculationRequest;
import com.bazi.model.response.ApiResponse;
import com.bazi.model.response.BaziCalculationResult;
import com.bazi.model.response.DeepSeekResponse;
import com.bazi.service.DeepSeekService;
import com.bazi.util.DeepSeekApiException;
import com.bazi.util.DeepSeekResponseParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 生辰数据计算控制器
 * 
 * @author bazi-team
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/bazi")
@Tag(name = "生辰数据分析接口", description = "提供生辰信息计算相关接口")
public class BaziCalculationController {
    
    private final DeepSeekService deepSeekService;
    private final DeepSeekResponseParser responseParser;
    
    @Autowired
    public BaziCalculationController(DeepSeekService deepSeekService, DeepSeekResponseParser responseParser) {
        this.deepSeekService = deepSeekService;
        this.responseParser = responseParser;
    }
    
    /**
     * 处理预检请求
     * 
     * @return 空响应
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptionsRequest() {
        log.debug("收到OPTIONS预检请求");
        return ResponseEntity.ok().build();
    }
    
    /**
     * 生辰数据计算接口
     * 
     * @param request 计算请求参数
     * @return 生辰数据计算结果
     */
    @PostMapping("/calculate")
    @Operation(summary = "生辰分析", description = "根据阳历日期和时辰计算生辰信息")
    public ApiResponse<BaziCalculationResult> calculateBazi(
            @Parameter(description = "计算请求参数") @Valid @RequestBody CalculationRequest request) {
        
        log.info("收到生辰分析请求: {}", request);
        
        // 调用DeepSeek API进行计算
        DeepSeekResponse apiResponse = deepSeekService.requestCalculation(request);
        
        // 解析响应结果
        BaziCalculationResult result = responseParser.parseResult(apiResponse);
        
        log.info("生辰分析完成: {}", result);
        
        return ApiResponse.success(result);
    }
} 