// 首页
Page({
  data: {
    // 页面数据
  },
  
  onLoad() {
    // 页面加载
  },
  
  /**
   * 点击开始计算按钮
   */
  onStartCalculate() {
    wx.navigateTo({
      url: '/pages/input/input'
    });
  }
}); 