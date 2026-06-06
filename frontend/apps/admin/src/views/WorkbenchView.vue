<template>
  <section class="workspace">
    <div class="toolbar">
      <div>
        <p class="eyebrow">{{ config.eyebrow }}</p>
        <h1>{{ config.title }}</h1>
      </div>
      <el-button type="primary">{{ config.primaryAction }}</el-button>
    </div>

    <div class="metrics">
      <el-card v-for="metric in config.metrics" :key="metric.label" shadow="never">
        <span>{{ metric.label }}</span>
        <strong>{{ metric.value }}</strong>
        <small>{{ metric.hint }}</small>
      </el-card>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form inline>
        <el-form-item label="关键词">
          <el-input placeholder="搜索名称、提交人、编号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select model-value="" placeholder="全部状态" clearable>
            <el-option v-for="filter in config.filters" :key="filter" :label="filter" :value="filter" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
          <el-button>重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="table-head">
          <strong>{{ config.title }}列表</strong>
          <div>
            <el-button size="small">导出</el-button>
            <el-button size="small" type="primary">批量处理</el-button>
          </div>
        </div>
      </template>
      <el-table :data="config.rows" border>
        <el-table-column v-for="column in config.columns" :key="column.prop" :prop="column.prop" :label="column.label" :width="column.width" />
        <el-table-column label="操作" width="210" fixed="right">
          <template #default>
            <el-button size="small">详情</el-button>
            <el-button size="small" type="primary">处理</el-button>
            <el-button size="small">记录</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-row">
        <el-pagination layout="prev, pager, next" :total="80" />
      </div>
    </el-card>

    <el-card v-if="config.module === 'dashboard'" shadow="never" class="flow-card">
      <template #header>待办流程</template>
      <div class="flow-grid">
        <div v-for="item in flows" :key="item.title" class="flow-item">
          <span>{{ item.title }}</span>
          <strong>{{ item.count }}</strong>
          <small>{{ item.hint }}</small>
        </div>
      </div>
    </el-card>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { getAdminModuleConfig } from '../data/mock'

const route = useRoute()
const config = computed(() => getAdminModuleConfig(route.path))

const flows = [
  { title: '商家入驻审核', count: 8, hint: '待线上初审' },
  { title: '资质材料审核', count: 21, hint: '含 6 条需补充' },
  { title: '投诉工单', count: 5, hint: '1 条临近超时' },
  { title: '内容审核', count: 32, hint: '商品与故事' }
]
</script>
