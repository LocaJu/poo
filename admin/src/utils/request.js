/**
 * 管理后台 API 请求封装
 */
import axios from 'axios';
import { useUserStore } from '../store/user';

const instance = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
});

instance.interceptors.request.use((config) => {
  const token = useUserStore().token;
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }
  return config;
});

instance.interceptors.response.use(
  (res) => {
    const data = res.data;
    if (data && data.code !== 0) {
      return Promise.reject(new Error(data.message || '请求失败'));
    }
    return data ? data.data : res.data;
  },
  (err) => {
    if (err.response && err.response.status === 401) {
      useUserStore().logout();
      window.location.href = '/#/login';
    }
    return Promise.reject(err);
  }
);

export default instance;
