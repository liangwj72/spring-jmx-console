package com.liangwj.spring.jmxConsole.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liangwj.spring.jmxConsole.dto.requests.JwChangeAttrForm;
import com.liangwj.spring.jmxConsole.dto.requests.JwInvokeOptForm;
import com.liangwj.spring.jmxConsole.dto.requests.JwObjectNameForm;
import com.liangwj.spring.jmxConsole.dto.responses.JwInvokeOptResponse;
import com.liangwj.spring.jmxConsole.dto.responses.JwMBeanInfoResponse;
import com.liangwj.spring.jmxConsole.dto.responses.JwMBeanListResponse;
import com.liangwj.spring.jmxConsole.dto.schemas.MBeanVo;
import com.liangwj.spring.jmxConsole.exceptions.BaseApiException;
import com.liangwj.spring.jmxConsole.services.MBeanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mbean")
@Tag(name = "运行状态", description = "获取操作系统、JMV的运行状态")
public class MBeanController {

	
	@Autowired
	private MBeanService service;

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "获取所有的MBean")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = JwMBeanListResponse.class))), })
	public ResponseEntity<JwMBeanListResponse> getMBeanList() throws BaseApiException {
		final JwMBeanListResponse res = new JwMBeanListResponse();

		res.setList(this.service.getMBeanList());
		
		return ResponseEntity.ok(res);
    }
	

	@PostMapping(value = "/changeAttr", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "改变一个属性")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功") })
	public ResponseEntity<Void> changeAttr(@Valid @RequestBody JwChangeAttrForm form) throws BaseApiException {

		this.service.changeAttr(form);
		
		return ResponseEntity.ok().build();
    }

	@PostMapping(value = "/invokeOpt", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "调用一个方法")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = JwInvokeOptResponse.class))), })
	public ResponseEntity<JwInvokeOptResponse> invokeOpt(@Valid @RequestBody JwInvokeOptForm form) throws BaseApiException {
		final JwInvokeOptResponse res = this.service.invokeOpt(form.getObjectName(), form.getOptName(), form.getParamInfo());;

		// TODO 调用一个方法
		
		return ResponseEntity.ok(res);
    }


	@PostMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "获取一个MBean的详情")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = JwMBeanInfoResponse.class))), })
	public ResponseEntity<JwMBeanInfoResponse> getMBeanInfo(@Valid @RequestBody JwObjectNameForm form) throws BaseApiException {
		MBeanVo vo = this.service.getMBeanInfo(form.getObjectName());

		final JwMBeanInfoResponse res = new JwMBeanInfoResponse();
		res.setInfo(vo);

		
		return ResponseEntity.ok(res);
    }
}
