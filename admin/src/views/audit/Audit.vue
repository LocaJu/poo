<template>
  <div class="audit-page">
    <h2>审核管理</h2>
    <el-table :data="list" stripe style="margin-top: 24px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="price" label="租金" width="100">
        <template #default="{ row }">{{ row.price }}元/月</template>
      </el-table-column>
      <el-table-column prop="type" label="户型" width="80" />
      <el-table-column prop="createTime" label="提交时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="success" size="small" @click="handleApprove(row.id)">通过</el-button>
          <el-button type="danger" size="small" @click="handleReject(row)">驳回</el-button>
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
    <el-dialog v-model="rejectVisible" title="驳回原因" width="400px">
      <el-input v-model="rejectReason" type="textarea" rows="3" placeholder="请输入驳回原因" />
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="submitReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getPendingHouses, approveHouse, rejectHouse } from '../../api/house';

const list = ref([]);
const loading = ref(false);
const page = ref(1);
const pageSize = ref(10);
const total = ref(0);
const rejectVisible = ref(false);
const rejectReason = ref('');
const rejectId = ref(null);

function formatTime(v) {
  if (!v) return '';
  return new Date(v).toLocaleString();
}

async function loadList() {
  loading.value = true;
  try {
    const res = await getPendingHouses(page.value, pageSize.value);
    list.value = res.list || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
}

async function handleApprove(id) {
  try {
    await approveHouse(id);
    ElMessage.success('已通过');
    loadList();
  } catch (e) {
    ElMessage.error(e.message);
  }
}

function handleReject(row) {
  rejectId.value = row.id;
  rejectReason.value = '';
  rejectVisible.value = true;
}

async function submitReject() {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请输入驳回原因');
    return;
  }
  try {
    await rejectHouse(rejectId.value, rejectReason.value);
    ElMessage.success('已驳回');
    rejectVisible.value = false;
    loadList();
  } catch (e) {
    ElMessage.error(e.message);
  }
}

onMounted(loadList);
</script>
