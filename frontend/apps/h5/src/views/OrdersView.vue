<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">预约与订单</p>
        <h1 class="page-title">我的订单</h1>
      </div>
      <RouterLink class="ghost-pill" to="/stalls">去下单</RouterLink>
    </header>

    <section class="section list-stack">
      <RouterLink v-for="order in userOrders" :key="order.id" class="list-card" :to="`/orders/${order.id}`">
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
        </div>
      </RouterLink>
    </section>
  </main>
</template>

<script setup lang="ts">
import { userOrders } from '../data/mock'
</script>
