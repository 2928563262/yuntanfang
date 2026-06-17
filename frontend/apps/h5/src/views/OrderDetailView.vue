<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink v-if="isCompleted" class="ghost-pill" :to="`/reviews/create/${order.id}`">评价</RouterLink>
      <RouterLink v-else class="ghost-pill" to="/orders">订单列表</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">订单 {{ order.id }}</p>
      <h1>{{ statusText(order.orderStatus) }}</h1>
      <p>{{ order.stallName }} · 取货时间 {{ order.pickupTime }}</p>
      <div class="hero-actions">
        <RouterLink v-if="isCompleted" class="primary-pill" :to="`/reviews/create/${order.id}`">发布评价</RouterLink>
        <RouterLink class="ghost-pill" :to="{ path: '/complaints/create', query: { orderId: order.id, vendorId: order.vendorId, stallName: order.stallName } }">提交投诉</RouterLink>
        <button class="ghost-pill" type="button">联系摊主</button>
      </div>
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
                  <h3>{{ item.productName ?? ('商品#' + item.productId) }}</h3>
                  <p>数量 x{{ item.quantity }}</p>
                </div>
                <strong>¥{{ item.price ?? '-' }}</strong>
              </div>
            </div>
            <p v-if="items.length === 0" class="muted">无商品明细</p>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>取货信息</h2>
          </div>
          <div class="meta-row">
            <span>{{ order.stallName }}</span>
            <span>联系方式 {{ order.contactPhone ?? '-' }}</span>
            <span>取货时间 {{ order.pickupTime ?? '-' }}</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>状态流转</h2>
        <div class="timeline">
          <div v-for="log in statusLogs" :key="log.id" class="timeline-item">
            <span></span>
            <strong>{{ statusText(log.orderStatus) }}</strong>
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
import { orderApi } from '@yuntanfang/api'

const route = useRoute()
const order = ref<any>({})
const items = ref<any[]>([])
const statusLogs = ref<any[]>([])

const statusZh: Record<string, string> = {
  created: '待接单',
  accepted: '备货中',
  preparing: '备货中',
  ready: '待取餐',
  completed: '已完成',
  reviewed: '已评价',
  cancelled: '已取消'
}
const statusText = (s: string) => statusZh[s] ?? s
const isCompleted = computed(() => ['completed', 'reviewed'].includes(order.value.orderStatus))
const amount = computed(() => (order.value.totalAmount != null ? Number(order.value.totalAmount).toFixed(2) : '0.00'))

async function load() {
  try {
    const res = await orderApi.detail(route.params.id as string)
    const data = res.data.data ?? {}
    order.value = data.order ?? {}
    items.value = data.items ?? []
    statusLogs.value = data.statusLogs ?? []
  } catch {
    order.value = {}
  }
}

onMounted(load)
</script>
