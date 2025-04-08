package com.bazi.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应格式
 * 
 * @author bazi-team
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API通用响应格式")
public class ApiResponse<T> {

    @Schema(description = "状态码", example = "200")
    private Integer code;

    @Schema(description = "状态描述", example = "success")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    /**
     * 创建成功响应
     * 
     * @param <T> 数据类型
     * @param data 响应数据
     * @return 成功响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    /**
     * 创建失败响应
     * 
     * @param <T> 数据类型
     * @param code 错误码
     * @param message 错误信息
     * @return 失败响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
} 