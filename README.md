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
- Python 3.8+ (AI开发环境)
- Maven 构建工具
- RESTful API 设计
- AI 大模型集成
- Nginx 反向代理

## 🚀 快速开始

### AI开发环境配置

1. 安装Python环境
   ```bash
   # 安装Python 3.8或更高版本
   python --version  # 确认Python版本
   
   # 创建并激活虚拟环境
   python -m venv venv
   source venv/bin/activate  # Linux/Mac
   # 或
   .\venv\Scripts\activate  # Windows
   
   # 安装必要的AI开发包
   pip install cursor-runtime
   pip install openai
   pip install transformers
   ```

2. 配置开发工具
   - 安装 [Visual Studio Code](https://code.visualstudio.com/)
   - 安装 [Cursor](https://cursor.sh/) - AI辅助编程工具
   - 配置相关插件和扩展

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

4. 配置开发环境
   - **方法一**: 直接修改 `bazi-frontend/config/api.js` 中的配置:
     ```javascript
     // 开发环境
     [ENV.DEV]: 'http://localhost:8080/api',
     // 生产环境（替换为你的域名）
     [ENV.PROD]: 'https://api.your-domain.com/api',
     ```
   - **方法二**: 使用小程序内置的开发配置功能
     1. 在开发模式下运行小程序
     2. 首页点击"⚙️ 开发配置"按钮
     3. 在配置页面中设置API地址和环境

5. 编译预览小程序

### 后端部署

1. 配置API密钥（需要自行申请）
   - [DeepSeek API](https://www.deepseek.com)申请地址
   - [火山引擎API](https://www.volcengine.com)申请地址
   - 详细申请步骤请参考[API密钥申请指南](api-key-guide.md)

2. 配置开发环境
   - **方法一**: 使用本地环境配置（推荐开发使用）
     ```bash
     # 复制本地配置模板
     cp bazi-backend/src/main/resources/application-local.template.yml bazi-backend/src/main/resources/application-local.yml
     
     # 编辑application-local.yml文件，填入你的API密钥
     ```
   - **方法二**: 通过环境变量或JVM参数配置
     ```bash
     # Linux/Mac
     export DEEPSEEK_API_KEY=your_api_key
     export VOLCENGINE_API_KEY=your_api_key
     
     # Windows
     set DEEPSEEK_API_KEY=your_api_key
     set VOLCENGINE_API_KEY=your_api_key
     ```

3. 使用Maven打包
   ```bash
   cd bazi-backend
   mvn clean package
   ```

4. 运行Spring Boot应用
   ```bash
   # 使用本地配置启动
   java -Dspring.profiles.active=local -jar target/bazi-backend-0.1.0.jar
   
   # 或使用开发环境配置
   java -Dspring.profiles.active=dev -jar target/bazi-backend-0.1.0.jar
   
   # 生产环境部署
   java -Dspring.profiles.active=prod -jar target/bazi-backend-0.1.0.jar
   ```

> 🔒 注意: 本地配置文件(application-local.yml)已添加到.gitignore，不会被提交到代码仓库

## 🔐 本地开发环境配置

为了解决本地开发时敏感信息（如API密钥、域名等）的管理问题，本项目提供了一套安全的本地配置方案。

### 前端本地配置

小程序前端使用`.env`文件和构建脚本来管理环境配置：

1. **首次配置步骤**
   - 进入前端目录：`cd bazi-frontend`
   - 复制模板文件：`cp .env.template .env`
   - 编辑`.env`文件，设置您的配置项：
     ```
     # 环境设置 (development 或 production)
     CURRENT_ENV=development  # 改为production使用生产环境
     
     # API地址配置
     DEV_API_URL=http://localhost:8080/api
     PROD_API_URL=https://your-actual-domain.com/api
     ```
   - 安装依赖：`npm install`，如果国内下载太慢，可以使用：`npm install --registry=https://registry.npmmirror.com`
   - 应用配置：`npm run config`

2. **切换环境**
   - 编辑`bazi-frontend/.env`文件（注意位置）
   - 修改`CURRENT_ENV`的值：
     - `CURRENT_ENV=development` 使用本地开发环境
     - `CURRENT_ENV=production` 使用生产环境
   - 重新应用配置：`cd bazi-frontend && npm run config`

3. **注意事项**
   - `.env`文件必须放在`bazi-frontend/`目录下，不是在`config/`子目录
   - 每次修改`.env`后必须运行`npm run config`使配置生效
   - 配置脚本会将设置应用到`config/api.js`文件中

### 后端本地配置

后端使用环境变量和`.env`文件管理敏感配置：

1. **创建本地配置文件**
   - 复制`bazi-backend/.env.template`为`.env`（已加入.gitignore）
   - 填写实际的API密钥和其他敏感信息
   ```
   # DeepSeek API配置
   DEEPSEEK_API_KEY=your_actual_key_here
   
   # 火山引擎API配置
   VOLCENGINE_API_KEY=your_actual_key_here
   ```

2. **自动加载机制**
   - 应用启动时会自动检测并加载`.env`文件
   - Spring Boot使用环境变量占位符读取这些值
   - 详见`bazi-backend/src/main/java/com/bazi/config/AppConfig.java`

### 环境配置文件结构

```
wechat-mini/
├── bazi-frontend/
│   ├── .env              <- 前端环境配置文件放这里
│   ├── .env.template     <- 前端配置模板
│   ├── config/
│   │   ├── api.js        <- API配置（由脚本自动更新）
│   │   └── scripts/
│   │       └── env-config.js  <- 环境配置脚本
│   └── package.json      <- 包含npm run config命令
├── bazi-backend/
│   ├── .env              <- 后端环境配置文件放这里
│   └── .env.template     <- 后端配置模板
```

### 安全最佳实践

1. **提交代码前检查**
   - 确保`.env`文件已添加到`.gitignore`
   - 不要提交包含实际API密钥的配置文件
   
2. **定期轮换密钥**
   - 定期更换API密钥增强安全性
   - 发现泄露立即更换

> 📝 更多详情，请参阅[本地开发环境配置指南](local-dev-guide.md)

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

## 🚢 后端部署指南

### JAR包部署方式

#### 1. 准备环境

- 安装JDK 11或更高版本
  ```bash
  # 检查Java版本
  java -version
  
  # 如果没有安装，请安装OpenJDK或Oracle JDK
  # Ubuntu系统：
  sudo apt update
  sudo apt install openjdk-11-jdk
  
  # CentOS/RHEL系统：
  sudo yum install java-11-openjdk-devel
  
  # macOS系统：
  brew install openjdk@11
  ```

#### 2. 打包应用

```bash
# 进入后端项目目录
cd bazi-backend

# 使用Maven打包
mvn clean package -DskipTests
```

#### 3. 配置环境变量

```bash
# 创建一个.env文件
cat > .env << EOL
# 服务器配置
SERVER_PORT=6803

# AI服务配置
AI_PROVIDER=volcengine  # 可选：volcengine, deepseek

# DeepSeek API配置
DEEPSEEK_API_URL=https://api.deepseek.com/v1/chat/completions
DEEPSEEK_API_KEY=your_deepseek_api_key_here

# 火山引擎API配置
VOLCENGINE_API_KEY=your_volcengine_api_key_here
VOLCENGINE_MODEL=doubao-lite-128k-240828

# 跨域设置
CORS_ALLOWED_ORIGINS=*
EOL
```

#### 4. 启动应用

```bash
# 使用生产环境配置启动
java -Dspring.profiles.active=prod -jar target/bazi-backend-0.1.0.jar

# 或者从.env文件加载环境变量
export $(grep -v '^#' .env | xargs)
java -Dspring.profiles.active=prod -jar target/bazi-backend-0.1.0.jar
```

#### 5. 使用systemd管理服务（Linux系统）

```bash
# 创建systemd服务文件
sudo cat > /etc/systemd/system/bazi-backend.service << EOL
[Unit]
Description=Bazi Backend Service
After=network.target

[Service]
User=youruser
WorkingDirectory=/path/to/bazi-backend
ExecStart=/usr/bin/java -Dspring.profiles.active=prod -jar /path/to/bazi-backend/target/bazi-backend-0.1.0.jar
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
EnvironmentFile=/path/to/bazi-backend/.env

[Install]
WantedBy=multi-user.target
EOL

# 启动服务
sudo systemctl daemon-reload
sudo systemctl start bazi-backend
sudo systemctl enable bazi-backend

# 查看服务状态
sudo systemctl status bazi-backend

# 查看日志
sudo journalctl -u bazi-backend -f
```

### Docker部署方式

#### 1. 创建Dockerfile

在后端项目根目录创建`Dockerfile`文件：

```bash
cat > Dockerfile << EOL
FROM openjdk:11-jre-slim

WORKDIR /app

# 复制项目JAR包
COPY target/bazi-backend-0.1.0.jar /app/bazi-backend.jar

# 设置环境变量（可选，也可以通过docker-compose或启动命令设置）
ENV SPRING_PROFILES_ACTIVE=prod

# 暴露端口
EXPOSE 6803

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/bazi-backend.jar"]
EOL
```

#### 2. 创建docker-compose.yml

```bash
cat > docker-compose.yml << EOL
version: '3'

services:
  bazi-backend:
    build: .
    container_name: bazi-backend
    restart: always
    ports:
      - "6803:6803"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DEEPSEEK_API_KEY=your_deepseek_api_key_here
      - VOLCENGINE_API_KEY=your_volcengine_api_key_here
      - CORS_ALLOWED_ORIGINS=*
    volumes:
      - ./logs:/var/log/bazi-backend
EOL
```

#### 3. 构建和启动Docker容器

```bash
# 构建Docker镜像
docker build -t bazi-backend .

# 使用docker-compose启动
docker-compose up -d

# 查看容器日志
docker logs -f bazi-backend

# 或使用docker-compose查看日志
docker-compose logs -f
```

#### 4. 使用.env文件配置Docker环境变量

也可以创建`.env`文件，然后在`docker-compose.yml`中引用：

```bash
# 创建.env文件（与前面相同）
cat > .env << EOL
# 服务器配置
SERVER_PORT=6803
SPRING_PROFILES_ACTIVE=prod

# AI服务配置
AI_PROVIDER=volcengine

# DeepSeek API配置
DEEPSEEK_API_URL=https://api.deepseek.com/v1/chat/completions
DEEPSEEK_API_KEY=your_deepseek_api_key_here

# 火山引擎API配置
VOLCENGINE_API_KEY=your_volcengine_api_key_here
VOLCENGINE_MODEL=doubao-lite-128k-240828

# 跨域设置
CORS_ALLOWED_ORIGINS=*
EOL

# 修改docker-compose.yml引用.env文件
cat > docker-compose.yml << EOL
version: '3'

services:
  bazi-backend:
    build: .
    container_name: bazi-backend
    restart: always
    ports:
      - "\${SERVER_PORT}:\${SERVER_PORT}"
    env_file: .env
    volumes:
      - ./logs:/var/log/bazi-backend
EOL

# 启动容器
docker-compose up -d
```

### 生产环境配置注意事项

1. **安全性考虑**
   - 避免在代码仓库中存储敏感信息
   - 使用环境变量或外部配置文件存储API密钥
   - 正式环境建议使用HTTPS加密通信

2. **性能优化**
   - 增加JVM内存分配: `-Xmx512m -Xms256m`
   - 启用GC日志记录: `-Xlog:gc*`
   - 生产环境增加max-connections参数

3. **监控与日志**
   - 配置集中式日志收集
   - 设置应用健康检查
   - 配置系统资源监控

4. **高可用部署**
   - 使用Nginx负载均衡
   - 配置多实例部署
   - 设置自动重启机制

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
- [本地开发环境配置指南](local-dev-guide.md)
- [API密钥申请指南](api-key-guide.md)
- [服务器配置指南](server-config-guide.md)
- [HTTPS配置指南](https-setup-guide.md)
- [子域名配置指南](subdomain-setup-guide.md)
- [用 Cursor 开发小程序万字沉浸式教程](https://mp.weixin.qq.com/s/E7jeUn3RNTV7AQK38oEcXg)
- [AI 上线微信小程序教程](https://mp.weixin.qq.com/s/TWbPn-LXEFL1wbYkFf9Gyg)

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
| 需求分析 | 5-7天   | 30分钟        |
| 系统设计 | 3-5天   | 40分钟        |
| 后端开发 | 7-14天  | 40分钟        |
| 前端开发 | 7-14天  | 40分钟        |
| 系统集成 | 3-5天   | 30分钟        |
| 部署上线 | 3-5天   | 2天        |
| 文档编写 | 3-7天   | 1天        |

### 📚 完整的学习资源

> 🎓 查看[开发日志](docs/dev-journey.md)了解本项目从构思到完成的全过程，以及AI在每个开发阶段的具体应用。
>
> 💻 所有代码都包含详细注释，说明AI生成代码的思路和逻辑。
>
> 📖 项目包含多种技术栈的实践案例：微信小程序、Java Spring Boot、AI模型集成。 

### star 趋势图

[![Star History Chart](https://api.star-history.com/svg?repos=freestylefly/wechat-mini&type=Date)](https://star-history.com/#freestylefly/wechat-mini&Date)

### 关注公众号

微信搜 **苍何** 或扫描下方二维码关注苍何的原创公众号，回复 **666** 即可免费领取 2000G AI 编程学习资源。

![苍何微信公众号](https://cdn.tobebetterjavaer.com/stutymore/%E6%89%AB%E7%A0%81_%E6%90%9C%E7%B4%A2%E8%81%94%E5%90%88%E4%BC%A0%E6%92%AD%E6%A0%B7%E5%BC%8F-%E6%A0%87%E5%87%86%E8%89%B2%E7%89%88.png)