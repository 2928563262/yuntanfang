<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">投诉举报</p>
        <h1 class="page-title">我的投诉</h1>
      </div>
      <RouterLink class="primary-pill" to="/complaints/create">提交投诉</RouterLink>
    </header>

    <section class="section list-stack">
      <article v-for="item in complaints" :key="item.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ item.targetName || item.vendorName || (item.vendorId ? ('摊主#' + item.vendorId) : '平台投诉') }}</h3>
            <p>{{ item.description }}</p>
          </div>
          <span class="status-tag">{{ statusText(item.status) }}</span>
        </div>
        <div class="meta-row">
          <span>工单 {{ item.id }}</span>
          <span>{{ item.complaintType || '服务问题' }}</span>
          <span v-if="item.orderId">订单 {{ item.orderId }}</span>
          <span>{{ item.updatedAt || item.createdAt }}</span>
        </div>
        <p v-if="item.processResult" class="muted">处理结果：{{ item.processResult }}</p>
      </article>
      <article v-if="!loading && complaints.length === 0" class="list-card">
        <h3>暂无投诉</h3>
        <p>遇到问题可以点右上角「提交投诉」。</p>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { complaintApi } from '@yuntanfang/api'

const complaints = ref<any[]>([])
const loading = ref(false)

const statusZh: Record<string, string> = {
  submitted: '已提交',
  assigned: '已分派',
  processing: '处理中',
  processed: '已处理',
  closed: '已关闭'
}
const statusText = (s: string) => statusZh[s] ?? s

async function load() {
  loading.value = true
  try {
    const res = await complaintApi.my()
    complaints.value = res.data.data?.records ?? []
  } catch {
    complaints.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
