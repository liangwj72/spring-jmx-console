package com.liangwj.spring.jmxConsole.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 健康检查控制器
 * 提供应用程序健康状态检查接口
 */
@RestController
@Tag(name = "健康检查", description = "应用程序健康状态检查接口")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查应用程序是否正常运行")
    @ApiResponse(responseCode = "200", description = "应用程序运行正常")
    public ResponseEntity<Void> health() {
		return ResponseEntity.ok().build();
    }
} 