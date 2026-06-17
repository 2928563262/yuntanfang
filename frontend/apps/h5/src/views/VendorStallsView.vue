<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">摊位预约与出摊</p>
        <h1 class="page-title">摊位预约</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/dashboard">工作台</RouterLink>
    </header>

    <section class="section metric-grid">
      <div class="card metric-card"><strong>{{ reservations.length }}</strong><span>预约总数</span></div>
      <div class="card metric-card"><strong>{{ countBy('pending') }}</strong><span>待审批</span></div>
      <div class="card metric-card"><strong>{{ countBy('approved') }}</strong><span>已通过</span></div>
      <div class="card metric-card"><strong>{{ countBy('rejected') }}</strong><span>已驳回</span></div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="item in reservations" :key="item.id" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ item.stallName || `摊位 #${item.stallId}` }}</h3>
              <p>预约编号 {{ item.id }} · 摊位编号 {{ item.stallId }}</p>
            </div>
            <span class="status-tag" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</span>
          </div>
          <div class="meta-row">
            <span v-if="item.status === 'approved'">审批通过，摊位已在用户端展示</span>
            <span v-else-if="item.status === 'rejected'">预约被驳回：{{ item.rejectReason || '可重新预约' }}</span>
            <span v-else>等待后台审批中</span>
          </div>
        </article>
        <article v-if="!loading && reservations.length === 0" class="list-card">
          <h3>还没有摊位预约</h3>
          <p>用右侧表单发起预约，审批通过后摊位即对用户开放。</p>
        </article>
      </div>

      <aside class="card form-list">
        <h2>发起预约</h2>
        <p class="muted">输入要预约的摊位编号（由运营在后台分配的空闲摊位）。</p>
        <div class="field-card">
          <label>摊位编号</label>
          <input v-model.number="stallId" type="number" min="1" placeholder="如：3" />
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="reserve">
          {{ submitting ? '提交中…' : '提交预约' }}
        </button>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const reservations = ref<any[]>([])
const loading = ref(false)
const stallId = ref<number | null>(null)
const error = ref('')
const submitting = ref(false)

function statusLabel(status: string) {
  return status === 'approved' ? '已通过' : status === 'rejected' ? '已驳回' : '待审批'
}
function statusClass(status: string) {
  return status === 'approved' ? 'tag-success' : status === 'rejected' ? 'tag-danger' : 'tag-pending'
}
function countBy(status: string) {
  return reservations.value.filter((r) => r.status === status).length
}

async function load() {
  loading.value = true
  try {
    const res = await vendorApi.reservations()
    reservations.value = res.data.data?.records ?? []
  } catch {
    reservations.value = []
  } finally {
    loading.value = false
  }
}

async function reserve() {
  error.value = ''
  if (!stallId.value) {
    error.value = '请输入摊位编号'
    return
  }
  submitting.value = true
  try {
    await vendorApi.reserve({ stallId: stallId.value })
    stallId.value = null
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '预约失败，请确认已通过入驻申请'
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.tag-success { background: #e7f7ec; color: #18a058; }
.tag-danger { background: #fdeceb; color: #d03050; }
.tag-pending { background: #fff4e5; color: #f0883a; }
.muted { color: #888; font-size: 13px; }
</style>
