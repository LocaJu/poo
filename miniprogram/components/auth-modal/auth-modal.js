Component({
  properties: {
    show: {
      type: Boolean,
      value: false
    }
  },

  methods: {
    handleConfirm() {
      this.triggerEvent('confirm');
    },
    handleCancel() {
      this.triggerEvent('cancel');
    }
  }
});
