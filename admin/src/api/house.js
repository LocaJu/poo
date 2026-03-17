import request from '../utils/request';

export function getPendingHouses(page = 1, pageSize = 10) {
  return request.get('/admin/houses/pending', { params: { page, pageSize } });
}

export function approveHouse(id) {
  return request.post(`/admin/houses/${id}/approve`);
}

export function rejectHouse(id, reason) {
  return request.post(`/admin/houses/${id}/reject`, { reason });
}

export function getHouseList(page = 1, pageSize = 10) {
  return request.get('/admin/houses', { params: { page, pageSize } });
}
