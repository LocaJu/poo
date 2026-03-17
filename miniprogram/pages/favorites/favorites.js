const request = require('../../utils/request');

Page({
  data: {
    list: [],
    page: 1,
    pageSize: 10,
    hasMore: true,
    loading: false
  },

  onLoad() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    this.loadList();
  },

  onShow() {
    if (getApp().globalData.token) this.loadList();
  },

  onPullDownRefresh() {
    this.setData({ page: 1, hasMore: true });
    this.loadList().then(() => wx.stopPullDownRefresh());
  },

  onReachBottom() {
    if (!this.data.hasMore || this.data.loading) return;
    this.setData({ page: this.data.page + 1 });
    this.loadList();
  },

  async loadList() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    try {
      const res = await request.get('/favorites', { page: this.data.page, pageSize: this.data.pageSize }, { showLoading: false });
      const list = this.data.page === 1 ? (res.list || []) : [...this.data.list, ...(res.list || [])];
      this.setData({
        list,
        hasMore: list.length < (res.total || 0),
        loading: false
      });
    } catch (e) {
      this.setData({ loading: false });
    }
  },

  handleTap(e) {
    wx.navigateTo({ url: '/pages/detail/detail?id=' + e.currentTarget.dataset.id });
  }
});
