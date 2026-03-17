# SubletHub 转租信息发布平台

微信小程序 + 管理后台 + Spring Boot 后端，帮助用户快速发布和查找转租房源。

## 项目结构

```
poo/
├── backend/          # Spring Boot 后端 (Java 11)
├── miniprogram/      # 微信小程序（原生）
├── admin/            # 管理后台 (Vue 3 + Vite + Element Plus)
├── docs/             # 文档与 SQL 参考
└── .cursorrules      # 项目规范
```

## 技术栈

- **后端**: Spring Boot 3.2、MyBatis（XML 配置）、MySQL、Redis、JWT
- **小程序**: 微信原生（WXML/WXSS/JS）
- **管理后台**: Vue 3、Vite、Pinia、Element Plus、Axios

## 快速开始

### 1. 数据库与 Redis

- 创建 MySQL 数据库：`sublethub`（或修改 `backend/src/main/resources/application.yml` 中的 `spring.datasource.url`）
- **执行建表脚本**：`backend/src/main/resources/schema.sql`（后端使用 MyBatis，不会自动建表）
- 确保 Redis 在 `localhost:6379` 运行（或修改配置）

### 2. 后端

```bash
cd backend
# 使用 Maven
mvn spring-boot:run
```

默认管理员：`admin` / `admin123`（首次启动自动创建）

API 根路径：`http://localhost:8080/api/v1`

### 3. 管理后台

```bash
cd admin
npm install
npm run dev
```

访问：`http://localhost:5174`，使用 `admin` / `admin123` 登录。

### 4. 小程序端（运行方式）

**必须使用「微信开发者工具」运行，无法用命令行直接启动。**

1. **安装微信开发者工具**  
   从 [微信公众平台 - 开发文档 - 开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html) 下载并安装。

2. **打开项目**  
   - 启动微信开发者工具  
   - 选择 **「导入项目」**（或「+」号）  
   - **目录**选择本仓库的**根目录**（即包含 `project.config.json` 和 `miniprogram` 的 `poo` 文件夹）  
   - **AppID**：使用你的小程序 AppID，或选择「测试号」先体验  
   - 点击「确定」后即可在模拟器中运行

3. **本地联调后端（可选）**  
   - 在 `miniprogram/app.js` 中把 `globalData.baseUrl` 改为你的后端地址，例如：  
     - 本机：`http://localhost:8080/api/v1`  
   - 在开发者工具右上角 **「详情」→「本地设置」** 中勾选 **「不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书」**，否则本地请求会报错。

4. **TabBar 图标（可选）**  
   若底部导航图标不显示，在 `miniprogram/images/` 下放入对应图标（见 `miniprogram/images/README.txt`），或先用任意小图占位。

5. **真机预览**  
   在开发者工具顶部点击 **「预览」**，用手机微信扫码即可在真机运行（真机需配置合法 request 域名且为 HTTPS）。

## 主要功能

- **小程序端**: 首页列表、搜索筛选、房源详情、发布/编辑/下架、收藏、地图找房、举报、个人中心、微信登录
- **管理后台**: 数据看板、房源审核、用户管理、房源管理、举报处理、系统设置
- **后端**: RESTful API、JWT 鉴权、统一返回 `{ code, data, message }`

## 常见问题（小程序端）

- **请求报 500 (Internal Server Error)**  
  表示后端出错或未启动。请先启动 Spring Boot 后端，并确认 MySQL、Redis 已启动且配置正确（见上文「快速开始」）。小程序会提示「服务器错误，请确认后端已启动且数据库正常」。

- **控制台报 `permission["scope.album"]` / `scope.camera` 无效**  
  已在 `app.json` 中移除无效配置。微信规定仅支持在 `permission` 中声明 `scope.userLocation`；相册、相机在调用 `wx.chooseMedia` 时会自动弹窗授权。

- **渲染层报错 `enableUpdateWxAppCode` of undefined**  
  多为微信开发者工具或基础库（如 3.15.0 灰度版）兼容问题。可在开发者工具 **「详情」→「本地设置」** 中将「基础库版本」改为稳定版（如 3.0.x）再试；若不影响功能可暂时忽略。

## 配置说明

- 后端 JWT 密钥与过期时间：`backend/src/main/resources/application.yml` → `jwt.*`
- 小程序请求基地址：`miniprogram/app.js` → `globalData.baseUrl`
- 管理后台代理：`admin/vite.config.js` → `server.proxy['/api']`

## 开发与部署

- 生产环境请修改 JWT 密钥、数据库密码，并配置 Nginx + Tomcat 部署后端；小程序需配置合法 request 域名。
- 图片上传：当前发布房源为本地路径占位，生产需对接对象存储（如腾讯云 COS）并实现上传接口。

## 许可证

MIT
