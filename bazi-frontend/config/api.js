/**
 * API配置文件
 */

// 环境配置
const ENV = {
  DEV: 'development',   // 开发环境
  PROD: 'production'    // 生产环境
};

// 从.env中读取环境设置，如不存在则使用默认值
// 这里使用硬编码方式，实际开发时可以替换为从构建工具注入的环境变量
const CURRENT_ENV = 'production';  // 通过构建工具替换此值

// 各环境API基础URL配置
const BASE_URLS = {
  // 开发环境API地址
  [ENV.DEV]: 'http://localhost:8080/api',
  // 生产环境API地址
  [ENV.PROD]: 'https://bazi.canghecode.com/api' // 通过构建工具替换此值
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

export default API; 