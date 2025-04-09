# 生辰分析小程序

![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)
![Platform](https://img.shields.io/badge/platform-WeChat-green.svg)
![AI-Powered](https://img.shields.io/badge/AI--Powered-Yes-orange.svg)

基于微信小程序平台的个人生辰数据分析应用。通过简单输入出生日期和时辰，获取详细的农历日期、生肖、天干地支和五行分析，结合AI技术提供个性化命理解读。

> 🔥 **特别说明**：本项目是一个面向AI编程学习的开源教程项目，完整展示了如何利用AI技术从零到一快速构建一个实用微信小程序。从需求分析、系统设计、前后端开发到最终部署上线，提供全流程、全沉浸式的实战教学体验。无论你是AI编程新手还是有经验的开发者，都能从中学习如何高效利用AI工具进行现代应用开发。

## 📋 功能特点

- 🔄 阳历日期自动转换为农历日期
- 🧮 八字排盘与天干地支计算
- 🔍 五行属性与缺失分析
- 🐯 生肖查询与解读
- 🤖 基于AI的综合命理分析
- 📱 微信小程序无需安装，扫码即用

## 🏗️ 系统架构

- **前端**：微信小程序（原生开发）
- **后端**：Java Spring Boot RESTful API
- **AI集成**：支持DeepSeek或火山引擎AI服务

## 🛠️ 技术栈

- 微信小程序开发框架
- Java Spring Boot 2.7+
- Maven 构建工具
- RESTful API 设计
- AI 大模型集成
- Nginx 反向代理

## 🚀 快速开始

### 前端部署

1. 克隆本仓库
   ```bash
   git clone https://github.com/yourusername/bazi-mini-program.git
   ```

2. 使用微信开发者工具打开 `bazi-frontend` 目录

3. 配置小程序信息
   - 修改 `project.config.json` 中的 appid:
     ```json
     {
       "appid": "YOUR_WECHAT_APPID"
     }
     ```
   - 在微信公众平台注册并获取 appid: [微信公众平台](https://mp.weixin.qq.com/)

4. 配置API地址（`bazi-frontend/config/api.js`）：
   ```javascript
   // 开发环境
   [ENV.DEV]: 'http://localhost:8080/api',
   // 生产环境（替换为你的域名）
   [ENV.PROD]: 'https://api.your-domain.com/api',
   ```

5. 编译预览小程序

### 后端部署

1. 配置API密钥（需要自行申请）
   - [DeepSeek API](https://www.deepseek.com)申请地址
   - [火山引擎API](https://www.volcengine.com)申请地址
   - 详细申请步骤请参考[API密钥申请指南](api-key-guide.md)

2. 修改配置文件 `bazi-backend/src/main/resources/application.yml`
   ```yaml
   # DeepSeek API配置
   deepseek:
     api:
       url: ${DEEPSEEK_API_URL:https://api.deepseek.com/v1/chat/completions}
       key: ${DEEPSEEK_API_KEY:YOUR_DEEPSEEK_API_KEY}

   # 火山引擎API配置
   volcengine:
     api:
       key: ${VOLCENGINE_API_KEY:YOUR_VOLCENGINE_API_KEY}
   ```

3. 使用Maven打包
   ```bash
   cd bazi-backend
   mvn clean package
   ```

4. 运行Spring Boot应用
   ```bash
   java -jar target/bazi-backend-0.1.0.jar
   ```

## 📝 API使用说明

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

## 📦 项目结构

```
bazi-mini-program/
├── bazi-frontend/       # 微信小程序前端代码
├── bazi-backend/        # Java Spring Boot后端代码
├── nginx-config.conf    # Nginx配置示例
└── docs/                # 文档和部署指南
```

## 📋 配置文档

详细配置文档请参阅：
- [API密钥申请指南](api-key-guide.md)
- [服务器配置指南](server-config-guide.md)
- [HTTPS配置指南](https-setup-guide.md)
- [子域名配置指南](subdomain-setup-guide.md)

## 📄 许可证

本项目采用 MIT 许可证 - 详情见 [LICENSE](LICENSE) 文件。

## 🤝 贡献指南

欢迎提交 Issues 和 Pull Requests 帮助改进项目！

1. Fork 本仓库
2. 创建你的功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交你的改动 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建一个 Pull Request

## 🙏 致谢

- 感谢所有开源项目和工具的贡献者
- 感谢微信小程序平台提供的技术支持

## 🧠 AI编程教学

![AI开发流程](https://img.shields.io/badge/AI%E5%BC%80%E5%8F%91%E6%B5%81%E7%A8%8B-%E5%85%A8%E6%B5%81%E7%A8%8B%E5%B1%95%E7%A4%BA-blue)

本项目不仅是一个功能完整的微信小程序，更是一个AI辅助开发的示范案例，旨在帮助开发者了解和掌握：

- 🚀 如何利用AI快速构建从想法到产品的全流程
- 💡 AI大模型在实际项目中的应用场景和集成方法
- 🔄 借助AI工具实现需求分析、架构设计和代码实现
- 🛠️ AI辅助调试和性能优化的最佳实践
- 🔧 AI生成代码的质量控制和人工审查流程
- 📊 全栈应用开发中AI的优势和局限性

通过学习本项目，你将体验到AI如何显著提升开发效率，减少重复工作，并帮助开发者专注于创意和用户体验。无论你是想学习微信小程序开发，还是探索AI在软件工程中的应用，这个项目都能提供宝贵的参考价值。

### ⏱️ 惊人的开发速度

在传统开发方式下需要数周完成的项目，借助AI辅助开发仅用了**10天**就完成了从构思到上线的全过程！

| 开发阶段 | 传统方式 | AI辅助方式 |
|---------|---------|------------|
| 需求分析 | 5-7天   | 1天        |
| 系统设计 | 3-5天   | 1天        |
| 后端开发 | 7-14天  | 2天        |
| 前端开发 | 7-14天  | 2天        |
| 系统集成 | 3-5天   | 1天        |
| 部署上线 | 3-5天   | 2天        |
| 文档编写 | 3-7天   | 1天        |

### 📚 完整的学习资源

> 🎓 查看[开发日志](docs/dev-journey.md)了解本项目从构思到完成的全过程，以及AI在每个开发阶段的具体应用。
>
> 💻 所有代码都包含详细注释，说明AI生成代码的思路和逻辑。
>
> 📖 项目包含多种技术栈的实践案例：微信小程序、Java Spring Boot、AI模型集成。 