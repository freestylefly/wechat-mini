# 本地开发环境配置指南

本指南详细介绍了如何在不泄露敏感信息的情况下，配置和运行本地开发环境。通过使用本地配置策略，您可以安全地开发和测试应用，同时保持代码库的安全性。

## 前端配置指南

小程序前端使用`.env`文件和构建脚本来管理环境配置，这种方式确保敏感信息仅存在于开发者的本地环境。

### 首次配置

1. **环境准备**
   - 确保安装了Node.js环境

2. **初始配置**
   - 进入前端目录：
     ```bash
     cd bazi-frontend
     ```
   - 复制模板文件：
     ```bash
     cp .env.template .env
     ```
   - 安装依赖：
     ```bash
     npm install
     ```
   - 编辑`.env`文件，设置环境和API地址：
     ```
     # 环境设置 (development 或 production)
     CURRENT_ENV=development
     
     # API地址配置
     DEV_API_URL=http://localhost:8080/api
     PROD_API_URL=https://your-actual-domain.com/api
     ```
   - 应用配置：
     ```bash
     npm run config
     ```

### 切换环境

1. **修改环境设置**
   - 编辑`bazi-frontend/.env`文件
   - 修改`CURRENT_ENV`的值：
     - 开发环境：`CURRENT_ENV=development` 
     - 生产环境：`CURRENT_ENV=production`

2. **应用新配置**
   ```bash
   cd bazi-frontend
   npm run config
   ```

3. **验证环境**
   - 配置脚本会输出当前环境信息：
     ```
     环境配置更新完成!
     当前环境: production
     开发API: http://localhost:8080/api
     生产API: https://your-domain.com/api
     ```

### 注意事项

- **配置文件位置**：`.env`文件必须放在`bazi-frontend/`目录下，不是在`config/`子目录
- **配置生效**：每次修改`.env`文件后必须运行`npm run config`命令使配置生效
- **本地存储**：配置不会上传到代码仓库，仅保存在开发者本地

## 后端配置指南

后端使用环境变量和`.env`文件管理敏感配置，Spring Boot会自动加载这些配置。

### 使用方法

1. **创建.env文件**
   - 复制模板文件：
     ```bash
     cp bazi-backend/.env.template bazi-backend/.env
     ```
   - 填写实际配置值：
     ```
     # DeepSeek API配置
     DEEPSEEK_API_KEY=your_actual_key_here
     
     # 火山引擎API配置
     VOLCENGINE_API_KEY=your_actual_key_here
     ```

2. **启动应用**
   - 应用启动时会自动加载`.env`文件中的环境变量
   - 日志中会显示"成功从 xxx 加载本地环境配置"

### 技术实现

1. 后端使用Java自定义加载器读取`.env`文件
2. 将`.env`文件加入`.gitignore`，确保不会被提交
3. 使用Spring Boot的属性占位符语法读取环境变量

## 目录结构参考

```
wechat-mini/
├── bazi-frontend/
│   ├── .env              <- 前端环境配置文件
│   ├── .env.template     <- 前端配置模板
│   ├── config/
│   │   ├── api.js        <- API配置（由脚本自动更新）
│   │   └── scripts/
│   │       └── env-config.js
│   └── package.json
├── bazi-backend/
│   ├── .env              <- 后端环境配置文件
│   └── .env.template     <- 后端配置模板
```

## 安全注意事项

1. **不要提交敏感信息**
   - 确保.env文件和本地配置文件已添加到.gitignore
   - 提交代码前检查是否包含API密钥等敏感信息

2. **避免硬编码**
   - 不要在代码中硬编码API密钥和其他敏感信息
   - 始终使用环境变量或配置文件

3. **定期轮换密钥**
   - 定期更换API密钥和其他凭证
   - 发现泄露时立即更换

## 常见问题

### 1. 配置没有正确加载

检查：
- 文件位置：`.env`文件是否在正确位置
- 依赖安装：是否已执行`npm install`
- 命令执行：是否已运行`npm run config`
- 文件格式：是否有多余空格或引号

### 2. 环境切换问题

- 确保修改`.env`文件后重新运行`npm run config`
- 检查配置脚本输出信息，确认当前环境

### 3. 路径问题

- 始终从项目根目录使用相对路径
- 使用`cd bazi-frontend`进入前端目录后再执行命令