/**
 * 环境配置加载工具
 * 用于加载本地开发环境的敏感配置
 */

// 默认配置
const defaultConfig = {
  // 环境设置
  CURRENT_ENV: 'development',
  
  // API 配置
  DEV_API_URL: 'http://localhost:8080/api',
  PROD_API_URL: 'https://example.com/api', // 默认为示例值
  
  // 微信小程序配置
  APPID: 'default_appid_placeholder'
};

/**
 * 初始化环境配置
 * 在小程序启动时调用此方法加载配置
 * @param {Object} options 配置选项
 * @param {boolean} options.force 是否强制重新初始化
 */
export const initEnvConfig = (options = {}) => {
  try {
    console.log('正在加载环境配置...');
    
    // 检查是否已经有本地存储的配置
    const hasLocalConfig = wx.getStorageSync('HAS_LOCAL_CONFIG');
    
    if (!hasLocalConfig || options.force) {
      // 首次使用或强制初始化，初始化默认配置到本地存储
      Object.keys(defaultConfig).forEach(key => {
        wx.setStorageSync(key, defaultConfig[key]);
      });
      
      wx.setStorageSync('HAS_LOCAL_CONFIG', true);
      console.log('已初始化默认环境配置');
      
      // 同步更新全局状态
      try {
        const app = getApp();
        if (app && app.globalData) {
          app.globalData.currentEnv = defaultConfig.CURRENT_ENV;
        }
      } catch (e) {
        console.error('更新全局状态失败', e);
      }
    } else {
      console.log('使用已存在的本地环境配置');
    }
    
    return true;
  } catch (error) {
    console.error('加载环境配置失败:', error);
    return false;
  }
};

/**
 * 获取环境变量值
 * @param {string} key 环境变量名
 * @param {*} defaultValue 默认值
 * @returns 环境变量值
 */
export const getEnv = (key, defaultValue = null) => {
  try {
    const value = wx.getStorageSync(key);
    return value || defaultValue;
  } catch (error) {
    console.error(`获取环境变量 ${key} 失败:`, error);
    return defaultValue;
  }
};

/**
 * 设置环境变量值
 * @param {string} key 环境变量名
 * @param {*} value 环境变量值
 * @param {boolean} updateGlobal 是否更新全局状态
 */
export const setEnv = (key, value, updateGlobal = true) => {
  try {
    wx.setStorageSync(key, value);
    
    // 如果设置的是环境变量，同步更新全局状态
    if (key === 'CURRENT_ENV' && updateGlobal) {
      try {
        const app = getApp();
        if (app && app.globalData) {
          app.globalData.currentEnv = value;
          console.log('已更新全局环境状态:', value);
        }
      } catch (e) {
        console.error('更新全局状态失败', e);
      }
    }
    
    return true;
  } catch (error) {
    console.error(`设置环境变量 ${key} 失败:`, error);
    return false;
  }
};

/**
 * 重置环境配置为默认值
 */
export const resetEnvConfig = () => {
  try {
    Object.keys(defaultConfig).forEach(key => {
      setEnv(key, defaultConfig[key], false);
    });
    
    // 更新全局环境状态
    try {
      const app = getApp();
      if (app && app.globalData) {
        app.globalData.currentEnv = defaultConfig.CURRENT_ENV;
      }
    } catch (e) {
      console.error('更新全局状态失败', e);
    }
    
    console.log('环境配置已重置为默认值');
    return true;
  } catch (error) {
    console.error('重置环境配置失败:', error);
    return false;
  }
};

export default {
  initEnvConfig,
  getEnv,
  setEnv,
  resetEnvConfig
}; 