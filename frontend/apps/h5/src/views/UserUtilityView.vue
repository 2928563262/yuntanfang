<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">{{ config.eyebrow }}</p>
        <h1 class="page-title">{{ config.title }}</h1>
      </div>
      <button class="primary-pill" type="button" @click="runAction">{{ config.action }}</button>
    </header>

    <section class="section content-grid">
      <div class="list-stack">
        <article v-for="row in rows" :key="`${row.title}-${row.status}`" class="list-card">
          <div class="list-card-header">
            <div>
              <h3>{{ row.title }}</h3>
              <p>{{ row.desc }}</p>
            </div>
            <span class="status-tag">{{ row.status }}</span>
          </div>
        </article>
      </div>

      <aside class="card">
        <h2>{{ sideTitle }}</h2>
        <p class="muted">{{ sideDesc }}</p>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { clearAuthSession } from '@yuntanfang/shared'
import { userUtilityConfigs } from '../data/mock'
import { useUserDataStore } from '../stores/userData'

const route = useRoute()
const router = useRouter()
const userData = useUserDataStore()
const config = computed(() => userUtilityConfigs[route.path] ?? userUtilityConfigs['/settings'])
const rows = computed(() => {
  if (route.path === '/my-reviews') {
    return userData.reviews.value.map((review) => ({
      title: review.stall,
      desc: `${review.rating} 星 · ${review.content}`,
      status: review.status
    }))
  }

  if (route.path === '/wallet') {
    return userData.orders.value.map((order) => ({
      title: `订单 ${order.id}`,
      desc: `${order.stall} · ¥${order.amount} · ${order.time}`,
      status: order.status === '待支付' ? '待支付' : '已记录'
    }))
  }

  if (route.path === '/footprints') {
    return userData.orders.value.slice(0, 3).map((order) => ({
      title: order.stall,
      desc: `${order.createdAt} 预约 ${order.items[0]?.name ?? '商品'}`,
      status: '订单足迹'
    }))
  }

  return config.value.rows
})
const sideTitle = computed(() => {
  if (route.path === '/my-reviews') return '评价留痕'
  if (route.path === '/wallet') return '账单概览'
  if (route.path === '/settings') return '账号操作'
  return '服务说明'
})
const sideDesc = computed(() => {
  if (route.path === '/my-reviews') return '评价提交后会同步到摊位详情，用于用户决策和商家服务改进。'
  if (route.path === '/wallet') return '当前展示预约订单支付记录，后续接入真实支付、退款和发票接口。'
  if (route.path === '/settings') return '退出登录会清除当前测试登录态，返回登录页重新选择身份。'
  return '当前页已接入用户端基础信息，后续按接口替换为服务端数据。'
})

function runAction() {
  if (route.path === '/settings') {
    clearAuthSession()
    router.push('/login')
    return
  }
  if (route.path === '/my-reviews') {
    router.push('/orders')
    return
  }
  if (route.path === '/feedback') {
    router.push('/complaints/create')
  }
}
</script>
