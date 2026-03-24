const request = require('../../utils/request');
const util = require('../../utils/util');
const { MAIN_CITIES, QUICK_TAGS } = require('../../utils/constants');

const COMMUTE_KEY = 'commute_destination';

/** 首页顶部轮播图（静态资源，可替换 miniprogram/images/banner-*.png） */
const BANNER_IMAGES = [
  '/images/banner-1.png',
  '/images/banner-2.png',
  '/images/banner-3.png',
  '/images/banner-4.png',
  '/images/banner-5.png'
];

Page({
  data: {
    bannerImages: BANNER_IMAGES,
    cities: MAIN_CITIES,
    cityIndex: 0,
    searchKeyword: '',
    quickTags: QUICK_TAGS.map((t) => ({ ...t, active: false })),
    sortType: 'latest',
    filterLeaseOpen: false,
    filterPriceOpen: false,
    filterTypeOpen: false,
    leaseOptions: ['不限', '1个月内', '3个月内', '半年内', '1年内'],
    leaseIndex: 0,
    priceRanges: [
      { label: '不限', min: null, max: null },
      { label: '1500以下', min: null, max: 1500 },
      { label: '1500-2500', min: 1500, max: 2500 },
      { label: '2500-4000', min: 2500, max: 4000 },
      { label: '4000以上', min: 4000, max: null }
    ],
    priceRangeIndex: 0,
    typeOptions: ['不限', '1室', '2室', '3室', '4室', '5室及以上'],
    typeIndex: 0,
    commuteText: '未设置',
    userLat: null,
    userLng: null,
    list: [],
    total: 0,
    page: 1,
    pageSize: 10,
    loading: false,
    hasMore: true
  },

  onLoad() {
    this.loadCommute();
    this.tryUserLocation();
    this.loadList();
  },

  onShow() {
    this.loadCommute();
  },

  loadCommute() {
    try {
      const raw = wx.getStorageSync(COMMUTE_KEY);
      if (raw && raw.address) {
        this.setData({ commuteText: raw.address.length > 12 ? raw.address.slice(0, 12) + '…' : raw.address });
      } else {
        this.setData({ commuteText: '未设置' });
      }
    } catch (e) {
      this.setData({ commuteText: '未设置' });
    }
  },

  tryUserLocation() {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        this.setData({ userLat: res.latitude, userLng: res.longitude });
        if (this.data.list.length) this.decorateList(this.data.list);
      },
      fail: () => {}
    });
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

  buildKeyword() {
    const city = this.data.cities[this.data.cityIndex];
    const kw = (this.data.searchKeyword || '').trim();
    const parts = [];
    if (city && city !== '全部') parts.push(city.replace('市', ''));
    if (kw) parts.push(kw);
    this.data.quickTags.forEach((t) => {
      if (t.active && t.keyword) parts.push(t.keyword);
    });
    return parts.length ? parts.join(' ') : '';
  },

  buildQuery() {
    const pr = this.data.priceRanges[this.data.priceRangeIndex];
    const typeOpt = this.data.typeOptions[this.data.typeIndex];
    let orderBy = 'latest';
    if (this.data.sortType === 'price_low') orderBy = 'price_asc';
    else if (this.data.sortType === 'price_high') orderBy = 'price_desc';
    else if (this.data.sortType === 'area') orderBy = 'area_desc';
    else orderBy = 'latest';

    const q = {
      page: this.data.page,
      pageSize: this.data.pageSize,
      orderBy
    };
    const kw = this.buildKeyword();
    if (kw) q.keyword = kw;
    if (pr.min != null) q.priceMin = pr.min;
    if (pr.max != null) q.priceMax = pr.max;
    if (typeOpt && typeOpt !== '不限') q.type = typeOpt;
    return q;
  },

  decorateList(rawList) {
    const lat = this.data.userLat;
    const lng = this.data.userLng;
    return (rawList || []).map((h) => {
      const loc = h.location || {};
      const city = loc.city || '';
      const district = loc.district || '';
      const metaLoc = city && district ? `${city}·${district}` : city || district || '位置待定';
      let rentTag = '[整租]';
      if (h.type && (h.type.indexOf('合租') >= 0 || h.type.indexOf('单间') >= 0)) rentTag = '[合租]';
      let distText = '';
      const dkm = util.distanceKm(lat, lng, loc.latitude, loc.longitude);
      if (dkm != null) distText = `距离${dkm}km`;
      const subTags = [];
      if (h.deposit && h.deposit.indexOf('押一') >= 0) subTags.push('押付灵活');
      if (h.description && h.description.indexOf('宠') >= 0) subTags.push('可养宠');
      if (h.description && h.description.indexOf('天然气') >= 0) subTags.push('天然气');
      return Object.assign({}, h, {
        _rentTag: rentTag,
        _metaLoc: metaLoc,
        _pubDate: util.formatPublishDate(h.createTime),
        _distText: distText,
        _subTags: subTags.slice(0, 2)
      });
    });
  },

  applyClientSort(list) {
    const t = this.data.sortType;
    const arr = [...list];
    if (t === 'urgent') {
      arr.sort((a, b) => {
        const ua = (a.title || '').indexOf('急') >= 0 ? 1 : 0;
        const ub = (b.title || '').indexOf('急') >= 0 ? 1 : 0;
        if (ub !== ua) return ub - ua;
        return new Date(b.createTime) - new Date(a.createTime);
      });
    } else if (t === 'distance') {
      arr.sort((a, b) => {
        const la = a.location;
        const lb = b.location;
        const da = util.distanceKm(this.data.userLat, this.data.userLng, la && la.latitude, la && la.longitude);
        const db = util.distanceKm(this.data.userLat, this.data.userLng, lb && lb.latitude, lb && lb.longitude);
        if (da == null && db == null) return 0;
        if (da == null) return 1;
        if (db == null) return -1;
        return da - db;
      });
    } else if (t === 'lease') {
      arr.sort((a, b) => {
        if (!a.endDate || !b.endDate) return 0;
        return new Date(a.endDate) - new Date(b.endDate);
      });
    }
    return arr;
  },

  async loadList() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    try {
      const q = this.buildQuery();
      const res = await request.get('/houses', q, { showLoading: false });
      let chunk = this.decorateList(res.list || []);
      let list = this.data.page === 1 ? chunk : [...this.data.list, ...chunk];
      if (['urgent', 'distance', 'lease'].includes(this.data.sortType)) {
        list = this.applyClientSort(list);
      }
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

  onCityChange(e) {
    this.setData({ cityIndex: Number(e.detail.value), page: 1, hasMore: true });
    this.loadList();
  },

  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value });
  },

  onSearchConfirm() {
    this.setData({ page: 1, hasMore: true });
    this.loadList();
  },

  toggleQuickTag(e) {
    const id = e.currentTarget.dataset.id;
    const quickTags = this.data.quickTags.map((t) =>
      t.id === id ? { ...t, active: !t.active } : t
    );
    this.setData({ quickTags, page: 1, hasMore: true });
    this.loadList();
  },

  setSort(e) {
    let sortType = e.currentTarget.dataset.sort;
    if (!sortType) return;
    if (sortType === 'price_low') {
      if (this.data.sortType === 'price_low') sortType = 'price_high';
      else sortType = 'price_low';
    }
    this.setData({ sortType, page: 1, hasMore: true });
    this.loadList();
  },

  toggleFilter(e) {
    const key = e.currentTarget.dataset.key;
    if (key === 'lease') this.setData({ filterLeaseOpen: !this.data.filterLeaseOpen, filterPriceOpen: false, filterTypeOpen: false });
    if (key === 'price') this.setData({ filterPriceOpen: !this.data.filterPriceOpen, filterLeaseOpen: false, filterTypeOpen: false });
    if (key === 'type') this.setData({ filterTypeOpen: !this.data.filterTypeOpen, filterLeaseOpen: false, filterPriceOpen: false });
    if (key === 'more') {
      wx.navigateTo({ url: '/pages/search/search' });
    }
  },

  onLeasePick(e) {
    this.setData({
      leaseIndex: Number(e.detail.value),
      filterLeaseOpen: false,
      page: 1,
      hasMore: true
    });
    this.loadList();
  },

  onPricePick(e) {
    this.setData({
      priceRangeIndex: Number(e.detail.value),
      filterPriceOpen: false,
      page: 1,
      hasMore: true
    });
    this.loadList();
  },

  onTypePick(e) {
    this.setData({
      typeIndex: Number(e.detail.value),
      filterTypeOpen: false,
      page: 1,
      hasMore: true
    });
    this.loadList();
  },

  maskClose() {
    this.setData({ filterLeaseOpen: false, filterPriceOpen: false, filterTypeOpen: false });
  },

  handleTapHouse(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/detail/detail?id=' + id });
  },

  handleCommuteSet() {
    wx.chooseLocation({
      success: (res) => {
        wx.setStorageSync(COMMUTE_KEY, {
          address: res.address,
          latitude: res.latitude,
          longitude: res.longitude
        });
        this.loadCommute();
        wx.showToast({ title: '已保存', icon: 'success' });
      }
    });
  },

  goMap() {
    wx.navigateTo({ url: '/pages/map/map' });
  },

  goPublish() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先在「我的」登录', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/publish/publish' });
  }
});
