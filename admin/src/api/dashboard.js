import request from '../utils/request';

export function getStats() {
  return request.get('/admin/dashboard/stats');
}
