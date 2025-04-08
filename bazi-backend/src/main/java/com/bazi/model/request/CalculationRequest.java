package com.bazi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 八字计算请求数据模型
 * 
 * @author bazi-team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "八字计算请求")
public class CalculationRequest {

    @NotBlank(message = "日期不能为空")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式不正确，应为YYYY-MM-DD")
    @Schema(description = "阳历日期，格式：YYYY-MM-DD", example = "2025-03-22", required = true)
    private String date;

    @NotBlank(message = "时辰不能为空")
    @Schema(description = "时辰，中文表示", example = "子时", required = true)
    private String time;
} 