### working-AI README

这是一个业余项目，用于测试AI大模型对接，框架使用了springboot3 、vaadin24、spring-data-jpa、mybatis-plus3.5.12、langchain4j、spring-ai等

## 如何开始
To start the application in development mode, import it into your IDE and run the `Application` class. 
You can also start the application from the command line by running: 

```bash
./mvnw
```

## 构建项目
To build the application in production mode, run:

```bash
./mvnw -Pproduction package
```

## Getting Started

### 项目总体情况
1. 项目语言：jdk21
2. 框架：springboot3 、vaadin24、spring-data-jpa、mybatis-plus3.5.12
3. langchain4j实现AI大模型对接
### 项目结构：
1. com.sl.chat.ui:存放页面
2. com.sl.config:存放配置文件
3. com.sl.entity:存放实体类
4. com.sl.mapper:存放mapper接口
5. com.sl.service:存放业务逻辑
6. com.sl.template.ui:存放模板页面
7. com.sl.chat.tool.spring:是结合spring ai 实现的工具类
8. com.sl.chat.agent.spring:是结合spring ai 创建的代理类

## 访问： 
localhost:8080，自动跳转到登录页面









