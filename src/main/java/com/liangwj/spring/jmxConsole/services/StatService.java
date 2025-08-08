package com.liangwj.spring.jmxConsole.services;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Service;

import com.liangwj.spring.jmxConsole.dto.schemas.RuntimeInfoBean;
import com.liangwj.spring.jmxConsole.dto.schemas.UrlBean;
import com.liangwj.spring.jmxConsole.dto.schemas.UrlStatInfoBean;
import com.liangwj.spring.jmxConsole.utils.ExecutorServiceUtils;
import com.liangwj.spring.jmxConsole.utils.MBeanUtils;
import com.liangwj.spring.jmxConsole.utils.OsUtil;
import com.liangwj.spring.jmxConsole.utils.QueueLinkedList;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * 统计服务
 * 
 */
@Service
public class StatService {
	public class SnapshotBean {

		@Schema(description = "启动时间")
		private final long uptime = MBeanUtils.getVmInfo().getUptime();

		private final long processCpuTime = MBeanUtils.getOsInfo().getProcessCpuTime();

		@Schema(description = "api执行次数")
		private long apiCount;

		public SnapshotBean(long apiCount) {
			super();
			this.apiCount = apiCount;
		}

		public void reset() {
			this.apiCount = 0;
		}

	}

	/**
	 * 记录每个时间范围的URL访问数据
	 *
		 *
	 */
	public class StatInfo {
		private final int id;
		private final AtomicLong counter = new AtomicLong(); // 次数
		private final long timeRangeMax;// 时间范围 最大值
		private final long timeRangeMin;// 时间范围-最小值

		private final QueueLinkedList<UrlBean> urlLog = new QueueLinkedList<>(LOG_MAX_SIZE);
		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		public StatInfo(int id, long timeRangeMin, long timeRangeMax) {
			this.id = id;
			this.timeRangeMin = timeRangeMin;
			this.timeRangeMax = timeRangeMax;

			log.debug("初始化时间范围统计数据，id: {}, timeRangeMin: {}, timeRangeMax: {}", id, timeRangeMin, timeRangeMax);
		}

		/** 生成给api接口用的数据 */
		public UrlStatInfoBean toInfoBean() {
			final UrlStatInfoBean bean = new UrlStatInfoBean(this.id, this.timeRangeMin, this.timeRangeMax,
					this.counter.get());

			this.lock.readLock().lock();
			try {
				bean.getUrlHistory().addAll(this.urlLog);

				Collections.reverse(bean.getUrlHistory()); // 反序，最新记录放前面
			} finally {
				this.lock.readLock().unlock();
			}

			return bean;
		}

		/** 增加一条url记录 */
		void add(String url, long ms) {
			// 计时器 +1
			counter.incrementAndGet();

			this.lock.writeLock().lock();
			try {
				this.urlLog.add(new UrlBean(url, ms));
			} finally {
				this.lock.writeLock().unlock();
			}

		}

		/** 重置所有 */
		void reset() {
			// 计数器清零
			this.counter.set(0);

			// 清除历史
			this.lock.writeLock().lock();
			try {
				this.urlLog.clear();
			} finally {
				this.lock.writeLock().unlock();
			}
			log.debug("重置统计数据，id: {}, timeRangeMin: {}, timeRangeMax: {}", this.id, this.timeRangeMin,
					this.timeRangeMax);
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("StatInfo{");
			sb.append("id=").append(id).append(", ");
			sb.append("counter=").append(counter.get()).append(", ");
			sb.append("urlLog=").append(urlLog.size()).append("}");
			return sb.toString();
		}

	}

	/** 记录的历史长度 */
	public static final int HISTROY_LEN = 60;

	/** 时间间隔(秒） */
	public static final int TIME_INTERVAL = 10;

	private static final int LOG_MAX_SIZE = 200; // 日志最大记录条数

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StatService.class);

	/** 运行时的历史信息 */
	private final QueueLinkedList<RuntimeInfoBean> history = new QueueLinkedList<>(HISTROY_LEN);

	/** api调用的计数器 */
	private final AtomicLong apiCounter = new AtomicLong();

	/** 定时获取运行状态的调度器 */
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	/** 上一个时间点的运行状态（累计值） */
	private SnapshotBean lastSnapShot = new SnapshotBean(0);

	/** 处理器数量 */
	private final int availableProcessors = MBeanUtils.getOsInfo().getAvailableProcessors();

	private final StatInfo[] stats; // 保存所有时间范围数据

	/** 时间分段 */
	private final int[] timeRanges = { 20, // 第一个不能为0，编程方便
			100, 300, 1000 };

	public StatService() {
		// 将所有的数据范围数据都初始化好
		this.stats = new StatInfo[this.timeRanges.length + 1];
		int min = 0;
		int index = 0;
		for (final int max : timeRanges) {
			this.stats[index] = new StatInfo(index, min, max);
			min = max;
			index++;
		}
		// 补上>1000的
		this.stats[this.stats.length - 1] = new StatInfo(index, min, -1);
		log.debug("初始化所有时间范围，合计 {} 个范围", index);
	}

	/** 返回所有的时间范围统计数据 */
	public List<UrlStatInfoBean> getAllUrlStat() {
		final List<UrlStatInfoBean> list = new LinkedList<>();
		for (final StatInfo info : this.stats) {
			// 列表时，不给url详情
			list.add(info.toInfoBean());
		}
		return list;
	}

	/** 获取历史运行状态 */
	public LinkedList<RuntimeInfoBean> getRuntimeHistory() {
		synchronized (this.history) {
			final LinkedList<RuntimeInfoBean> list = new LinkedList<>(this.history); // 先添加历史
			return list;
		}
	}

	/**
	 * 当api完成时，添加统计数据
	 * 
	 * @param url      api的url，不要问号后面的参数
	 * @param costTime api耗时
	 * 
	 */
	public void onApiFinish(String url, long costTime) {
		this.apiCounter.incrementAndGet(); // 增加api调用次数

		// 根据api耗时，找到要记录url的地方
		final int index = this.findIndex(costTime);
		final StatInfo stat = this.stats[index];

		stat.add(url, costTime); // 增加url耗时记录

		log.debug("增加统计 {}, 时间:{}ms, index:{} ", url, costTime, stat);

	}

	/** 重置api计数器 */
	public void resetApiCounter() {
		this.apiCounter.set(0);

		// 要重置上一个时间点的数据
		this.lastSnapShot.reset();
	}

	/** 重置url统计数据 */
	public void resetUrlStat() {
		// 重置所有的统计数据
		for (final StatInfo stat : this.stats) {
			stat.reset();
		}
	}

	/** 获得当前时间和最后状态的差异值 */
	private RuntimeInfoBean calcDiff(SnapshotBean curSnapshot) {
		final RuntimeInfoBean point = new RuntimeInfoBean();

		// 设置固定值
		point.setThreadCount(ManagementFactory.getThreadMXBean().getThreadCount()); // 线程数
		point.setMemory(OsUtil.getMemoryInfo()); // 内存信息

		// 各项差值
		point.setActionCount(curSnapshot.apiCount - this.lastSnapShot.apiCount);

		final long elapsedCpu = curSnapshot.processCpuTime - this.lastSnapShot.processCpuTime; // 获取cpu消耗的差值
		final long elaspedTime = curSnapshot.uptime - this.lastSnapShot.uptime; // 获取时间的差值
		if (elaspedTime > 0) {
			// 计算CPU负载
			final double cpuUsage = Math.min(99d, elapsedCpu / (elaspedTime * 10000d * this.availableProcessors));
			point.setProcessCpuLoad(cpuUsage);
		}

		return point;

	}

	/** 计算当前时间点的运行状态 */
	protected void addCurTimeHistoryPoint() {

		final long now = System.currentTimeMillis();
		final long timeInterval = TIME_INTERVAL * 1000;
		final long recordTime = now / timeInterval * timeInterval; // 时间取整

		// 先记录当前状态快照
		final SnapshotBean curSnapshot = new SnapshotBean(this.apiCounter.get());

		// 计算当前数值和上一个时间点的差值
		final RuntimeInfoBean point = this.calcDiff(curSnapshot);
		point.setRecordTime(recordTime); // 修改修改一下时间

		// 先更新最后状态
		this.lastSnapShot = curSnapshot;

		// 加入到历史
		synchronized (this.history) {
			this.history.add(point);
		}
	}

	/** 根据毫秒时间，获取时间段的键值 */
	protected int findIndex(long ms) {
		int index = 0;
		for (; index < timeRanges.length; index++) {
			if (ms < this.timeRanges[index]) {
				// 小于范围的最大值，就是在这个范围内了，直接返回索引编号
				return index;
			}
		}

		// 如果都不匹配，就是在所有时间范围以外了，放最后一个
		return this.stats.length - 1;

	}

	@PostConstruct
	protected void init() {
		final long now = System.currentTimeMillis();

		final long intervalMs = TimeUnit.SECONDS.toMillis(TIME_INTERVAL);
		final long initialDelay = intervalMs - now % intervalMs; // 延时的时间（毫秒）

		// 启动时就增加一个点
		this.addCurTimeHistoryPoint();

		this.scheduledExecutorService.scheduleAtFixedRate(() -> {
			// 定时执行添加时间点的任务
			this.addCurTimeHistoryPoint();
		}, initialDelay, intervalMs, TimeUnit.MILLISECONDS);
	}

	@PreDestroy
	protected void shutdown() {
		ExecutorServiceUtils.shutdownExecutorService("运行状态定时任务", this.scheduledExecutorService);
	}
}
