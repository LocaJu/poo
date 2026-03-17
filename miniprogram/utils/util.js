/**
 * 通用工具函数
 */

function formatTime(date) {
  if (!date) return '';
  const d = new Date(date);
  const now = new Date();
  const diff = now - d;
  if (diff < 60000) return '刚刚';
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前';
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前';
  if (diff < 604800000) return Math.floor(diff / 86400000) + '天前';
  return d.getMonth() + 1 + '-' + d.getDate();
}

function formatPrice(price) {
  if (price == null) return '';
  return Number(price).toFixed(0) + '元/月';
}

function formatArea(area) {
  if (area == null) return '';
  return area + '㎡';
}

module.exports = {
  formatTime,
  formatPrice,
  formatArea
};
