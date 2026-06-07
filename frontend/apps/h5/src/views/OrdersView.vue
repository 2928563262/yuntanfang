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
        <div class="meta-row">
          <span>订单号 {{ order.id }}</span>
          <span>金额 ¥{{ order.amount }}</span>
          <span>{{ order.items.map((item) => `${item.name} x${item.quantity}`).join('、') }}</span>
        </div>
      </RouterLink>
      <article v-if="filteredOrders.length === 0" class="list-card">
        <h3>暂无订单</h3>
        <p>去附近摊位预约一单，订单会出现在这里。</p>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useUserDataStore } from '../stores/userData'

const userData = useUserDataStore()
const tabs = ['全部', '待支付', '待取餐', '已完成', '售后']
const activeTab = ref('全部')
const filteredOrders = computed(() => {
  if (activeTab.value === '全部') {
    return userData.orders.value
  }
  if (activeTab.value === '售后') {
    return userData.orders.value.filter((order) => ['已取消', '退款中'].includes(order.status))
  }
  return userData.orders.value.filter((order) => order.status === activeTab.value)
})
</script>
