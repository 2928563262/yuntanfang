<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">预约订单处理</p>
        <h1 class="page-title">订单处理</h1>
      </div>
      <RouterLink class="ghost-pill" to="/vendor/dashboard">工作台</RouterLink>
    </header>

    <section class="section list-stack">
      <article v-for="order in orders" :key="order.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ order.stallName ?? ('摊位#' + (order.stallId ?? '-')) }}</h3>
            <p>下单人 {{ order.contactPhone ?? ('用户#' + order.userId) }}</p>
          </div>
          <span class="status-tag">{{ zh(order.orderStatus) }}</span>
        </div>
        <div class="order-items">
          <span v-for="item in normalizeItems(order.items)" :key="item.key">{{ item.text }}</span>
        </div>
        <div class="meta-row">
          <span>订单号 {{ order.id }}</span>
          <span>取货 {{ order.pickupTime ?? '-' }}</span>
          <span>¥{{ order.totalAmount != null ? Number(order.totalAmount).toFixed(2) : '0.00' }}</span>
        </div>
        <p v-if="errorMap[order.id]" class="form-error">{{ errorMap[order.id] }}</p>
        <div class="action-grid">
          <button class="ghost-pill" type="button" :disabled="!canSet(order.orderStatus, 'accepted')" @click="setStatus(order.id, 'accepted')">接单</button>
          <button class="ghost-pill" type="button" :disabled="!canSet(order.orderStatus, 'cancelled')" @click="setStatus(order.id, 'cancelled')">拒单</button>
          <button class="ghost-pill" type="button" :disabled="!canSet(order.orderStatus, 'preparing')" @click="setStatus(order.id, 'preparing')">备货中</button>
          <button class="primary-pill" type="button" :disabled="!canSet(order.orderStatus, 'completed')" @click="setStatus(order.id, 'completed')">完成</button>
        </div>
      </article>
      <article v-if="!loading && orders.length === 0" class="list-card">
        <h3>暂无订单</h3>
        <p>消费者下单后会实时出现在这里（用 test1 下单，再用 test2 登录查看）。</p>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const orders = ref<any[]>([])
const loading = ref(false)
const errorMap = ref<Record<number, string>>({})

const statusMap: Record<string, string> = {
  created: '待接单',
  accepted: '备货中',
  preparing: '备货中',
  ready: '待取餐',
  completed: '已完成',
  reviewed: '已完成',
  cancelled: '已取消'
}
const zh = (s: string) => statusMap[s] ?? s

async function load() {
  loading.value = true
  try {
    const res = await vendorApi.orders()
    orders.value = res.data.data?.records ?? []
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
}

async function setStatus(id: number, status: string) {
  errorMap.value[id] = ''
  try {
    await vendorApi.updateOrderStatus(id, status)
    await load()
  } catch (err: any) {
    errorMap.value[id] = err?.response?.data?.message ?? '状态更新失败'
  }
}

function canSet(current: string, next: string) {
  const transitions: Record<string, string[]> = {
    created: ['accepted', 'cancelled'],
    accepted: ['preparing', 'cancelled'],
    preparing: ['completed'],
    ready: ['completed'],
    completed: [],
    reviewed: [],
    cancelled: []
  }
  return (transitions[current] ?? []).includes(next)
}

function normalizeItems(items: unknown) {
  if (!Array.isArray(items) || items.length === 0) {
    return [{ key: 'empty', text: '商品明细：暂无' }]
  }
  return items.map((item: any, index: number) => {
    const name = item.productName ?? `商品#${item.productId ?? index + 1}`
    const quantity = Number(item.quantity ?? 1)
    const price = item.price != null ? ` ¥${Number(item.price).toFixed(2)}` : ''
    return {
      key: `${item.id ?? index}-${name}`,
      text: `${name} x${quantity}${price}`
    }
  })
}

onMounted(load)
</script>
