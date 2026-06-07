<template>
  <main class="page order-detail-page">
    <van-nav-bar title="订单详情" left-arrow fixed placeholder @click-left="router.back()" />
    <section class="order-status-card">
      <p>{{ statusText(order.status) }}</p>
      <h1>取餐码 {{ order.pickupCode }}</h1>
      <span>{{ order.pickupTime }} 到 {{ order.stallName }} 自提</span>
    </section>
    <section class="checkout-card">
      <h2>商品清单</h2>
      <article v-for="item in order.items" :key="item.productId" class="checkout-item">
        <span>{{ item.name }} ×{{ item.quantity }}</span>
        <strong>¥{{ item.price * item.quantity }}</strong>
      </article>
      <div class="checkout-total">实付款 ¥{{ order.amount }}</div>
    </section>
    <section class="timeline-panel">
      <h2>订单跟踪</h2>
      <div class="flow-list">
        <div v-for="step in steps" :key="step.title">
          <span>{{ step.no }}</span>
          <strong>{{ step.title }}</strong>
          <p>{{ step.text }}</p>
        </div>
      </div>
    </section>
    <section class="order-detail-actions">
      <button type="button">联系商家</button>
      <button type="button">申请退款</button>
      <button v-if="order.status === 'review'" class="primary" type="button" @click="router.push(`/reviews/create/${order.id}`)">去评价</button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { type DemoOrder, getOrder } from '../mock/demoData'

const route = useRoute()
const router = useRouter()
const order = getOrder(String(route.params.id))
const steps = [
  { no: '01', title: '订单提交', text: order.createdAt },
  { no: '02', title: '商家备餐', text: '摊主已收到预约信息。' },
  { no: '03', title: '到店自提', text: `凭取餐码 ${order.pickupCode} 取餐。` }
]

function statusText(status: DemoOrder['status']) {
  return ({ preparing: '商家备餐中', pickup: '待取货', review: '待评价', refund: '退款处理中', done: '已完成' })[status]
}
</script>
