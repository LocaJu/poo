const request = require('../../utils/request');
const { HOUSE_TYPE, DEPOSIT_OPTIONS } = require('../../utils/constants');

Page({
  data: {
    keyword: '',
    list: [],
    page: 1,
    pageSize: 10,
    total: 0,
    loading: false,
    hasMore: true,
    filters: {
      type: '',
      priceMin: '',
      priceMax: '',
      deposit: '',
      orderBy: 'latest'
    }
  },

  onLoad() {
    this.loadList();
  },

  onReachBottom() {
    if (this.data.loading || !this.data.hasMore) return;
    this.setData({ page: this.data.page + 1 });
    this.loadList();
  },

  onKeywordConfirm(e) {
    this.setData({ keyword: e.detail.value, page: 1, hasMore: true });
    this.loadList();
  },

  async loadList() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    const q = {
      page: this.data.page,
      pageSize: this.data.pageSize,
      orderBy: this.data.filters.orderBy || 'latest'
    };
    if (this.data.keyword) q.keyword = this.data.keyword;
    if (this.data.filters.type) q.type = this.data.filters.type;
    if (this.data.filters.priceMin) q.priceMin = this.data.filters.priceMin;
    if (this.data.filters.priceMax) q.priceMax = this.data.filters.priceMax;
    try {
      const res = await request.get('/houses', q, { showLoading: false });
      const list = this.data.page === 1 ? (res.list || []) : [...this.data.list, ...(res.list || [])];
      this.setData({
        list,
        total: res.total || 0,
        loading: false,
        hasMore: list.length < (res.total || 0)
      });
    } catch (e) {
      this.setData({ loading: false });
    }
  },

  handleTapHouse(e) {
    wx.navigateTo({ url: '/pages/detail/detail?id=' + e.currentTarget.dataset.id });
  },

  handleOrderChange(e) {
    const orderBy = e.currentTarget.dataset.order;
    this.setData({ 'filters.orderBy': orderBy, page: 1, hasMore: true });
    this.loadList();
  }
});
