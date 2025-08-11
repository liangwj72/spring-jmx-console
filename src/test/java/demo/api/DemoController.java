package demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 健康检查控制器 提供应用程序健康状态检查接口
 */
@RestController
@Tag(name = "演示api", description = "演示api")
public class DemoController {

	public static final String DEMO_API = "/api/demo";

	@GetMapping(DEMO_API)
	@Operation(summary = "演示api", description = "演示api")
	@ApiResponse(responseCode = "200", description = "演示api")
	public ResponseEntity<DemoResponse> demo() {
		return ResponseEntity.ok(new DemoResponse("/api/demo"));
	}

}