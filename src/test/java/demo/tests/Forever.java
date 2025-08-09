package demo.tests;

import org.springframework.stereotype.Component;

@Component
public class Forever extends BaseTester {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Forever.class);

	@Override
	public void run() {
		// 永久运行，不做任何操作
		log.info("Forever 测试模式: 应用会持续运行下去，不会自动停止。");
	}

}
