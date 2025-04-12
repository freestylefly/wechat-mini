// 导入环境配置工具
import { getEnv, setEnv, resetEnvConfig } from '../../utils/env-loader';
import { ENVIRONMENT } from '../../config/api';

Page({
  data: {
    currentEnv: '',
    devApiUrl: '',
    prodApiUrl: '',
    appId: '',
    showSuccess: false,
    successMsg: '',
    wxEnvInfo: '' // 微信环境信息
  },

  onLoad: function() {
    this.loadConfig();
    
    // 显示微信环境信息
    const wxEnv = __wxConfig.envVersion || 'unknown';
    this.setData({
      wxEnvInfo: `微信环境: ${wxEnv}`
    });
  },

  loadConfig: function() {
    // 加载当前配置到页面
    this.setData({
      currentEnv: getEnv('CURRENT_ENV', 'development'),
      devApiUrl: getEnv('DEV_API_URL', 'http://localhost:8080/api'),
      prodApiUrl: getEnv('PROD_API_URL', 'https://example.com/api'),
      appId: getEnv('APPID', '开发模式无需填写')
    });
  },

  // 表单输入处理
  onEnvChange: function(e) {
    this.setData({
      currentEnv: e.detail.value
    });
  },

  onDevApiInput: function(e) {
    this.setData({
      devApiUrl: e.detail.value
    });
  },

  onProdApiInput: function(e) {
    this.setData({
      prodApiUrl: e.detail.value
    });
  },

  onAppIdInput: function(e) {
    this.setData({
      appId: e.detail.value
    });
  },

  // 保存配置
  saveConfig: function() {
    // 保存到本地存储
    setEnv('CURRENT_ENV', this.data.currentEnv);
    setEnv('DEV_API_URL', this.data.devApiUrl);
    setEnv('PROD_API_URL', this.data.prodApiUrl);
    setEnv('APPID', this.data.appId);

    // 应用环境切换
    if (this.data.currentEnv === 'development') {
      ENVIRONMENT.switchToDev();
    } else {
      ENVIRONMENT.switchToProd();
    }
    
    // 更新全局应用状态
    const app = getApp();
    app.switchEnvironment(this.data.currentEnv);
    
    console.log('已切换环境到:', this.data.currentEnv);

    // 显示成功提示
    this.setData({
      showSuccess: true,
      successMsg: `配置已保存！当前环境: ${this.data.currentEnv}`
    });

    setTimeout(() => {
      this.setData({
        showSuccess: false
      });
    }, 2000);
  },

  // 重置为默认配置
  resetConfig: function() {
    wx.showModal({
      title: '确认重置',
      content: '确定要重置所有配置为默认值吗？',
      success: (res) => {
        if (res.confirm) {
          resetEnvConfig();
          this.loadConfig();
          
          // 更新全局应用状态
          const app = getApp();
          app.switchEnvironment('development');
          
          this.setData({
            showSuccess: true,
            successMsg: '配置已重置！'
          });
          
          setTimeout(() => {
            this.setData({
              showSuccess: false
            });
          }, 2000);
        }
      }
    });
  }
}); 