<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" type="button" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" to="/vendor/orders">订单列表</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">订单 {{ order.id }}</p>
      <h1>{{ order.stallName || '订单详情' }}</h1>
      <p>{{ statusText(order.orderStatus) }} · 取货 {{ order.pickupTime || '-' }}</p>
      <div class="hero-actions">
        <button class="ghost-pill" type="button" :disabled="!canSet('accepted')" @click="setStatus('accepted')">接单</button>
        <button class="ghost-pill" type="button" :disabled="!canSet('preparing')" @click="setStatus('preparing')">备货中</button>
        <button class="primary-pill" type="button" :disabled="!canSet('completed')" @click="setStatus('completed')">完成</button>
      </div>
      <p v-if="error" class="form-error">{{ error }}</p>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <article class="card">
          <div class="section-head">
            <h2>商品明细</h2>
            <span class="status-tag">¥{{ amount }}</span>
          </div>
          <div class="list-stack">
            <div v-for="item in items" :key="item.id" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ item.productName || `商品 #${item.productId}` }}</h3>
                  <p>数量 x{{ item.quantity }}</p>
                </div>
                <strong>¥{{ money(item.price) }}</strong>
              </div>
            </div>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>取货与联系</h2>
          </div>
          <div class="meta-row">
            <span>下单用户 #{{ order.userId || '-' }}</span>
            <span>联系方式 {{ order.contactPhone || '-' }}</span>
            <span>备注 {{ order.remark || '-' }}</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>操作记录</h2>
        <div class="timeline">
          <div v-for="log in statusLogs" :key="log.id" class="timeline-item">
            <span></span>
            <strong>{{ statusText(log.orderStatus) }}</strong>
            <small>{{ log.createdAt || '-' }}</small>
          </div>
          <p v-if="statusLogs.length === 0" class="muted">暂无状态记录</p>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { vendorApi } from '@yuntanfang/api'

const route = useRoute()
const order = ref<any>({})
const items = ref<any[]>([])
const statusLogs = ref<any[]>([])
const error = ref('')

const statusMap: Record<string, string> = {
  created: '待接单',
  accepted: '已接单',
  preparing: '备货中',
  ready: '待取餐',
  completed: '已完成',
  reviewed: '已评价',
  cancelled: '已取消'
}
const transitions: Record<string, string[]> = {
  created: ['accepted', 'cancelled'],
  accepted: ['preparing', 'cancelled'],
  preparing: ['completed'],
  ready: ['completed']
}

const amount = computed(() => money(order.value.totalAmount))
function money(value: unknown) {
  return value == null ? '0.00' : Number(value).toFixed(2)
}
function statusText(status: string) {
  return statusMap[status] ?? status
}
function canSet(next: string) {
  return (transitions[order.value.orderStatus] ?? []).includes(next)
}

async function load() {
  const res = await vendorApi.orderDetail(route.params.id as string)
  const data = res.data.data ?? {}
  order.value = data.order ?? {}
  items.value = data.items ?? []
  statusLogs.value = data.statusLogs ?? []
}

async function setStatus(status: string) {
  error.value = ''
  try {
    await vendorApi.updateOrderStatus(order.value.id, status)
    await load()
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '状态更新失败'
  }
}

onMounted(load)
</script>
