// 导入八字服务
import baziService from '../../utils/baziService';

// 输入页面
Page({
  data: {
    // 日期值
    date: '',
    // 时辰文本
    timeText: '',
    // 时辰值
    timeValue: '',
    // 是否可以计算
    canCalculate: false,
    
    // 日期选择器相关
    showDatePicker: false,
    years: [],
    months: [],
    days: [],
    datePickerValue: [50, 0, 0], // 默认选中2000年1月1日
    tempDatePickerValue: [50, 0, 0],
    
    // 时辰选择器相关
    showTimePicker: false,
    timeOptions: [],
    timePickerValue: [0],
    tempTimePickerValue: [0]
  },
  
  onLoad() {
    // 初始化年月日选项
    this.initDateOptions();
    
    // 初始化时辰选项
    this.initTimeOptions();
  },
  
  /**
   * 初始化日期选项
   */
  initDateOptions() {
    // 年份从1950年到2050年
    const years = [];
    for (let i = 1950; i <= 2050; i++) {
      years.push(i);
    }
    
    // 月份1-12
    const months = [];
    for (let i = 1; i <= 12; i++) {
      months.push(i);
    }
    
    // 日期1-31
    const days = [];
    for (let i = 1; i <= 31; i++) {
      days.push(i);
    }
    
    this.setData({
      years,
      months,
      days
    });
  },
  
  /**
   * 初始化时辰选项
   */
  initTimeOptions() {
    const timeOptions = baziService.getTimeOptions();
    this.setData({
      timeOptions
    });
  },
  
  /**
   * 检查表单是否可提交
   */
  checkForm() {
    const { date, timeValue } = this.data;
    const canCalculate = !!date && !!timeValue;
    this.setData({
      canCalculate
    });
  },
  
  /**
   * 显示日期选择器
   */
  onShowDatePicker() {
    this.setData({
      showDatePicker: true
    });
  },
  
  /**
   * 隐藏日期选择器
   */
  onCancelDatePicker() {
    this.setData({
      showDatePicker: false
    });
  },
  
  /**
   * 日期选择器变化
   */
  onDatePickerChange(e) {
    this.setData({
      tempDatePickerValue: e.detail.value
    });
  },
  
  /**
   * 确认日期选择
   */
  onConfirmDatePicker() {
    const { years, months, days, tempDatePickerValue } = this.data;
    const [yearIndex, monthIndex, dayIndex] = tempDatePickerValue;
    
    const year = years[yearIndex];
    const month = months[monthIndex].toString().padStart(2, '0');
    const day = days[dayIndex].toString().padStart(2, '0');
    
    // 检查日期是否有效
    if (this.isValidDate(`${year}-${month}-${day}`)) {
      this.setData({
        date: `${year}-${month}-${day}`,
        datePickerValue: tempDatePickerValue,
        showDatePicker: false
      });
      
      this.checkForm();
    } else {
      wx.showToast({
        title: '日期无效，请重新选择',
        icon: 'none'
      });
    }
  },
  
  /**
   * 检查日期是否有效
   */
  isValidDate(dateStr) {
    const date = new Date(dateStr);
    return !isNaN(date.getTime());
  },
  
  /**
   * 显示时辰选择器
   */
  onShowTimePicker() {
    this.setData({
      showTimePicker: true
    });
  },
  
  /**
   * 隐藏时辰选择器
   */
  onCancelTimePicker() {
    this.setData({
      showTimePicker: false
    });
  },
  
  /**
   * 时辰选择器变化
   */
  onTimePickerChange(e) {
    this.setData({
      tempTimePickerValue: e.detail.value
    });
  },
  
  /**
   * 确认时辰选择
   */
  onConfirmTimePicker() {
    const { timeOptions, tempTimePickerValue } = this.data;
    const [timeIndex] = tempTimePickerValue;
    const selectedTime = timeOptions[timeIndex];
    
    this.setData({
      timeText: selectedTime.text,
      timeValue: selectedTime.value,
      timePickerValue: tempTimePickerValue,
      showTimePicker: false
    });
    
    this.checkForm();
  },
  
  /**
   * 阻止冒泡
   */
  preventBubble() {
    // 阻止冒泡，防止点击选择器内部关闭选择器
  },
  
  /**
   * 点击计算按钮
   */
  onCalculate() {
    const { date, timeValue, canCalculate } = this.data;
    
    if (!canCalculate) {
      return;
    }
    
    wx.showLoading({
      title: '计算中...',
      mask: true
    });
    
    // 调用八字计算服务
    baziService.calculateBazi(date, timeValue)
      .then(result => {
        wx.hideLoading();
        
        // 将结果保存到全局数据
        const app = getApp();
        app.globalData.calculationResult = result;
        
        // 跳转到结果页面
        wx.navigateTo({
          url: '/pages/result/result'
        });
      })
      .catch(error => {
        wx.hideLoading();
        wx.showToast({
          title: '计算失败，请重试',
          icon: 'none'
        });
        console.error('计算失败:', error);
      });
  }
}); 