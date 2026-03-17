const request = require('../../utils/request');
const util = require('../../utils/util');

Page({
  data: {
    list: [],
    total: 0,
    page: 1,
    pageSize: 10,
    loading: false,
    hasMore: true
  },

  onLoad() {
    this.loadList();
  },

  onPullDownRefresh() {
    this.setData({ page: 1, hasMore: true });
    this.loadList().then(() => wx.stopPullDownRefresh());
  },

  onReachBottom() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ page: this.data.page + 1 });
    this.loadList();
  },

  async loadList() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    try {
      const res = await request.get('/houses', {
        page: this.data.page,
        pageSize: this.data.pageSize,
        orderBy: 'latest'
      }, { showLoading: false });
      const list = this.data.page === 1 ? (res.list || []) : [...(this.data.list || []), ...(res.list || [])];
      const hasMore = list.length < (res.total || 0);
      this.setData({
        list,
        total: res.total || 0,
        loading: false,
        hasMore
      });
    } catch (e) {
      this.setData({ loading: false });
    }
  },

  handleTapHouse(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/detail/detail?id=' + id });
  },

  handleTapSearch() {
    wx.navigateTo({ url: '/pages/search/search' });
  }
});
