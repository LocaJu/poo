请在此目录放置 TabBar 图标（建议 81x81 像素）：
- tab-home.png / tab-home-active.png
- tab-map.png / tab-map-active.png
- tab-chat.png / tab-chat-active.png
- tab-profile.png / tab-profile-active.png
以及 placeholder.png、avatar.png（可选）用于默认图。

首页顶部轮播（每 2 秒切换）使用：
  banner-1.png ~ banner-5.png
可将 5 张正式宣传图（建议宽高比约 5:2 或 750×320 像素）替换同名文件。

若暂无图标，可在微信开发者工具中暂时使用任意小图或从 WeUI 示例复制。
运行 node miniprogram/scripts/gen-tab-icons.js 可一键生成所有占位 PNG。
