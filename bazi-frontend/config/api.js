/**
 * API配置文件
 */

// 环境配置
const ENV = {
  DEV: 'development',   // 开发环境
  PROD: 'production'    // 生产环境
};

// 当前环境，可以根据需要修改
const CURRENT_ENV = ENV.PROD;  // 默认使用生产环境

// 各环境API基础URL配置
const BASE_URLS = {
  [ENV.DEV]: 'http://localhost:8080/api',           // 本地开发环境
  [ENV.PROD]: 'https://bazi.canghecode.com/api',    // 线上生产环境 - 子域名
  // 临时替代方案（如遇问题可切换）
  // [ENV.PROD]: 'https://8.137.9.219/api',         // 使用IP代替域名
};

// 获取当前环境的基础URL
const BASE_URL = BASE_URLS[CURRENT_ENV];

// API接口路径
export const API = {
  // 八字计算接口
  CALCULATE_BAZI: `${BASE_URL}/bazi/calculate`,
  
  // 环境信息
  ENV: CURRENT_ENV,
  IS_DEV: CURRENT_ENV === ENV.DEV,
  IS_PROD: CURRENT_ENV === ENV.PROD
};

// 导出环境配置，方便在其他文件中使用
export const ENVIRONMENT = {
  CURRENT: CURRENT_ENV,
  IS_DEV: CURRENT_ENV === ENV.DEV,
  IS_PROD: CURRENT_ENV === ENV.PROD,
  
  // 切换环境的方法（可选使用）
  switchToDev: () => {
    // 注意：此方法仅在调试模式下使用，实际环境切换应通过修改CURRENT_ENV常量实现
    console.log('已切换到开发环境');
    return BASE_URLS[ENV.DEV];
  },
  switchToProd: () => {
    // 注意：此方法仅在调试模式下使用，实际环境切换应通过修改CURRENT_ENV常量实现
    console.log('已切换到生产环境');
    return BASE_URLS[ENV.PROD];
  }
};

export default API; 