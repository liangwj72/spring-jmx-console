package com.liangwj.spring.jmxConsole.dto.schemas;

import java.util.concurrent.atomic.AtomicLong;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 流量计数器
 * 
 */
@Schema(description = " 流量计数器")
public class PayloadCounter {

	private final AtomicLong count = new AtomicLong(); // 次数
	private final AtomicLong payloadTotal = new AtomicLong(); // 总带宽
	private final AtomicLong timeTotal = new AtomicLong(); // 使用时间

	private long payloadMin = 0;
	private long payloadMax = 0;
	private long timeMin = 0;
	private long timeMax = 0;

	/** 增加使用量 */
	public void addPayload(long payload, long useTime) {
		this.count.incrementAndGet(); // 增加次数
		this.payloadTotal.addAndGet(payload); // 增加宽度总使用量

		if (this.payloadMax < payload) {
			this.payloadMax = payload;
		}
		if (this.payloadMin == 0 || this.payloadMin > payload) {
			this.payloadMin = payload;
		}

		if (useTime > 0) {
			this.timeTotal.addAndGet(useTime); // 增加平均总用时
			if (this.timeMax < useTime) {
				this.timeMax = useTime;
			}
			if (this.timeMin == 0 || this.timeMin > useTime) {
				this.timeMin = useTime;
			}
		}
	}

	@Schema(description = "平均带宽")
	public long getPayloadAvg() {
		final long count = this.getCount();
		if (count > 0) {
			return this.getPayloadTotal() / count;
		} else {
			return 0;
		}
	}

	@Schema(description = "平均用时")
	public long getTimeAvg() {
		final long count = this.getCount();
		if (count > 0) {
			return this.getTimeTotal() / count;
		} else {
			return 0;
		}
	}

	@Schema(description = "传输的次数")
	public long getCount() {
		return count.get();
	}

	@Schema(description = "使用的带宽")
	public long getPayloadTotal() {
		return payloadTotal.get();
	}

	@Schema(description = "最大流量")
	public long getPayloadMax() {
		return payloadMax;
	}

	@Schema(description = "最小流量")
	public long getPayloadMin() {
		return payloadMin;
	}

	@Schema(description = "最长耗时")
	public long getTimeMax() {
		return timeMax;
	}

	@Schema(description = "最短耗时")
	public long getTimeMin() {
		return timeMin;
	}

	@Schema(description = "传输耗时")
	public long getTimeTotal() {
		return timeTotal.get();
	}

	/** 重置 */
	public void reset() {
		this.count.set(0);
		this.payloadTotal.set(0);
		this.timeTotal.set(0);
		this.payloadMin = 0;
		this.payloadMax = 0;
		this.timeMin = 0;
		this.timeMax = 0;
	}

}
