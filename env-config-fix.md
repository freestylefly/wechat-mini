# 微信小程序环境配置问题解决方案

## 问题描述

在微信小程序中，环境配置存在以下挑战：

1. **微信环境与应用环境不同步**
   - 微信开发工具有自己的环境标识（develop, trial, release）
   - 应用可能需要自定义的环境设置（development, production）

2. **无法直接读取本地文件**
   - 小程序无法直接读取本地文件系统上的`.env`文件
   - 需要使用小程序的存储机制

3. **全局状态与本地存储不同步**
   - 本地存储可能不会实时反映在全局状态中
   - 需要手动同步二者状态

## 解决方案

我们实现了一个完整的解决方案来处理这些问题：

### 1. 分离微信环境和应用环境

- 使用`isWxDevMode`表示微信开发工具的环境（控制UI显示）
- 使用`currentEnv`表示应用使用的API环境（控制API调用）

```javascript
// app.js
const wxEnv = __wxConfig.envVersion; // develop, trial, release
const isWxDevMode = wxEnv === 'develop' || wxEnv === 'trial';
const storedEnv = getEnv('CURRENT_ENV', 'development'); // 应用环境
```

### 2. 使用全局状态管理环境

在应用实例中维护全局环境状态：

```javascript
// app.js
globalData: {
  isWxDevMode: false,    // 微信开发环境（控制UI）
  currentEnv: 'development' // 应用环境（控制API）
}
```

### 3. 环境切换机制

当用户在配置页面切换环境时，同时更新：
- 本地存储
- 全局状态

```javascript
// 在app.js中添加方法
switchEnvironment(env) {
  setEnv('CURRENT_ENV', env);
  this.globalData.currentEnv = env;
  return env;
}
```

### 4. 动态获取API配置

API配置根据全局环境状态动态生成：

```javascript
const getGlobalEnv = () => {
  const app = getApp();
  if (app && app.globalData) {
    return app.globalData.currentEnv || wx.getStorageSync('CURRENT_ENV') || 'development';
  }
  return wx.getStorageSync('CURRENT_ENV') || 'development';
};
```

### 5. 环境信息显示

为了方便开发，在开发模式下显示当前环境信息：
- 首页底部显示当前环境
- 配置页面显示微信环境和应用环境

## 使用方法

1. **切换环境**
   - 在开发模式下，点击首页底部的"⚙️ 开发配置"按钮
   - 选择环境（开发/生产）并保存

2. **查看当前环境**
   - 开发模式下首页底部显示当前API环境
   - 配置页显示详细环境信息

3. **注意事项**
   - 切换环境后会立即生效，不需要重启应用
   - 生产环境和开发环境可以使用不同的API地址 