// SubletHub 转租信息发布平台 - 小程序入口
App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://localhost:8080/api/v1'
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
    }
  }
});
