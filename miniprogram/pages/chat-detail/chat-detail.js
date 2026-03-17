Page({
  data: {
    userId: '',
    houseId: '',
    messages: []
  },

  onLoad(options) {
    this.setData({ userId: options.userId || '', houseId: options.houseId || '' });
  }
});
