/**
 * 网络请求封装：统一 baseUrl、token、loading、错误提示
 */

const app = getApp();

function request(options) {
  const url = (options.url && options.url.startsWith('http'))
    ? options.url
    : (app.globalData.baseUrl || '') + (options.url || '');
  const header = {
    'Content-Type': 'application/json',
    ...options.header
  };
  if (app.globalData.token) {
    header['Authorization'] = 'Bearer ' + app.globalData.token;
  }
  const showLoading = options.showLoading !== false;
  if (showLoading) {
    wx.showLoading({ title: options.loadingText || '加载中...' });
  }
  return new Promise((resolve, reject) => {
    wx.request({
      url,
      method: options.method || 'GET',
      data: options.data,
      header,
      success(res) {
        if (showLoading) wx.hideLoading();
        if (res.statusCode >= 200 && res.statusCode < 300) {
          const data = res.data;
          if (data && data.code === 0) {
            resolve(data.data);
          } else {
            const msg = (data && data.message) || '请求失败';
            if (options.showError !== false) {
              wx.showToast({ title: msg, icon: 'none' });
            }
            reject(new Error(msg));
          }
        } else {
          if (showLoading) wx.hideLoading();
          let msg = (res.data && res.data.message) || '网络错误';
          if (res.statusCode === 500) {
            msg = '服务器错误，请确认后端已启动且数据库正常';
          } else if (res.statusCode === 401) {
            wx.removeStorageSync('token');
            app.globalData.token = null;
            wx.showToast({ title: '请先登录', icon: 'none' });
            reject(new Error(msg));
            return;
          }
          if (options.showError !== false) {
            wx.showToast({ title: msg, icon: 'none' });
          }
          reject(new Error(msg));
        }
      },
      fail(err) {
        if (showLoading) wx.hideLoading();
        if (options.showError !== false) {
          wx.showToast({ title: '网络请求失败', icon: 'none' });
        }
        reject(err);
      }
    });
  });
}

module.exports = {
  get: (url, data, opts = {}) => request({ ...opts, url, method: 'GET', data }),
  post: (url, data, opts = {}) => request({ ...opts, url, method: 'POST', data }),
  put: (url, data, opts = {}) => request({ ...opts, url, method: 'PUT', data }),
  del: (url, opts = {}) => request({ ...opts, url, method: 'DELETE' })
};
