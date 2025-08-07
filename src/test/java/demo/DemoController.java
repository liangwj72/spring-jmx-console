package demo;

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
@Tag(name = "演示api", description = "演示api")
public class DemoController {

    @GetMapping("/")
    @Operation(summary = "演示api", description = "演示api")
    @ApiResponse(responseCode = "200", description = "演示api")
    public ResponseEntity<DemoResponse> health() {
      return ResponseEntity.ok(new DemoResponse("演示api"));
    }
} 