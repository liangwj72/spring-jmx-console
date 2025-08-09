package demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import demo.tests.BaseTester;
import demo.tests.Forever;

@Component
public class TesterRunner {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TesterRunner.class);

	private static final String DEFAUTER_TESTER = Forever.class.getSimpleName(); // 默认测试器：持续运行参数

	@Autowired
	private ApplicationContext applicationContext;

	private String testerName;

	public TesterRunner(ApplicationArguments args) {
		if (args.getSourceArgs().length > 0) {
			// 如果有参数，取第一个参数作为测试器名称
			log.info("根据运行时的参数获得测试器名：{}", args.getSourceArgs()[0]);
			this.testerName = args.getSourceArgs()[0];
		} else {
			log.info("运行时，没有参数指定测试器名，使用默认测试器：{}", DEFAUTER_TESTER);
			// 否则使用默认测试器
			this.testerName = DEFAUTER_TESTER;
		}
	}

	public void runTests() {
		// 这里可以添加测试逻辑
		BaseTester tester = getTester();

		log.info("运行测试器：{}", tester.getClass().getSimpleName());
		tester.run();
	}

	/**
	 * 获取测试器实例, 从 ApplicationContext 中查找所有基于 BaseTester 的 Bean，然后根据查找是否和 testerName
	 * 匹配的，如果找不到就用默认的 Forever 测试器。
	 * 
	 * @return
	 */
	public BaseTester getTester() {

		var allTester = applicationContext.getBeansOfType(BaseTester.class).values();
		Map<String, BaseTester> testerMap = new HashMap<>();
		for (BaseTester tester : allTester) {
			testerMap.put(tester.getClass().getSimpleName(), tester);
		}
		if (testerMap.containsKey(testerName)) {
			// 如果 testerMap 中包含指定的测试器名称，则返回对应的测试器实例
			return testerMap.get(testerName);
		} else {
			// 如果没有找到指定的测试器名称，则返回默认的测试器实例
			log.debug("没有找到指定的测试器：{}，使用默认的测试器：{}", testerName, DEFAUTER_TESTER);
			return testerMap.get(DEFAUTER_TESTER);
		}
	}
}
