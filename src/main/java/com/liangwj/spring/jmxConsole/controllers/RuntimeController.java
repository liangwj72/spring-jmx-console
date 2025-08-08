package com.liangwj.spring.jmxConsole.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liangwj.spring.jmxConsole.dto.responses.OsInfoResponse;
import com.liangwj.spring.jmxConsole.dto.responses.RuntimeHistoryResponse;
import com.liangwj.spring.jmxConsole.services.StatService;
import com.liangwj.spring.jmxConsole.utils.MBeanUtils;
import com.liangwj.spring.jmxConsole.utils.OsUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/runtime")
@Tag(name = "运行状态", description = "获取操作系统、JMV的运行状态")
public class RuntimeController {

	@Autowired
	private StatService statService;

	@GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "获取操作系统，JVM的线图数据")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = RuntimeHistoryResponse.class))), })
	public ResponseEntity<RuntimeHistoryResponse> history() {
		final RuntimeHistoryResponse res = new RuntimeHistoryResponse();

		res.setDiskInfo(OsUtil.getDiskInfo()); // 获取实时硬盘信息
		res.setUriStat(this.statService.getAllUrlStat()); // 获取所有URL的统计信息
		res.setRuntimes(this.statService.getRuntimeHistory()); // 获取运行状态历史数据

		return ResponseEntity.ok(res);
    }

	@GetMapping(value = "/os_info", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "获取操作系统和虚拟机信息")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = OsInfoResponse.class))), })
	public ResponseEntity<OsInfoResponse> osInfo() {
		final OsInfoResponse res = new OsInfoResponse();
		res.setOs(MBeanUtils.getOsInfo()); // 获取操作系统信息
		res.setClassLoading(MBeanUtils.getClassLoadingInfo()); // 获取类加载信息
		res.setThreading(MBeanUtils.getThreadingInfo()); // 获取线程信息
		res.setVm(MBeanUtils.getVmInfo()); // 获取虚拟机信息

		return ResponseEntity.ok(res);
    }
}
