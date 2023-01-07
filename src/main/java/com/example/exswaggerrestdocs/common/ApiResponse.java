package com.example.exswaggerrestdocs.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private LocalDateTime responseTime;
    private T data;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return ApiResponse.<T>builder()
                .responseTime(LocalDateTime.now())
                .data(data)
                .build();
    }
}
