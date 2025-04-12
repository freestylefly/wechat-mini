/**
 * 环境配置处理脚本
 * 用于在构建过程中读取.env文件并替换配置
 * 
 * 使用方法:
 * 1. 在项目根目录创建.env文件
 * 2. 在构建前运行此脚本
 * 
 * 注意: 此脚本需要在Node.js环境中运行，不是微信小程序代码的一部分
 */

const fs = require('fs');
const path = require('path');
const dotenv = require('dotenv');  // 需要安装：npm install dotenv --save-dev

// 配置文件路径
const ENV_FILE_PATH = path.resolve(__dirname, '../../.env');
const API_CONFIG_PATH = path.resolve(__dirname, '../api.js');

// 默认配置
const DEFAULT_ENV = 'development';
const DEFAULT_DEV_API = 'http://localhost:8080/api';
const DEFAULT_PROD_API = 'https://example.com/api';

// 读取环境变量
function loadEnvFile() {
  try {
    if (fs.existsSync(ENV_FILE_PATH)) {
      const envConfig = dotenv.parse(fs.readFileSync(ENV_FILE_PATH));
      console.log('成功加载.env文件');
      return envConfig;
    } else {
      console.warn('.env文件不存在，使用默认配置');
      return {
        CURRENT_ENV: DEFAULT_ENV,
        DEV_API_URL: DEFAULT_DEV_API,
        PROD_API_URL: DEFAULT_PROD_API
      };
    }
  } catch (error) {
    console.error('加载.env文件失败:', error);
    process.exit(1);
  }
}

// 更新API配置文件
function updateApiConfig(envConfig) {
  try {
    // 读取当前API配置文件
    let apiConfig = fs.readFileSync(API_CONFIG_PATH, 'utf8');
    
    // 替换环境设置
    apiConfig = apiConfig.replace(
      /const CURRENT_ENV = ['"].*['"];/,
      `const CURRENT_ENV = '${envConfig.CURRENT_ENV || DEFAULT_ENV}';`
    );
    
    // 替换API地址
    apiConfig = apiConfig.replace(
      /\[ENV\.DEV\]: ['"].*['"],/,
      `[ENV.DEV]: '${envConfig.DEV_API_URL || DEFAULT_DEV_API}',`
    );
    
    apiConfig = apiConfig.replace(
      /\[ENV\.PROD\]: ['"].*['"] \/\/ 通过构建工具替换此值/,
      `[ENV.PROD]: '${envConfig.PROD_API_URL || DEFAULT_PROD_API}' // 通过构建工具替换此值`
    );
    
    // 写入更新后的文件
    fs.writeFileSync(API_CONFIG_PATH, apiConfig);
    console.log('成功更新API配置文件');
  } catch (error) {
    console.error('更新API配置文件失败:', error);
    process.exit(1);
  }
}

// 执行配置更新
const envConfig = loadEnvFile();
updateApiConfig(envConfig);

console.log('环境配置更新完成!');
console.log(`当前环境: ${envConfig.CURRENT_ENV || DEFAULT_ENV}`);
console.log(`开发API: ${envConfig.DEV_API_URL || DEFAULT_DEV_API}`);
console.log(`生产API: ${envConfig.PROD_API_URL || DEFAULT_PROD_API}`); 