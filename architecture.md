# 八字命理小助手 - 架构设计文档

## 1. 架构概述

### 1.1 系统架构图

```
┌────────────────────┐          ┌─────────────────────┐          ┌───────────────────┐
│                    │          │                     │          │                   │
│  前端 (uni-app)    │◄─────────►  后端 (Java)        │◄─────────►  DeepSeek API     │
│  微信小程序        │   HTTP   │  RESTful API        │   HTTP   │                   │
│                    │          │                     │          │                   │
└────────────────────┘          └─────────────────────┘          └───────────────────┘
```

### 1.2 技术栈选型

#### 前端技术栈
- 开发框架：uni-app
- 编程语言：JavaScript/TypeScript
- UI框架：uView
- 状态管理：Vuex
- 网络请求：uni.request
- 开发工具：HBuilderX

#### 后端技术栈
- 开发框架：Spring Boot 2.7.x
- 编程语言：Java 11
- API风格：RESTful
- 网络请求：OkHttp
- JSON处理：Jackson
- API文档：Swagger/OpenAPI
- 开发工具：IntelliJ IDEA

### 1.3 整体架构说明

八字命理小助手采用前后端分离的架构设计：
- 前端使用uni-app框架开发微信小程序，保持与原型设计一致的UI界面
- 后端使用Java/Spring Boot提供RESTful API，负责处理前端请求并调用DeepSeek API
- 核心计算逻辑通过调用DeepSeek API实现，后端负责数据转换和格式化
- 系统遵循微信小程序开发规范和阿里巴巴Java开发规范

## 2. 前端架构设计

### 2.1 项目结构

```
uni-app-project/
├── pages/                  # 页面文件夹
│   ├── index/              # 首页
│   ├── input/              # 信息输入页
│   └── result/             # 结果展示页
├── static/                 # 静态资源
│   ├── images/             # 图片资源
│   └── styles/             # 样式文件
├── components/             # 公共组件
│   ├── date-picker/        # 日期选择器
│   └── time-picker/        # 时辰选择器
├── store/                  # Vuex状态管理
│   └── index.js            # 状态管理入口
├── utils/                  # 工具函数
│   ├── request.js          # 网络请求封装
│   └── formatter.js        # 数据格式化工具
├── config/                 # 配置文件
│   └── api.js              # API接口配置
├── manifest.json           # 应用配置文件
├── pages.json              # 页面配置
└── App.vue                 # 应用入口
```

### 2.2 页面组件设计

按照prototype.html分为三个主要页面：

#### 2.2.1 首页 (index)
- 背景：水墨画风格背景图
- 主体内容：
  - 标题："阳历-农历"
  - 副标题："天干、地支，一键计算。"
  - 按钮："开始计算"
- 交互：点击"开始计算"跳转到信息输入页

#### 2.2.2 信息输入页 (input)
- 背景：与首页保持一致的水墨画风格
- 主体内容：
  - 输入表单：
    - 阳历日期选择器
    - 时辰选择器
  - 按钮："计算"
- 交互：
  - 点击输入框弹出对应的选择器
  - 点击"计算"按钮调用后端API并跳转到结果页

#### 2.2.3 结果展示页 (result)
- 背景：与前两页保持一致的水墨画风格
- 主体内容：
  - 标题："计算结果"
  - 结果内容区：
    - 输入日期
    - 农历日期
    - 天干地支
    - 所属五行
    - 五行缺啥
    - 所属生肖
  - 备注信息
  - 按钮："再来一次"
- 交互：点击"再来一次"返回到信息输入页

### 2.3 数据流管理

使用Vuex进行状态管理：

```javascript
// store结构设计
{
  state: {
    // 用户输入的数据
    userInput: {
      date: '',  // 阳历日期
      time: ''   // 时辰
    },
    // 计算结果
    result: {
      gregorianDate: '',  // 输入日期
      lunarDate: '',      // 农历日期
      eightCharacters: '', // 天干地支
      fiveElements: '',   // 所属五行
      missingElements: '', // 五行缺失
      zodiac: ''          // 生肖
    }
  },
  mutations: {
    // 更新用户输入
    updateUserInput(state, payload) { ... },
    // 更新计算结果
    updateResult(state, payload) { ... },
    // 重置数据
    resetData(state) { ... }
  },
  actions: {
    // 调用API计算结果
    calculateResult({ commit, state }) { ... }
  }
}
```

### 2.4 UI风格与组件

按照原型设计实现中国传统水墨风格的UI界面：

- 色彩：主色调为青绿色(#3CB371)，辅以传统中国风元素
- 字体：使用系统默认字体，关键信息加粗显示
- 按钮：统一使用圆角绿色按钮，确保各页面位置一致
- 背景：使用统一的水墨画背景图，保持整体风格一致
- 选择器：自定义日期和时辰选择器，保持与整体风格一致

## 3. 后端架构设计

### 3.1 项目结构

```
java-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bazi/
│   │   │           ├── controller/       # 控制器层
│   │   │           │   └── CalculationController.java
│   │   │           ├── service/          # 服务层
│   │   │           │   ├── CalculationService.java
│   │   │           │   └── DeepSeekService.java
│   │   │           ├── model/            # 数据模型
│   │   │           │   ├── request/      # 请求模型
│   │   │           │   │   └── CalculationRequest.java
│   │   │           │   └── response/     # 响应模型
│   │   │           │       └── CalculationResponse.java
│   │   │           ├── config/           # 配置类
│   │   │           │   ├── RestConfig.java
│   │   │           │   └── SwaggerConfig.java
│   │   │           ├── util/             # 工具类
│   │   │           │   └── DeepSeekUtil.java
│   │   │           └── BaziApplication.java  # 应用入口
│   │   └── resources/
│   │       ├── application.yml           # 应用配置
│   │       └── application-prod.yml      # 生产环境配置
│   └── test/                            # 单元测试
├── pom.xml                              # Maven配置
└── README.md                            # 项目说明
```

### 3.2 API设计

遵循RESTful风格设计API，主要提供一个计算接口：

#### 3.2.1 八字计算接口

- **接口URI**：`/api/v1/calculation`
- **HTTP方法**：POST
- **请求参数**：

```json
{
  "date": "2025-03-22",   // 阳历日期，格式：YYYY-MM-DD
  "time": "子时"          // 时辰，中文表示
}
```

- **响应数据**：

```json
{
  "code": 200,            // 状态码：200成功，非200失败
  "message": "success",   // 状态描述
  "data": {
    "gregorianDate": "2025-03-22",              // 输入日期
    "lunarDate": "二月廿三",                    // 农历日期
    "eightCharacters": "乙巳-己卯-庚寅-辛巳",    // 天干地支
    "fiveElements": "木火-土木-金木-金火",       // 所属五行
    "missingElements": "水",                    // 五行缺失
    "zodiac": "蛇"                             // 生肖
  }
}
```

### 3.3 服务层设计

#### 3.3.1 CalculationService

负责处理计算业务逻辑，是控制器和DeepSeek API的中间层：

- 接收控制器传来的请求参数
- 调用DeepSeekService请求计算结果
- 处理和转换DeepSeek返回的数据
- 返回格式化的结果给控制器

```java
@Service
public class CalculationService {
    
    private final DeepSeekService deepSeekService;
    
    @Autowired
    public CalculationService(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }
    
    public CalculationResponse calculate(CalculationRequest request) {
        // 1. 调用DeepSeek API
        DeepSeekResponse deepSeekResponse = deepSeekService.requestCalculation(request);
        
        // 2. 转换DeepSeek结果为标准响应格式
        CalculationResponse response = new CalculationResponse();
        // 设置各字段...
        
        return response;
    }
}
```

#### 3.3.2 DeepSeekService

负责与DeepSeek API的交互：

- 构造请求参数
- 发送HTTP请求到DeepSeek API
- 接收和解析DeepSeek API的响应
- 返回结果给CalculationService

```java
@Service
public class DeepSeekService {
    
    @Value("${deepseek.api.url}")
    private String apiUrl;
    
    @Value("${deepseek.api.key}")
    private String apiKey;
    
    private final OkHttpClient httpClient;
    
    @Autowired
    public DeepSeekService(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    public DeepSeekResponse requestCalculation(CalculationRequest request) {
        // 构造请求体
        String requestBody = constructRequestBody(request);
        
        // 发送请求并接收响应
        String responseBody = sendRequest(requestBody);
        
        // 解析响应
        return parseResponse(responseBody);
    }
    
    private String constructRequestBody(CalculationRequest request) {
        // 构造DeepSeek API请求体
        // ...
    }
    
    private String sendRequest(String requestBody) {
        // 使用OkHttpClient发送请求
        // ...
    }
    
    private DeepSeekResponse parseResponse(String responseBody) {
        // 解析DeepSeek API响应
        // ...
    }
}
```

### 3.4 错误处理与日志

采用全局异常处理机制，确保API返回标准化的错误信息：

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("系统异常：", e);
        
        ErrorResponse errorResponse = new ErrorResponse(500, "系统错误，请稍后重试");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(DeepSeekApiException.class)
    public ResponseEntity<ErrorResponse> handleDeepSeekApiException(DeepSeekApiException e) {
        logger.error("DeepSeek API异常：", e);
        
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }
    
    // 其他异常处理...
}
```

## 4. DeepSeek API调用设计

### 4.1 API调用流程

```
┌─────────────────┐      ┌────────────────────┐      ┌───────────────────┐
│                 │      │                    │      │                   │
│  前端请求参数   │─────►│ 格式化为DeepSeek   │─────►│ 发送HTTP请求到    │
│                 │      │ 可识别的提示词     │      │ DeepSeek API      │
│                 │      │                    │      │                   │
└─────────────────┘      └────────────────────┘      └─────────┬─────────┘
                                                               │
┌─────────────────┐      ┌────────────────────┐      ┌─────────▼─────────┐
│                 │      │                    │      │                   │
│  返回标准化     │◄─────┤ 解析和提取关键信息 │◄─────┤ 接收DeepSeek      │
│  计算结果       │      │                    │      │ API响应           │
│                 │      │                    │      │                   │
└─────────────────┘      └────────────────────┘      └───────────────────┘
```

### 4.2 提示词构造

为了从DeepSeek API获取准确的结果，需要构造明确的提示词：

```
请根据以下阳历日期和时辰，计算：
1. 对应的农历日期
2. 天干地支（年柱、月柱、日柱、时柱）
3. 五行属性
4. 五行缺失
5. 生肖

阳历日期：{date}
时辰：{time}

请以JSON格式回复，包含以下字段：
gregorianDate: 输入的阳历日期
lunarDate: 农历日期（如"二月廿三"）
eightCharacters: 天干地支（格式如"乙巳-己卯-庚寅-辛巳"）
fiveElements: 所属五行（格式如"木火-土木-金木-金火"）
missingElements: 五行缺失（如"水"）
zodiac: 生肖（如"蛇"）
```

### 4.3 响应解析

接收DeepSeek API返回的结果，提取关键信息并转换为标准格式：

```java
private CalculationResponse parseDeepSeekResponse(String deepSeekResponse) {
    try {
        // 解析DeepSeek返回的JSON
        JsonNode rootNode = objectMapper.readTree(deepSeekResponse);
        JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");
        
        // 提取计算结果JSON
        JsonNode resultNode = objectMapper.readTree(contentNode.asText());
        
        // 构造响应对象
        CalculationResponse response = new CalculationResponse();
        response.setGregorianDate(resultNode.path("gregorianDate").asText());
        response.setLunarDate(resultNode.path("lunarDate").asText());
        response.setEightCharacters(resultNode.path("eightCharacters").asText());
        response.setFiveElements(resultNode.path("fiveElements").asText());
        response.setMissingElements(resultNode.path("missingElements").asText());
        response.setZodiac(resultNode.path("zodiac").asText());
        
        return response;
    } catch (Exception e) {
        throw new DeepSeekApiException("解析DeepSeek响应失败", e);
    }
}
```

## 5. 开发规范

### 5.1 前端开发规范

- 遵循微信小程序开发规范
- 使用ES6+标准语法
- 采用Vue.js单文件组件(.vue)开发模式
- 组件命名采用PascalCase（首字母大写驼峰式）
- 页面文件夹和文件命名采用kebab-case（短横线连接式）
- CSS采用BEM命名规范
- 注释规范：
  - 文件顶部添加文件说明
  - 复杂逻辑需添加详细注释
  - API调用添加参数和返回说明

### 5.2 后端开发规范

- 遵循阿里巴巴Java开发手册规范
- 采用RESTful API设计风格
- 命名规范：
  - 类名：PascalCase（首字母大写驼峰式）
  - 方法名、变量名：camelCase（首字母小写驼峰式）
  - 常量名：SNAKE_CASE（全大写下划线连接式）
- API路径采用kebab-case（短横线连接式）
- 包名全小写，多级包名使用.分隔
- 注释规范：
  - 类、方法添加JavaDoc注释
  - 复杂业务逻辑添加详细注释
  - 所有公开API方法添加参数和返回值说明

### 5.3 API接口规范

- URI使用名词而非动词，表示资源
- 使用HTTP方法表示操作类型（GET、POST、PUT、DELETE）
- 响应状态码符合HTTP标准
- 响应数据统一格式：
  ```json
  {
    "code": 200,           // 状态码
    "message": "success",  // 状态描述
    "data": {}             // 业务数据
  }
  ```
- 错误响应统一格式：
  ```json
  {
    "code": 400,                     // 错误码
    "message": "参数错误",           // 错误消息
    "details": "日期格式不正确"      // 错误详情（可选）
  }
  ```

## 6. 安全设计

### 6.1 API安全

- 使用HTTPS协议
- 实现API签名验证
- 添加请求频率限制，防止恶意调用
- 敏感配置（如DeepSeek API密钥）使用环境变量或配置中心

### 6.2 数据安全

- 敏感信息（如用户出生日期）不明文存储
- 遵循最小权限原则，每个服务只能访问其所需的资源
- 实现完善的日志记录，便于问题追踪和审计

## 7. 部署架构

### 7.1 部署环境

- 前端：微信小程序平台
- 后端：阿里云ECS或容器服务
- 监控：使用阿里云监控服务

### 7.2 部署流程

```
┌─────────────┐      ┌────────────────┐      ┌──────────────┐
│             │      │                │      │              │
│  代码提交   │─────►│  CI/CD流水线   │─────►│  自动化测试  │
│             │      │                │      │              │
└─────────────┘      └────────────────┘      └──────┬───────┘
                                                    │
┌─────────────┐      ┌────────────────┐      ┌──────▼───────┐
│             │      │                │      │              │
│  上线发布   │◄─────┤  人工审核      │◄─────┤  打包构建    │
│             │      │                │      │              │
└─────────────┘      └────────────────┘      └──────────────┘
```

### 7.3 扩展性设计

- 后端服务可水平扩展
- 使用负载均衡分发请求
- 关键配置可通过配置中心动态调整，无需重启
- 预留功能扩展接口，便于后续功能迭代

## 8. 未来迭代规划

### 8.1 第一阶段（MVP）

- 实现基础八字计算功能
- 完成三个主要页面的开发
- 对接DeepSeek API实现核心计算逻辑

### 8.2 第二阶段

- 添加用户登录功能
- 实现历史记录保存
- 增加结果分享功能
- 优化用户体验

### 8.3 第三阶段

- 增加高级解读功能（付费）
- 实现多人信息管理
- 添加命理知识科普内容
- 实现数据统计和分析功能

## 9. 附录

### 9.1 技术选型依据

- **为什么选择uni-app**：跨平台能力强，代码复用率高，后期可快速拓展到其他平台
- **为什么选择Spring Boot**：成熟稳定，开发效率高，社区支持丰富，符合企业级应用需求
- **为什么使用DeepSeek API**：强大的自然语言处理能力，可以准确理解和计算传统命理学内容

### 9.2 参考资料

- 微信小程序开发文档
- uni-app开发文档
- 阿里巴巴Java开发手册
- Spring Boot官方文档
- RESTful API设计指南
- DeepSeek API文档 