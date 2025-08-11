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

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Forever defaultTester; // 默认测试器实例

	private String testerName;

	public TesterRunner(ApplicationArguments args) {
		if (args.getSourceArgs().length > 0) {
			// 如果有参数，取第一个参数作为测试器名称
			this.testerName = args.getSourceArgs()[0].toLowerCase();
			log.info("根据运行时的参数获得测试器名：{}", this.testerName);
		}
	}

	public void runTests() {
		// 这里可以添加测试逻辑
		BaseTester tester = getTester();

		log.info("运行测试器：{}", tester.getClass().getSimpleName());
		try {
			boolean isQuit = tester.run();

			if (isQuit) {
				log.info("测试器运行完成，退出程序");
				System.exit(0); // 测试器运行完成后退出程序
			} else {
				log.info("测试器运行完成，但不退出程序");
			}

		} catch (Exception e) {
			log.error("测试器运行失败：{}", e.getMessage(), e);
			System.exit(1); // 运行失败时退出程序
		}
	}

	/**
	 * 获取测试器实例, 从 ApplicationContext 中查找所有基于 BaseTester 的 Bean，然后根据查找是否和 testerName
	 * 匹配的，如果找不到就用默认的 Forever 测试器。
	 * 
	 * @return
	 */
	public BaseTester getTester() {

		// 获取所有的测试器，并构造一个 Map，以测试器类名为键，测试器实例为值
		var allTester = applicationContext.getBeansOfType(BaseTester.class).values();
		Map<String, BaseTester> testerMap = new HashMap<>();
		for (BaseTester tester : allTester) {
			// 忽略大小写
			testerMap.put(tester.getClass().getSimpleName().toLowerCase(), tester);
		}

		if (this.testerName != null && testerMap.containsKey(testerName)) {
			// 如果 testerMap 中包含指定的测试器名称，则返回对应的测试器实例
			return testerMap.get(testerName);
		} else {
			log.debug("没有找到指定的测试器：{}，使用默认的测试器：{}", testerName, this.defaultTester.getClass().getSimpleName());
			return this.defaultTester;
		}
	}
}
