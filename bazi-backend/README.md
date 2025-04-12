# 生辰分析小程序后端服务

本项目是生辰分析小程序的后端服务，基于Spring Boot开发，提供八字排盘、五行分析等REST API接口。

## 技术栈
- Java 8+
- Spring Boot 2.7.x
- RESTful API设计
- AI大模型集成（支持DeepSeek/火山引擎）

## 多环境配置说明

本项目采用Spring Boot的多环境配置管理方式，支持不同开发环境的配置隔离，无需使用.env文件。

### 配置文件结构

```
src/main/resources/
├── application.yml            # 主配置文件（共享配置）
├── application-dev.yml        # 开发环境配置
├── application-prod.yml       # 生产环境配置
├── application-local.yml      # 本地开发配置（不提交到Git）
└── application-local.template.yml  # 本地配置模板
```

### 使用方法

1. **本地开发配置**

   首次设置项目时，复制本地配置模板：
   
   ```bash
   cp src/main/resources/application-local.template.yml src/main/resources/application-local.yml
   ```

   然后编辑`application-local.yml`，填入你自己的API密钥和其他配置。此文件不会被提交到Git。

2. **指定使用的环境**

   有以下几种方式指定使用哪个环境的配置：

   * **方法1：命令行参数**
   
     ```bash
     # 使用本地配置
     ./mvnw spring-boot:run -Dspring.profiles.active=local
     
     # 使用开发环境配置
     ./mvnw spring-boot:run -Dspring.profiles.active=dev
     
     # 使用生产环境配置
     ./mvnw spring-boot:run -Dspring.profiles.active=prod
     ```

   * **方法2：环境变量**
   
     ```bash
     # Linux/Mac
     export SPRING_PROFILES_ACTIVE=local
     ./mvnw spring-boot:run
     
     # Windows
     set SPRING_PROFILES_ACTIVE=local
     ./mvnw spring-boot:run
     ```

   * **方法3：IDE配置**
   
     在IDE (如IntelliJ IDEA) 中，可以在运行配置中设置环境变量`spring.profiles.active=local`

### 配置优先级

Spring Boot的配置优先级从高到低：

1. 命令行参数
2. 环境变量
3. 特定profile的配置文件(如`application-local.yml`)
4. 主配置文件(`application.yml`)

## 开发和启动

1. **安装JDK和Maven**

   确保已安装JDK 8+和Maven

2. **配置本地开发环境**

   按照上述步骤创建并配置`application-local.yml`

3. **启动服务**

   ```bash
   # 使用本地配置
   ./mvnw spring-boot:run -Dspring.profiles.active=local
   ```

4. **验证服务**

   访问 http://localhost:8080/api/swagger-ui.html 查看API文档

## API接口说明

### 八字计算接口

**请求**：
```
POST /api/bazi/calculate
```

**参数**：
```json
{
  "year": 2000,
  "month": 1,
  "day": 1,
  "hour": 0,
  "gender": "男"
}
```

**响应**：
```json
{
  "gregorianDate": "2000年1月1日",
  "lunarDate": "己卯年十一月廿四",
  "eightCharacters": "己卯-丙子-壬午-壬子",
  "fiveElements": "土木-火水-水火-水水",
  "missingElements": "缺金土",
  "zodiac": "兔",
  "analysis": "..."
}
```

## 环境迁移

从.env文件迁移到Spring Boot多环境配置的注意事项：

1. 不再需要`.env`文件，相关配置已移到`application-{profile}.yml`文件中
2. 敏感信息放在`application-local.yml`中，此文件不提交到Git
3. 通过JVM参数或环境变量`SPRING_PROFILES_ACTIVE`指定使用的配置文件 