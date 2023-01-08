package com.example.exswaggerrestdocs.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private LocalDateTime responseTime;
    private String errorMessage;
    private T data;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return ApiResponse.<T>builder()
                .responseTime(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static ApiResponse<?> createError(String errorMessage) {
        return ApiResponse.builder()
                .responseTime(LocalDateTime.now())
                .errorMessage(errorMessage)
                .build();
    }

    public static <T> ApiResponse<T> createValidationError(T data) {
        return ApiResponse.<T>builder()
                .responseTime(LocalDateTime.now())
                .errorMessage("요청 데이터 검증 오류 발생")
                .data(data)
                .build();
    }
}
