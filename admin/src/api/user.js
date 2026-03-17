import request from '../utils/request';

export function getUserList(page = 1, pageSize = 10) {
  return request.get('/admin/users', { params: { page, pageSize } });
}

export function setUserStatus(id, status) {
  return request.post(`/admin/users/${id}/status`, { status });
}

export function setUserVerified(id, verified) {
  return request.post(`/admin/users/${id}/verify`, { verified });
}
