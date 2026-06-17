<template>
  <main class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">订单 {{ orderId }}</p>
        <h1 class="page-title">发布评价</h1>
      </div>
      <RouterLink class="ghost-pill" :to="`/orders/${orderId}`">订单详情</RouterLink>
    </header>

    <section class="section content-grid">
      <form class="card form-list">
        <div v-for="metric in reviewMetrics" :key="metric" class="rating-row">
          <span>{{ metric }}</span>
          <van-rate v-model="ratings[metric]" color="#ff694f" void-icon="star" />
        </div>
        <div class="field-card">
          <label>评价内容</label>
          <textarea v-model="content" placeholder="分享卫生、服务、品质和性价比体验"></textarea>
        </div>
        <p v-if="error" class="form-error">{{ error }}</p>
        <button class="primary-pill" type="button" :disabled="submitting" @click="submitReview">
          {{ submitting ? '提交中…' : '提交评价' }}
        </button>
      </form>

      <aside class="card">
        <h2>{{ stallName }}</h2>
        <p class="muted">评价提交后写入后端，可在「我的评价」和摊位详情看到。</p>
      </aside>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '@yuntanfang/api'

const route = useRoute()
const router = useRouter()
const orderId = route.params.orderId as string
const reviewMetrics = ['卫生', '服务', '品质', '性价比']
const content = ref('')
const error = ref('')
const submitting = ref(false)
const stallName = ref('')
const ratings = reactive<Record<string, number>>({ 卫生: 5, 服务: 5, 品质: 5, 性价比: 5 })

onMounted(async () => {
  try {
    const res = await orderApi.detail(orderId)
    stallName.value = res.data.data?.order?.stallName ?? ''
  } catch {
    stallName.value = ''
  }
})

async function submitReview() {
  error.value = ''
  submitting.value = true
  try {
    const avg = Math.round(reviewMetrics.reduce((sum, m) => sum + ratings[m], 0) / reviewMetrics.length)
    await orderApi.review(orderId, { rating: avg, content: content.value })
    router.push('/my-reviews')
  } catch (err: any) {
    error.value = err?.response?.data?.message ?? '提交失败，请确认已登录'
  } finally {
    submitting.value = false
  }
}
</script>
