<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">预约与订单</p>
        <h1 class="page-title">我的订单</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">去下单</RouterLink>
    </header>

    <section class="section chip-row">
      <button v-for="tab in tabs" :key="tab" class="chip" :class="{ active: activeTab === tab }" @click="activeTab = tab">{{ tab }}</button>
    </section>

    <section class="section list-stack">
      <RouterLink v-for="order in filteredOrders" :key="order.id" class="list-card" :to="`/orders/${order.id}`">
        <div class="list-card-header">
          <div>
            <h3>{{ order.stall }}</h3>
            <p>取货时间：{{ order.time }}</p>
          </div>
          <span class="status-tag">{{ order.status }}</span>
        </div>
        <div class="order-items">
          <span v-for="item in order.items" :key="item.key">{{ item.text }}</span>
        </div>
        <div class="meta-row">
          <span>订单号 {{ order.id }}</span>
          <span>金额 ¥{{ order.amount }}</span>
        </div>
      </RouterLink>
      <article v-if="!loading && filteredOrders.length === 0" class="list-card">
        <h3>暂无订单</h3>
        <p>去附近摊位预约一单，订单会出现在这里。</p>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { orderApi } from '@yuntanfang/api'

const tabs = ['全部', '待接单', '备货中', '待取餐', '已完成']
const activeTab = ref('全部')
const orders = ref<any[]>([])
const loading = ref(false)

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
    const res = await orderApi.my(1, 50)
    orders.value = res.data.data?.records ?? []
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
}

onMounted(load)

const filteredOrders = computed(() => {
  const list = orders.value.map((o) => ({
    id: o.id,
    stall: o.stallName ?? `摊位#${o.stallId ?? '-'}`,
    time: o.pickupTime ?? '-',
    status: zh(o.orderStatus),
    amount: o.totalAmount != null ? Number(o.totalAmount).toFixed(2) : '0.00',
    items: normalizeItems(o.items)
  }))
  if (activeTab.value === '全部') {
    return list
  }
  return list.filter((o) => o.status === activeTab.value)
})

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
</script>
