// app.js
App({
  onLaunch() {
    // 小程序初始化逻辑
    console.log('小程序启动');
    
    // 获取微信环境信息（仅供内部参考）
    const wxEnv = __wxConfig.envVersion; // develop, trial, release
    console.log('当前微信环境:', wxEnv);
  },
  
  globalData: {
    // 全局数据
    calculationResult: null
  }
}) 