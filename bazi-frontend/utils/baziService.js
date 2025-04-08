/**
 * 八字命理计算服务
 */
import request from './request';
import { API } from '../config/api';

/**
 * 计算八字命理信息
 * @param {string} date - 阳历日期 YYYY-MM-DD
 * @param {string} time - 时辰，如"子时"
 * @returns {Promise} 返回计算结果Promise
 */
export const calculateBazi = (date, time) => {
  return request.post(API.CALCULATE_BAZI, {
    date,
    time
  });
};

/**
 * 获取时辰选项
 * @returns {Array} 返回时辰选项数组
 */
export const getTimeOptions = () => {
  return [
    { text: '子时 (23:00-00:59)', value: '子时' },
    { text: '丑时 (01:00-02:59)', value: '丑时' },
    { text: '寅时 (03:00-04:59)', value: '寅时' },
    { text: '卯时 (05:00-06:59)', value: '卯时' },
    { text: '辰时 (07:00-08:59)', value: '辰时' },
    { text: '巳时 (09:00-10:59)', value: '巳时' },
    { text: '午时 (11:00-12:59)', value: '午时' },
    { text: '未时 (13:00-14:59)', value: '未时' },
    { text: '申时 (15:00-16:59)', value: '申时' },
    { text: '酉时 (17:00-18:59)', value: '酉时' },
    { text: '戌时 (19:00-20:59)', value: '戌时' },
    { text: '亥时 (21:00-22:59)', value: '亥时' }
  ];
};

export default {
  calculateBazi,
  getTimeOptions
}; 