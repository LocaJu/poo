<template>
  <div class="users-page">
    <h2>用户管理</h2>
    <el-table :data="list" stripe style="margin-top: 24px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="nickName" label="昵称" width="120" />
      <el-table-column prop="openId" label="OpenID" min-width="140" show-overflow-tooltip />
      <el-table-column prop="isVerified" label="认证" width="80">
        <template #default="{ row }">{{ row.isVerified ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">{{ row.status === 0 ? '正常' : '禁用' }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button
            :type="row.status === 0 ? 'warning' : 'success'"
            size="small"
            @click="toggleStatus(row)"
          >
            {{ row.status === 0 ? '禁用' : '启用' }}
          </el-button>
          <el-button size="small" @click="toggleVerify(row)">
            {{ row.isVerified ? '取消认证' : '通过认证' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px"
      @current-change="loadList"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getUserList, setUserStatus, setUserVerified } from '../../api/user';

const list = ref([]);
const loading = ref(false);
const page = ref(1);
const pageSize = ref(10);
const total = ref(0);

function formatTime(v) {
  if (!v) return '';
  return new Date(v).toLocaleString();
}

async function loadList() {
  loading.value = true;
  try {
    const res = await getUserList(page.value, pageSize.value);
    list.value = res.list || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
}

async function toggleStatus(row) {
  const status = row.status === 0 ? 1 : 0;
  try {
    await setUserStatus(row.id, status);
    ElMessage.success(status === 1 ? '已禁用' : '已启用');
    loadList();
  } catch (e) {
    ElMessage.error(e.message);
  }
}

async function toggleVerify(row) {
  const verified = !row.isVerified;
  try {
    await setUserVerified(row.id, verified);
    ElMessage.success(verified ? '已通过认证' : '已取消认证');
    loadList();
  } catch (e) {
    ElMessage.error(e.message);
  }
}

onMounted(loadList);
</script>
