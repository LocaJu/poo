Component({
  properties: {
    house: {
      type: Object,
      value: null
    }
  },

  methods: {
    handleTap() {
      const id = this.properties.house && this.properties.house.id;
      if (id) {
        wx.navigateTo({ url: '/pages/detail/detail?id=' + id });
      }
    }
  }
});
