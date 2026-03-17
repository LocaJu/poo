/**
 * 生成 TabBar 占位图标（最小合法 PNG）
 * 在项目根目录运行：node miniprogram/scripts/gen-tab-icons.js
 */
const fs = require('fs');
const path = require('path');

const dir = path.join(__dirname, '..', 'images');
const minimalPng = Buffer.from(
  'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDQAHRwIAv8x7PgAAAABJRU5ErkJggg==',
  'base64'
);

const files = [
  'tab-home.png',
  'tab-home-active.png',
  'tab-map.png',
  'tab-map-active.png',
  'tab-chat.png',
  'tab-chat-active.png',
  'tab-profile.png',
  'tab-profile-active.png',
  'avatar.png',
  'placeholder.png'
];

if (!fs.existsSync(dir)) {
  fs.mkdirSync(dir, { recursive: true });
}

files.forEach((name) => {
  fs.writeFileSync(path.join(dir, name), minimalPng);
  console.log('Created:', name);
});

console.log('Done. TabBar icons + avatar.png、placeholder.png created in miniprogram/images/');
