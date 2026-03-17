<template>
  <div class="dashboard">
    <h2>数据看板</h2>
    <el-row :gutter="24" style="margin-top: 24px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-label">总用户数</div>
          <div class="stat-value">{{ stats.totalUsers ?? '-' }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-label">总房源数</div>
          <div class="stat-value">{{ stats.totalHouses ?? '-' }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-label">待审核房源</div>
          <div class="stat-value">{{ stats.pendingHouses ?? '-' }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-label">今日新增(已审)</div>
          <div class="stat-value">{{ stats.todayNewHouses ?? '-' }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getStats } from '../../api/dashboard';

const stats = ref({});

onMounted(async () => {
  try {
    stats.value = await getStats();
  } catch (e) {}
});
</script>

<style scoped>
.stat-label { font-size: 14px; color: #999; }
.stat-value { font-size: 28px; font-weight: 600; margin-top: 8px; }
</style>
