<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink class="ghost-pill" :to="`/reviews/create/${order.id}`">评价</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">订单 {{ order.id }}</p>
      <h1>{{ order.status }}</h1>
      <p>{{ order.stall }} · 取货时间 {{ order.time }}</p>
      <div class="hero-actions">
        <button class="primary-pill">联系摊主</button>
        <button class="ghost-pill">一键导航</button>
      </div>
    </section>

    <section class="section content-grid">
      <div class="list-stack">
        <article class="card">
          <div class="section-head">
            <h2>商品明细</h2>
            <span class="status-tag">¥{{ order.amount }}</span>
          </div>
          <div class="list-stack">
            <div v-for="item in order.items" :key="item.name" class="list-card">
              <div class="list-card-header">
                <div>
                  <h3>{{ item.name }}</h3>
                  <p>数量 x{{ item.quantity }}</p>
                </div>
                <strong>¥{{ item.price }}</strong>
              </div>
            </div>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>取货信息</h2>
          </div>
          <div class="meta-row">
            <span>{{ order.address }}</span>
            <span>{{ order.contact }}</span>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>支付/退款</h2>
            <span class="status-tag">{{ payment.status }}</span>
          </div>
          <div class="meta-row">
            <span>{{ payment.type }}</span>
            <span>金额 ¥{{ payment.amount }}</span>
            <span>{{ payment.updatedAt }}</span>
          </div>
          <div class="action-grid section">
            <button class="primary-pill">H5 支付</button>
            <button class="ghost-pill">申请退款</button>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>状态流转</h2>
        <div class="timeline">
          <div v-for="step in order.timeline" :key="step" class="timeline-item">
            <span></span>
            <strong>{{ step }}</strong>
          </div>
        </div>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { paymentRecords, userOrders } from '../data/mock'

const route = useRoute()
const order = computed(() => userOrders.find((item) => item.id === Number(route.params.id)) ?? userOrders[0])
const payment = computed(() => paymentRecords.find((item) => item.orderId === order.value.id) ?? paymentRecords[0])
</script>
