import request from '../utils/request';

export function adminLogin(username, password) {
  return request.post('/auth/admin-login', { username, password });
}
