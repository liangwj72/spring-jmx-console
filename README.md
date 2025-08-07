# Spring JMX Console

jconsole的web版

- 这是一个springboot 的组件项目，本项目不能独立使用，
- 本组件用于为其他web项目提供一个web界面可查看mbean
- 本组件监听在一个独立的端口，和原始项目不冲突
- 组件的package名: `com.liangwj.spring.jmxConsole`

## 流程
- 本项目的 META-INF/spring/imports 定义了启动的类
- 主项目通过 `@EnableAutoConfiguration` 启动组件

## 配置例子
主项目的 `application.yml` 有以下配置时，启动本组件

```yaml
spring-jmx-console:
    port: 60080 # 监听的端口
    context-path: ${JMX_CONSOLE_CONTEXT_PATH:}  # 默认上下文路径

```
对应的class是: `com.liangwj.spring.jmxConsole.JmxConsoleProperties`

## 测试

`demo.Application` 是一个演示用的Web项目，通过其他这个项目就可以测试本组件