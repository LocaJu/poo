const request = require('../../utils/request');
const util = require('../../utils/util');

Page({
  data: {
    id: null,
    house: null,
    favorited: false
  },

  onLoad(options) {
    const id = options.id;
    if (!id) {
      wx.showToast({ title: '参数错误', icon: 'none' });
      return;
    }
    this.setData({ id });
    this.loadDetail();
    this.checkFavorite();
  },

  async loadDetail() {
    try {
      const house = await request.get('/houses/' + this.data.id);
      if (house.images && typeof house.images === 'string') {
        house.imageList = house.images.split(',').filter(Boolean);
      } else {
        house.imageList = [];
      }
      this.setData({ house });
      wx.setNavigationBarTitle({ title: house.title || '房源详情' });
    } catch (e) {
      wx.showToast({ title: '加载失败', icon: 'none' });
    }
  },

  async checkFavorite() {
    const token = getApp().globalData.token;
    if (!token) return;
    try {
      const res = await request.get('/favorites/check', { houseId: this.data.id }, { showError: false });
      this.setData({ favorited: res.favorited || false });
    } catch (e) {}
  },

  handleTapFavorite() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      wx.navigateTo({ url: '/pages/profile/profile' });
      return;
    }
    const favorited = !this.data.favorited;
    const method = favorited ? 'post' : 'del';
    const url = favorited ? '/favorites/' + this.data.id : '/favorites/' + this.data.id;
    request[method](url, {}, { showLoading: false }).then(() => {
      this.setData({ favorited });
      wx.showToast({ title: favorited ? '已收藏' : '已取消', icon: 'success' });
    }).catch(() => {});
  },

  handleTapChat() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    const house = this.data.house;
    if (!house || !house.userId) return;
    wx.navigateTo({ url: '/pages/chat-detail/chat-detail?userId=' + house.userId + '&houseId=' + this.data.id });
  },

  handleTapComplaint() {
    wx.showActionSheet({
      itemList: ['虚假信息', '中介冒充个人', '违规收费', '其他'],
      success: (res) => {
        const reasons = ['虚假信息', '中介冒充个人', '违规收费', '其他'];
        this.submitComplaint(reasons[res.tapIndex]);
      }
    });
  },

  async submitComplaint(reason) {
    try {
      await request.post('/complaints', {
        targetType: 'house',
        targetId: this.data.id,
        reason
      });
      wx.showToast({ title: '举报已提交', icon: 'success' });
    } catch (e) {}
  }
});
