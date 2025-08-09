package demo.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 错误响应对象
 */
@Schema(description = "演示响应")
public class DemoResponse {
	@Schema(description = "信息", example = "演示信息")
	private final String detail;
    
	public DemoResponse(String detail) {
		this.detail = detail;
    }

	public String getDetail() {
		return detail;
	}
}