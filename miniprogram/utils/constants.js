/**
 * 常量定义
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

module.exports = {
  HOUSE_TYPE,
  DEPOSIT_OPTIONS,
  HOUSE_STATUS_MAP,
  COMPLAINT_REASONS
};
