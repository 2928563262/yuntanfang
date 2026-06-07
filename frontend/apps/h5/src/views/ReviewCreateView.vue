<template>
  <main class="page review-create-page">
    <van-nav-bar title="发表评价" left-arrow fixed placeholder @click-left="router.back()" />
    <section class="checkout-card">
      <h2>{{ order.stallName }}</h2>
      <p class="form-hint">本次评价会更新演示订单状态。</p>
      <van-rate v-model="rating" color="#ff6b4a" />
      <van-field v-model="content" rows="5" autosize type="textarea" placeholder="说说摊位口味、出摊准时和服务体验" />
      <div class="upload-placeholder">图片上传占位</div>
      <van-button block round type="primary" @click="submitReview">提交评价</van-button>
    </section>
  </main>
</template>

<script setup lang="ts">
import { showToast } from 'vant'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrder, markOrderReviewed } from '../mock/demoData'

const route = useRoute()
const router = useRouter()
const order = getOrder(String(route.params.orderId))
const rating = ref(5)
const content = ref('')

function submitReview() {
  markOrderReviewed(order.id)
  showToast('评价成功')
  router.push(`/orders/${order.id}`)
}
</script>
