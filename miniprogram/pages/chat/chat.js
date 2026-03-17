Page({
  data: {
    list: []
  },

  onLoad() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
    }
  },

  onShow() {
    this.loadConversations();
  },

  loadConversations() {
    this.setData({ list: [] });
  }
});
