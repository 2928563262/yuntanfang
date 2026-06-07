<template>
  <main class="page">
    <header class="page-header">
      <button class="ghost-pill" @click="$router.back()">返回</button>
      <RouterLink v-if="order.status === '已完成'" class="ghost-pill" :to="`/reviews/create/${order.id}`">评价</RouterLink>
      <RouterLink v-else class="ghost-pill" to="/orders">订单列表</RouterLink>
    </header>

    <section class="hero-card">
      <p class="eyebrow">订单 {{ order.id }}</p>
      <h1>{{ order.status }}</h1>
      <p>{{ order.stall }} · 取货时间 {{ order.time }}</p>
      <div class="hero-actions">
        <button v-if="order.status === '待支付'" class="primary-pill" type="button" @click="pay">立即支付</button>
        <button v-if="order.status === '待取餐'" class="primary-pill" type="button" @click="complete">确认取餐</button>
        <RouterLink v-if="order.status === '已完成'" class="primary-pill" :to="`/reviews/create/${order.id}`">发布评价</RouterLink>
        <button class="ghost-pill" type="button">联系摊主</button>
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
            <span>取餐码 {{ order.pickupCode }}</span>
          </div>
        </article>

        <article class="card">
          <div class="section-head">
            <h2>支付/退款</h2>
            <span class="status-tag">{{ paymentStatus }}</span>
          </div>
          <div class="meta-row">
            <span>{{ payment.type }}</span>
            <span>金额 ¥{{ payment.amount }}</span>
            <span>{{ payment.updatedAt }}</span>
          </div>
          <div class="action-grid section">
            <button class="primary-pill" type="button" :disabled="order.status !== '待支付'" @click="pay">H5 支付</button>
            <button class="ghost-pill" type="button" :disabled="!canRefund" @click="refund">申请退款</button>
            <button class="ghost-pill" type="button" :disabled="!canCancel" @click="cancel">取消订单</button>
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
import { paymentRecords } from '../data/mock'
import { useUserDataStore } from '../stores/userData'

const route = useRoute()
const userData = useUserDataStore()
const order = computed(() => userData.findOrder(Number(route.params.id)))
const payment = computed(() => {
  const record = paymentRecords.find((item) => item.orderId === order.value.id)
  return record ?? {
    id: order.value.id,
    orderId: order.value.id,
    type: 'H5 支付',
    status: paymentStatus.value,
    amount: order.value.amount,
    updatedAt: order.value.createdAt
  }
})
const paymentStatus = computed(() => {
  if (order.value.status === '待支付') {
    return '待支付'
  }
  if (order.value.status === '退款中') {
    return '退款中'
  }
  if (order.value.status === '已取消') {
    return '已关闭'
  }
  return '已支付'
})
const canCancel = computed(() => ['待支付', '待接单'].includes(order.value.status))
const canRefund = computed(() => ['待取餐', '已完成'].includes(order.value.status))

function pay() {
  userData.payOrder(order.value.id)
}

function complete() {
  userData.completeOrder(order.value.id)
}

function cancel() {
  userData.cancelOrder(order.value.id)
}

function refund() {
  userData.refundOrder(order.value.id)
}
</script>
