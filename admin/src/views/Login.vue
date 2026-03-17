<template>
  <div class="login-page">
    <div class="login-box">
      <h1>SubletHub 管理后台</h1>
      <el-form :model="form" label-width="80" style="max-width: 360px; margin-top: 32px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="admin123" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { adminLogin } from '../api/auth';
import { useUserStore } from '../store/user';

const router = useRouter();
const store = useUserStore();
const loading = ref(false);
const form = reactive({ username: '', password: '' });

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码');
    return;
  }
  loading.value = true;
  try {
    const data = await adminLogin(form.username, form.password);
    store.setLogin(data.token, data.username);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (e) {
    ElMessage.error(e.message || '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}
.login-box {
  background: #fff;
  padding: 48px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
.login-box h1 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #333;
}
</style>
