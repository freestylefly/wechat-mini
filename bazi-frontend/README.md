# 八字命理小助手-微信小程序前端

## 项目简介

八字命理小助手是一款基于微信小程序平台的命理分析工具，用户只需输入阳历出生日期和时辰，系统即可自动计算对应的农历日期、天干地支、五行属性、五行缺失情况及生肖信息。

## 技术栈

- 微信小程序原生开发
- 采用原生JS，未使用第三方框架
- 通过API与后端交互，获取八字计算结果

## 环境配置

项目支持两个环境：
- 开发环境（本地）：`http://localhost:8080/api`
- 生产环境（线上）：`https://api.your-domain.com/api`

### 环境配置方法

我们使用`.env`文件和构建脚本来管理环境配置：

1. **首次配置步骤**
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
   - 修改`.env`文件中的`CURRENT_ENV`值：
     - `CURRENT_ENV=development` 使用本地开发环境
     - `CURRENT_ENV=production` 使用生产环境
   - 重新应用配置：`npm run config`

3. **注意事项**
   - 每次修改`.env`后必须运行`npm run config`使配置生效
   - `.env`文件不会被提交到代码仓库，保证敏感信息安全

### 微信小程序配置

在使用前，需要在 `project.config.json` 文件中配置你自己的微信小程序 appid：

```json
{
  "appid": "YOUR_WECHAT_APPID",
  "compileType": "miniprogram",
  ...
}
```

你可以在[微信公众平台](https://mp.weixin.qq.com/)注册并获取 appid。

## 安全域名配置

为了让微信小程序能够正常访问后端API，需要在微信公众平台进行域名配置：

1. 登录[微信公众平台](https://mp.weixin.qq.com/)
2. 进入"开发管理" > "开发设置" > "服务器域名"
3. 在"request合法域名"中添加：
   - 开发环境：`localhost:8080`（仅在开发工具的本地模式下有效）
   - 生产环境：`api.your-domain.com`

**注意：** 
- 微信小程序正式环境只支持HTTPS请求，已配置为使用HTTPS
- 在开发者工具中，可以勾选"不校验合法域名..."选项进行本地测试

## 目录结构

```
bazi-frontend/
├── .env               # 环境配置文件（本地开发使用，不提交）
├── .env.template      # 环境配置模板
├── config/            # 配置文件
│   ├── api.js         # API接口配置
│   └── scripts/       # 脚本目录
│       └── env-config.js  # 环境配置处理脚本
├── pages/             # 页面文件
│   ├── index/         # 首页
│   ├── input/         # 信息输入页
│   └── result/        # 结果展示页
├── static/            # 静态资源
├── utils/             # 工具函数
├── package.json       # 项目配置
├── app.js             # 小程序入口文件
└── app.json           # 小程序配置文件
```

## 开发和运行

1. 安装Node.js环境（用于配置管理）
2. 进入项目目录，安装依赖：`npm install`
3. 创建并配置`.env`文件
4. 运行配置脚本：`npm run config`
5. 使用微信开发者工具导入项目
6. 确保后端服务已启动

## 页面说明

- 首页：展示小程序简介和进入按钮
- 信息输入页：提供阳历日期和时辰选择
- 结果展示页：展示八字计算结果，包含农历日期、天干地支、五行属性等信息

## API接口

- `/bazi/calculate`：八字计算接口，传入阳历日期和时辰，返回计算结果 