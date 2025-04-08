package com.bazi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 八字计算结果响应模型
 * 
 * @author bazi-team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaziCalculationResult {
    
    /**
     * 阳历日期
     */
    private String gregorianDate;
    
    /**
     * 农历日期
     */
    private String lunarDate;
    
    /**
     * 八字（年月日时四柱天干地支）
     */
    private String eightCharacters;
    
    /**
     * 五行属性
     */
    private String fiveElements;
    
    /**
     * 五行缺失
     */
    private String missingElements;
    
    /**
     * 生肖
     */
    private String zodiac;
} 