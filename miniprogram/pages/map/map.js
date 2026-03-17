const request = require('../../utils/request');

Page({
  data: {
    markers: [],
    latitude: 31.23,
    longitude: 121.47
  },

  onLoad() {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        this.setData({ latitude: res.latitude, longitude: res.longitude });
        this.loadHouses();
      },
      fail: () => this.loadHouses()
    });
  },

  async loadHouses() {
    try {
      const res = await request.get('/houses', { page: 1, pageSize: 50 }, { showLoading: false });
      const list = res.list || [];
      const markers = list
        .filter(h => h.location && h.location.latitude != null)
        .map((h, i) => ({
          id: h.id,
          latitude: h.location.latitude,
          longitude: h.location.longitude,
          width: 30,
          height: 30,
          title: h.title,
          callout: { content: h.price + '元/月', display: 'ALWAYS' }
        }));
      this.setData({ markers });
    } catch (e) {}
  },

  onMarkerTap(e) {
    const id = e.detail.markerId;
    wx.navigateTo({ url: '/pages/detail/detail?id=' + id });
  }
});
