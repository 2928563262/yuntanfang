<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">订单 {{ order.id }}</p>
        <h1 class="page-title">发布评价</h1>
      </div>
      <RouterLink class="ghost-pill" :to="`/orders/${order.id}`">订单详情</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div v-for="metric in reviewMetrics" :key="metric" class="rating-row">
          <span>{{ metric }}</span>
          <van-rate v-model="ratings[metric]" color="#ff694f" void-icon="star" />
        </div>
        <div class="field-card">
          <label>评价内容</label>
          <textarea placeholder="分享卫生、服务、品质和性价比体验"></textarea>
        </div>
        <div class="upload-box">
          <strong>图文/视频评价</strong>
          <span>上传组件占位，后续接入对象存储</span>
        </div>
        <button class="primary-pill" type="button">提交评价</button>
      </form>

      <aside class="card">
        <h2>{{ order.stall }}</h2>
        <p class="muted">评价发布后进入内容安全审核，审核通过后展示在摊位详情。</p>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, computed } from 'vue'
import { useRoute } from 'vue-router'
import { reviewMetrics, userOrders } from '../data/mock'

const route = useRoute()
const order = computed(() => userOrders.find((item) => item.id === Number(route.params.orderId)) ?? userOrders[0])
const ratings = reactive<Record<string, number>>({
  卫生: 5,
  服务: 5,
  品质: 5,
  性价比: 5
})
</script>
