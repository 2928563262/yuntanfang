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
      <article v-for="order in vendorOrders" :key="order.id" class="list-card">
        <div class="list-card-header">
          <div>
            <h3>{{ order.buyer }}</h3>
            <p>{{ order.items }}</p>
          </div>
          <span class="status-tag">{{ order.status }}</span>
        </div>
        <div class="meta-row">
          <span>订单号 {{ order.id }}</span>
          <span>取货 {{ order.pickupTime }}</span>
          <span>¥{{ order.amount }}</span>
        </div>
        <div class="action-grid">
          <button class="ghost-pill">拒单</button>
          <button class="ghost-pill">备货中</button>
          <button class="primary-pill">接单</button>
        </div>
      </article>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorOrders } from '../data/mock'
</script>
