package com.bazi.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 八字计算响应数据模型
 * 
 * @author bazi-team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "八字计算结果")
public class CalculationResponse {

    @Schema(description = "输入的阳历日期", example = "2025-03-22")
    private String gregorianDate;

    @Schema(description = "农历日期", example = "二月廿三")
    private String lunarDate;

    @Schema(description = "天干地支（八字）", example = "乙巳-己卯-庚寅-辛巳")
    private String eightCharacters;

    @Schema(description = "五行属性", example = "木火-土木-金木-金火")
    private String fiveElements;

    @Schema(description = "五行缺失", example = "水")
    private String missingElements;

    @Schema(description = "生肖", example = "蛇")
    private String zodiac;
} 