<template>
  <main class="page">
    <section class="hero-card">
      <p class="eyebrow">商家工作台</p>
      <h1>默林的广东肠粉早餐店</h1>
      <p>处理入驻、资质、摊位预约、商品、订单和评价回复。</p>
      <div class="hero-actions">
        <RouterLink class="primary-pill" to="/vendor/apply">完善入驻</RouterLink>
        <RouterLink class="ghost-pill" to="/vendor/orders">处理订单</RouterLink>
      </div>
    </section>

    <section class="section metric-grid">
      <div v-for="task in vendorTasks" :key="task.title" class="card metric-card">
        <strong>{{ task.value }}</strong>
        <span>{{ task.title }}</span>
      </div>
    </section>

    <section class="section action-grid">
      <RouterLink v-for="item in vendorMenu" :key="item.path" class="list-card" :to="item.path">
        <h3>{{ item.title }}</h3>
        <p>{{ item.desc }}</p>
      </RouterLink>
    </section>
  </main>
</template>

<script setup lang="ts">
import { vendorMenu, vendorTasks } from '../data/mock'
</script>
