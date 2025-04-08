/**
 * HTTP请求工具
 * 特别针对微信小程序环境优化
 */

/**
 * 发送请求
 * @param {string} url - 请求URL
 * @param {string} method - 请求方法
 * @param {object} data - 请求数据
 * @param {object} header - 请求头
 * @returns {Promise} 返回Promise对象
 */
const request = (url, method = 'GET', data = {}, header = {}) => {
  return new Promise((resolve, reject) => {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });

    // 构建自定义请求头，应对referrer policy限制
    const customHeaders = {
      'content-type': 'application/json',
      ...header
    };

    console.log(`正在请求API: ${url}`);
    
    wx.request({
      url,
      method,
      data,
      header: customHeaders,
      success: (res) => {
        wx.hideLoading();
        console.log(`API响应状态: ${res.statusCode}`);
        
        if (res.statusCode >= 200 && res.statusCode < 300) {
          // 接口正常
          if (res.data.code === 200) {
            // 业务逻辑正常
            resolve(res.data.data);
          } else {
            // 业务逻辑异常
            showError(res.data.message || '请求失败');
            reject(res.data);
          }
        } else {
          // 接口异常
          showError(`${res.statusCode}: ${res.data.message || '服务器错误'}`);
          reject(res.data);
        }
      },
      fail: (err) => {
        wx.hideLoading();
        showError('网络错误，请检查网络连接');
        reject(err);
      }
    });
  });
};

/**
 * 显示错误信息
 * @param {string} message - 错误信息
 */
const showError = (message) => {
  wx.showToast({
    title: message,
    icon: 'none',
    duration: 2000
  });
};

/**
 * GET请求
 * @param {string} url - 请求URL
 * @param {object} data - 请求参数
 * @param {object} header - 请求头
 * @returns {Promise} 返回Promise对象
 */
const get = (url, data = {}, header = {}) => {
  return request(url, 'GET', data, header);
};

/**
 * POST请求
 * @param {string} url - 请求URL
 * @param {object} data - 请求数据
 * @param {object} header - 请求头
 * @returns {Promise} 返回Promise对象
 */
const post = (url, data = {}, header = {}) => {
  return request(url, 'POST', data, header);
};

export default {
  request,
  get,
  post
};