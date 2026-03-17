const request = require('../../utils/request');

Page({
  data: {
    user: null,
    isLogin: false
  },

  onShow() {
    const token = getApp().globalData.token;
    this.setData({ isLogin: !!token });
    if (token) this.loadUser();
  },

  async loadUser() {
    try {
      const user = await request.get('/users/me', {}, { showLoading: false });
      this.setData({ user });
    } catch (e) {
      this.setData({ user: null });
    }
  },

  handleLogin() {
    wx.login({
      success: (res) => {
        request.post('/auth/wx-login', {
          code: res.code || 'mock_code',
          nickName: '微信用户',
          avatarUrl: ''
        }).then(data => {
          getApp().globalData.token = data.token;
          getApp().globalData.userInfo = data.user;
          wx.setStorageSync('token', data.token);
          this.setData({ isLogin: true, user: data.user });
          wx.showToast({ title: '登录成功', icon: 'success' });
        }).catch(() => {});
      }
    });
  },

  handleLogout() {
    wx.removeStorageSync('token');
    getApp().globalData.token = null;
    getApp().globalData.userInfo = null;
    this.setData({ isLogin: false, user: null });
    wx.showToast({ title: '已退出', icon: 'none' });
  },

  handleNav(e) {
    const url = e.currentTarget.dataset.url;
    if (!url) return;
    if (url.startsWith('/pages/publish')) {
      wx.navigateTo({ url });
    } else {
      wx.navigateTo({ url });
    }
  }
});
