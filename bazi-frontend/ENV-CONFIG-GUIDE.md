# 环境配置使用指南

本项目使用`.env`文件进行环境配置，在本地开发时可以通过修改此文件来配置不同的环境。

## 配置步骤

1. **创建环境配置文件**

   在`bazi-frontend`目录下，复制`.env.template`为`.env`：
   
   ```bash
   cp .env.template .env
   ```

2. **修改配置**

   编辑`.env`文件，根据需要修改以下配置：
   
   ```
   # 环境设置 (development 或 production)
   CURRENT_ENV=development  # 改为production以使用生产环境
   
   # API地址配置
   DEV_API_URL=http://localhost:8080/api
   PROD_API_URL=https://your-actual-domain.com/api
   
   # 微信小程序配置
   APPID=your_wechat_appid_here
   ```

3. **安装依赖**

   在开始前，先安装必要的依赖：
   
   ```bash
   npm install
   ```
   
   如果国内下载速度较慢，可以使用镜像源：
   
   ```bash
   npm install --registry=https://registry.npmmirror.com
   ```

4. **应用配置**

   安装完依赖后，运行以下命令将环境配置应用到项目中：
   
   ```bash
   npm run config
   ```

5. **验证配置**

   配置应用后，会在控制台输出当前使用的环境和API地址：
   
   ```
   环境配置更新完成!
   当前环境: production
   开发API: http://localhost:8080/api
   生产API: https://your-actual-domain.com/api
   ```

## 注意事项

1. `.env`文件已添加到`.gitignore`中，不会被提交到代码仓库，保证敏感信息安全

2. 每次修改`.env`文件后，需要重新运行`npm run config`命令使配置生效

3. 如果没有创建`.env`文件，系统将使用默认配置（开发环境）

4. 在代码中，可以通过`API.ENV`、`API.IS_DEV`和`API.IS_PROD`获取当前环境信息

5. 文件必须放在正确的位置 - `.env`和`.env.template`都应该在`bazi-frontend/`目录下，不是在`config/`子目录内 