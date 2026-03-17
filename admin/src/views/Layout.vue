<template>
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo">SubletHub</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#07c160"
      >
        <el-menu-item index="/dashboard">数据看板</el-menu-item>
        <el-menu-item index="/audit">审核管理</el-menu-item>
        <el-menu-item index="/users">用户管理</el-menu-item>
        <el-menu-item index="/houses">房源管理</el-menu-item>
        <el-menu-item index="/complaints">举报处理</el-menu-item>
        <el-menu-item index="/settings">系统设置</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>{{ userStore.username }}</span>
        <el-button link type="danger" @click="handleLogout">退出</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useUserStore } from '../store/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

function handleLogout() {
  userStore.logout();
  router.push('/login');
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #304156; }
.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-weight: 600;
}
.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 24px;
  border-bottom: 1px solid #eee;
}
.main { background: #f0f2f5; padding: 24px; overflow: auto; }
</style>
