<template>
  <main class="page">
    <section class="hero-card">
      <p class="eyebrow">商家工作台</p>
      <h1>{{ vendorName || '我的摊位' }}</h1>
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
import { computed, onMounted, ref } from 'vue'
import { vendorApi } from '@yuntanfang/api'

const vendorName = ref('')
const orders = ref<any[]>([])
const products = ref<any[]>([])

const vendorTasks = computed(() => [
  { title: '全部订单', value: orders.value.length },
  { title: '待接单', value: orders.value.filter((o) => o.orderStatus === 'created').length },
  { title: '已完成', value: orders.value.filter((o) => ['completed', 'reviewed'].includes(o.orderStatus)).length },
  { title: '在售商品', value: products.value.length }
])

const vendorMenu = [
  { path: '/vendor/apply', title: '入驻申请', desc: '提交资料并完善摊位信息' },
  { path: '/vendor/qualifications', title: '资质管理', desc: '上传与维护经营资质' },
  { path: '/vendor/stalls', title: '摊位预约', desc: '预约摊位与出摊计划' },
  { path: '/vendor/products', title: '商品管理', desc: '上架商品、维护价格' },
  { path: '/vendor/orders', title: '订单处理', desc: '接单、备货、完成' },
  { path: '/vendor/reviews', title: '评价回复', desc: '回复顾客评价' }
]

onMounted(async () => {
  try {
    const me = await vendorApi.me()
    vendorName.value = me.data.data?.vendorName ?? ''
  } catch {
    vendorName.value = ''
  }
  try {
    const o = await vendorApi.orders()
    orders.value = o.data.data?.records ?? []
  } catch {
    orders.value = []
  }
  try {
    const p = await vendorApi.products()
    products.value = p.data.data?.records ?? []
  } catch {
    products.value = []
  }
})
</script>
