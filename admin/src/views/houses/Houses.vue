<template>
  <div class="houses-page">
    <h2>房源管理</h2>
    <el-table :data="list" stripe style="margin-top: 24px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="price" label="租金" width="100">
        <template #default="{ row }">{{ row.price }}元/月</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          {{ row.status === 'pending' ? '待审核' : row.status === 'approved' ? '已上架' : row.status === 'rejected' ? '已驳回' : '已下架' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
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
import request from '../../utils/request';

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
    const res = await request.get('/admin/houses', { params: { page: page.value, pageSize: pageSize.value } });
    list.value = res.list || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
}

onMounted(loadList);
</script>
