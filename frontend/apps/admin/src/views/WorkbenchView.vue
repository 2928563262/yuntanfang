<template>
  <section class="workspace">
    <div class="toolbar">
      <div>
        <p class="eyebrow">权限与流程占位</p>
        <h1>{{ $route.meta.title }}</h1>
      </div>
      <el-button type="primary">新建</el-button>
    </div>

    <div class="metrics">
      <el-card v-for="metric in metrics" :key="metric.label" shadow="never">
        <span>{{ metric.label }}</span>
        <strong>{{ metric.value }}</strong>
      </el-card>
    </div>

    <el-table :data="rows" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="status" label="状态" width="140" />
      <el-table-column label="操作" width="180">
        <template #default>
          <el-button size="small">查看</el-button>
          <el-button size="small" type="primary">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup lang="ts">
const metrics = [
  { label: '商家数', value: 12 },
  { label: '用户数', value: 120 },
  { label: '摊位使用率', value: '68%' },
  { label: '投诉待处理', value: 3 }
]

const rows = [
  { id: 1, name: '示例记录', status: '待处理' },
  { id: 2, name: '公益标签审核', status: '已通过' }
]
</script>
