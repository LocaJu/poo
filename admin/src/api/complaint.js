import request from '../utils/request';

export function getComplaintList(status, page = 1, pageSize = 10) {
  return request.get('/admin/complaints', { params: { status, page, pageSize } });
}

export function resolveComplaint(id, result, action) {
  return request.post(`/admin/complaints/${id}/resolve`, { result, action });
}
