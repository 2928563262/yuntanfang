<template>
  <main class="page orders-page">
    <van-nav-bar title="我的订单" fixed placeholder />
    <van-tabs v-model:active="active" sticky color="#ff6b4a">
      <van-tab v-for="tab in tabs" :key="tab.value" :title="tab.label">
        <section class="order-stack order-page-list">
          <article v-for="order in filteredOrders(tab.value)" :key="order.id" class="order-card">
            <div class="order-top">
              <strong>{{ order.stallName }}</strong>
              <van-tag :type="tagType(order.status)">{{ statusText(order.status) }}</van-tag>
            </div>
            <div class="order-body">
              <img :src="order.items[0]?.image" :alt="order.stallName" />
              <div>
                <p>{{ order.items.map((item) => `${item.name}×${item.quantity}`).join('、') }}</p>
                <span>{{ order.pickupTime }} · 取餐码 {{ order.pickupCode }}</span>
                <strong>实付款 ¥{{ order.amount }}</strong>
              </div>
            </div>
            <div class="order-actions">
              <button type="button" @click="router.push(`/stalls/${order.stallId}`)">再来一单</button>
              <button v-if="order.status === 'review'" class="primary" type="button" @click="router.push(`/reviews/create/${order.id}`)">去评价</button>
              <button class="primary" type="button" @click="router.push(`/orders/${order.id}`)">查看详情</button>
            </div>
          </article>
          <van-empty v-if="filteredOrders(tab.value).length === 0" description="暂无订单" />
        </section>
      </van-tab>
    </van-tabs>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { type DemoOrder, readOrders } from '../mock/demoData'

const router = useRouter()
const active = ref(0)
const orders = ref(readOrders())
const tabs = [
  { label: '全部', value: 'all' },
  { label: '备餐中', value: 'preparing' },
  { label: '待取货', value: 'pickup' },
  { label: '待评价', value: 'review' },
  { label: '退款', value: 'refund' }
]

function filteredOrders(status: string) {
  return status === 'all' ? orders.value : orders.value.filter((order) => order.status === status)
}

function statusText(status: DemoOrder['status']) {
  return ({ preparing: '备餐中', pickup: '待取货', review: '待评价', refund: '退款中', done: '已完成' })[status]
}

function tagType(status: DemoOrder['status']) {
  return status === 'pickup' ? 'primary' : status === 'review' || status === 'preparing' ? 'warning' : status === 'refund' ? 'danger' : 'success'
}
</script>
