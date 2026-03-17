const request = require('../../utils/request');
const { HOUSE_TYPE, DEPOSIT_OPTIONS } = require('../../utils/constants');

Page({
  data: {
    HOUSE_TYPE,
    DEPOSIT_OPTIONS,
    title: '',
    type: '',
    area: '',
    price: '',
    deposit: '',
    startDate: '',
    endDate: '',
    availableDate: '',
    description: '',
    images: [],
    location: null
  },

  onLoad() {
    if (!getApp().globalData.token) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      setTimeout(() => wx.switchTab({ url: '/pages/profile/profile' }), 1500);
    }
  },

  onTitleInput(e) { this.setData({ title: e.detail.value }); },
  onTypeChange(e) { this.setData({ type: HOUSE_TYPE[e.detail.value] || '' }); },
  onAreaInput(e) { this.setData({ area: e.detail.value }); },
  onPriceInput(e) { this.setData({ price: e.detail.value }); },
  onDepositChange(e) { this.setData({ deposit: DEPOSIT_OPTIONS[e.detail.value] || '' }); },
  onStartDateChange(e) { this.setData({ startDate: e.detail.value }); },
  onEndDateChange(e) { this.setData({ endDate: e.detail.value }); },
  onAvailableChange(e) { this.setData({ availableDate: e.detail.value }); },
  onDescInput(e) { this.setData({ description: e.detail.value }); },

  chooseImage() {
    const remain = 9 - this.data.images.length;
    if (remain <= 0) {
      wx.showToast({ title: '最多9张', icon: 'none' });
      return;
    }
    wx.chooseMedia({
      count: remain,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const files = (res.tempFiles || []).map(f => f.tempFilePath);
        this.setData({ images: [...this.data.images, ...files].slice(0, 9) });
      }
    });
  },

  removeImage(e) {
    const idx = e.currentTarget.dataset.idx;
    const images = this.data.images.filter((_, i) => i !== idx);
    this.setData({ images });
  },

  chooseLocation() {
    wx.chooseLocation({
      success: (res) => {
        this.setData({
          location: {
            address: res.address,
            latitude: res.latitude,
            longitude: res.longitude
          }
        });
      }
    });
  },

  async submit() {
    const { title, price, images, location } = this.data;
    if (!title || !price) {
      wx.showToast({ title: '请填写标题和租金', icon: 'none' });
      return;
    }
    const dto = {
      title: this.data.title,
      type: this.data.type || null,
      area: this.data.area ? Number(this.data.area) : null,
      price: Number(this.data.price),
      deposit: this.data.deposit || null,
      startDate: this.data.startDate || null,
      endDate: this.data.endDate || null,
      availableDate: this.data.availableDate || null,
      description: this.data.description || null,
      images: [],
      location: null
    };
    if (location) {
      dto.location = {
        address: location.address,
        latitude: location.latitude,
        longitude: location.longitude
      };
    }
    try {
      await request.post('/houses', dto);
      wx.showToast({ title: '提交成功，等待审核', icon: 'success' });
      setTimeout(() => wx.navigateBack(), 1500);
    } catch (e) {}
  }
});
