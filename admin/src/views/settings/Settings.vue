<template>
  <div class="settings-page">
    <h2>系统设置</h2>
    <el-card style="margin-top: 24px; max-width: 600px">
      <template #header>基础配置</template>
      <p>敏感词、审核开关、轮播图等配置可在后续版本中接入。</p>
      <el-form label-width="120">
        <el-form-item label="审核开关">
          <el-switch v-model="auditEnabled" />
          <span style="margin-left: 8px">开启后新发布房源需审核</span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const auditEnabled = ref(true);
</script>
