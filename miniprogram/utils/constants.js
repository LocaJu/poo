/**
 * 常量定义（与 app.wxss 设计令牌语义一致）
 */

const HOUSE_TYPE = ['1室', '2室', '3室', '4室', '5室及以上'];
const DEPOSIT_OPTIONS = ['押一付一', '押一付三', '面议'];
const HOUSE_STATUS_MAP = {
  pending: '待审核',
  approved: '已上架',
  rejected: '已驳回',
  offline: '已下架'
};
const COMPLAINT_REASONS = ['虚假信息', '中介冒充个人', '违规收费', '其他'];

const MAIN_CITIES = ['全部', '南京市', '杭州市', '上海市', '苏州市', '北京市', '深圳市', '广州市'];

const QUICK_TAGS = [
  { id: 'benefit', label: '接租福利', keyword: '福利' },
  { id: 'contract', label: '合同验证', keyword: '合同' },
  { id: 'movein', label: '即时入住', keyword: '入住' },
  { id: 'gas', label: '通天然气', keyword: '天然气' }
];

/** 与全局 UI 主色一致（文档/注释用） */
const THEME_PRIMARY = '#0F766E';
const THEME_PRIMARY_LIGHT = '#14B8A6';
const THEME_ACCENT = '#EA580C';

module.exports = {
  HOUSE_TYPE,
  DEPOSIT_OPTIONS,
  HOUSE_STATUS_MAP,
  COMPLAINT_REASONS,
  MAIN_CITIES,
  QUICK_TAGS,
  THEME_PRIMARY,
  THEME_PRIMARY_LIGHT,
  THEME_ACCENT
};
