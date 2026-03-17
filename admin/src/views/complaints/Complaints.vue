<template>
  <div class="complaints-page">
    <h2>举报处理</h2>
    <el-radio-group v-model="filterStatus" style="margin-top: 16px" @change="loadList">
      <el-radio-button label="">全部</el-radio-button>
      <el-radio-button label="pending">待处理</el-radio-button>
      <el-radio-button label="resolved">已处理</el-radio-button>
    </el-radio-group>
    <el-table :data="list" stripe style="margin-top: 24px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="targetType" label="类型" width="80">
        <template #default="{ row }">{{ row.targetType === 'house' ? '房源' : '用户' }}</template>
      </el-table-column>
      <el-table-column prop="targetId" label="目标ID" width="80" />
      <el-table-column prop="reason" label="原因" width="120" />
      <el-table-column prop="description" label="描述" min-width="160" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          {{ row.status === 'pending' ? '待处理' : row.status === 'resolved' ? '已处理' : '已拒绝' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="举报时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="120" v-if="filterStatus === 'pending' || filterStatus === ''">
        <template #default="{ row }">
          <el-button v-if="row.status === 'pending'" type="primary" size="small" @click="handleResolve(row)">
            处理
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
    <el-dialog v-model="resolveVisible" title="处理举报" width="400px">
      <el-input v-model="resolveResult" type="textarea" rows="3" placeholder="处理结果说明" />
      <template #footer>
        <el-button @click="resolveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResolve">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { getComplaintList, resolveComplaint } from '../../api/complaint';

const list = ref([]);
const loading = ref(false);
const page = ref(1);
const pageSize = ref(10);
const total = ref(0);
const filterStatus = ref('pending');
const resolveVisible = ref(false);
const resolveResult = ref('');
const currentRow = ref(null);

function formatTime(v) {
  if (!v) return '';
  return new Date(v).toLocaleString();
}

async function loadList() {
  loading.value = true;
  try {
    const res = await getComplaintList(filterStatus.value, page.value, pageSize.value);
    list.value = res.list || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
}

function handleResolve(row) {
  currentRow.value = row;
  resolveResult.value = '';
  resolveVisible.value = true;
}

async function submitResolve() {
  if (!currentRow.value) return;
  try {
    await resolveComplaint(currentRow.value.id, resolveResult.value);
    ElMessage.success('已处理');
    resolveVisible.value = false;
    loadList();
  } catch (e) {
    ElMessage.error(e.message);
  }
}

watch(filterStatus, () => { page.value = 1; loadList(); });
onMounted(loadList);
</script>
