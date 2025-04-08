// 结果页面
Page({
  data: {
    result: null
  },
  
  onLoad() {
    // 从全局数据获取计算结果
    const app = getApp();
    const result = app.globalData.calculationResult;
    
    if (!result) {
      wx.showToast({
        title: '数据错误，请重新计算',
        icon: 'none',
        duration: 2000
      });
      
      setTimeout(() => {
        wx.navigateBack();
      }, 2000);
      
      return;
    }
    
    // 设置结果数据
    this.setData({
      result
    });
  },
  
  /**
   * 再来一次
   */
  onCalculateAgain() {
    wx.navigateBack({
      delta: 1
    });
  }
}); 